package com.gmail.geraldik.newsfeed.repository;

import com.gmail.geraldik.newsfeed.dto.ItemFullResponse;
import com.gmail.geraldik.newsfeed.filter.ItemPageFilter;
import com.gmail.geraldik.newsfeed.pesristence.tables.pojos.Item;
import com.gmail.geraldik.newsfeed.pojo.ItemWithCommentNum;
import lombok.RequiredArgsConstructor;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.SortField;
import org.jooq.impl.DSL;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static com.gmail.geraldik.newsfeed.pesristence.Tables.COMMENT;
import static com.gmail.geraldik.newsfeed.pesristence.Tables.ITEM;
import static org.jooq.impl.DSL.count;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final static List<? extends SortField<?>> DEF_SORT =
            List.of(ITEM.field("created").desc());
    private final static Set<String> FIELDS = Set.of(
            "author", "created", "title", "commentNum");
    private static final String COMMENT_NUM = "commentNum";
    private static final String COMMENT_NUM_DB = "comment_num";
    private static final String COUNT_PUBLIC_COMMENT_ID = "count(\"public\".\"comment\".\"id\")";

    private final DSLContext dsl;

    public Integer insertOne(Item item) {
        return dsl.insertInto(
                        ITEM,
                        ITEM.TITLE,
                        ITEM.BODY,
                        ITEM.AUTHOR,
                        ITEM.CREATED
                )
                .values(
                        item.getTitle(),
                        item.getBody(),
                        item.getAuthor(),
                        LocalDateTime.now(ZoneOffset.UTC)
                )
                .returning(ITEM.ID)
                .fetchOne()
                .getValue(ITEM.ID);
    }

    public boolean updateOne(Item item) {
        return dsl.update(ITEM)
                .set(ITEM.TITLE, item.getTitle())
                .set(ITEM.BODY, item.getBody())
                .set(ITEM.AUTHOR, item.getAuthor())
                .set(ITEM.CREATED, LocalDateTime.now(ZoneOffset.UTC))
                .set(ITEM.DISABLE, item.getDisable())
                .where(ITEM.ID.eq(item.getId()))
                .execute() > 0;
    }

    public List<ItemWithCommentNum> findAllWithLimitAndOffsetAndSortIsNotDisabled(
            int page, int size, Sort sort, ItemPageFilter filter) {
        List<Condition> whereConditions = forWhereClause(filter);
        List<Condition> havingConditions = forHavingClause(filter);
        var sortFields = toSortField(sort);
        return dsl.select(
                        ITEM.ID,
                        ITEM.TITLE,
                        ITEM.BODY,
                        ITEM.AUTHOR,
                        count(COMMENT.ID).as("comment_num"))
                .from(ITEM.leftJoin(COMMENT)
                        .on(ITEM.ID.eq(COMMENT.ITEM_ID)))
                .where(whereConditions)
                .and(ITEM.DISABLE.eq(false))
                .groupBy(ITEM.ID)
                .having(havingConditions)
                .orderBy(sortFields)
                .offset(size * page)
                .limit(size)
                .fetchInto(ItemWithCommentNum.class);
    }

    public Optional<ItemFullResponse> findItem(int itemId) {
        var result = dsl.fetchOne(ITEM, ITEM.ID.eq(itemId));
        return result != null
                ? Optional.of(result.into(ItemFullResponse.class))
                : Optional.empty();
    }

    public int countAllItem() {
        return dsl.selectCount()
                .from(ITEM)
                .fetchOne(0, int.class);
    }

    /**
     * Converts the given `Sort` object to a list of `SortField` objects.
     * If the `Sort` object is unsorted, the method returns a default sort list.
     * If the `Sort` object contains properties not in the allowed fields list, they are filtered out.
     * The remaining properties are sorted according to the ascending or descending order specified in the `Sort` object.
     *
     * @param sort the `Sort` object to be converted
     * @return a list of `SortField` objects based on the given `Sort` object
     */
    private List<? extends SortField<?>> toSortField(Sort sort) {
        if (sort.isUnsorted()) {
            return DEF_SORT;
        }
        var fields = sort.stream()
                .filter(order -> FIELDS.contains(order.getProperty()))
                .map(order -> ascOrDesc(order.isAscending(), order.getProperty()))
                .toList();
        return fields.isEmpty() ? DEF_SORT : fields;
    }

    /**
     * Creates a sort field based on the specified property and sort order.
     *
     * @param isAscending Specifies the sort order. Set to `true` for ascending, and `false` for descending.
     * @param property    Specifies the property to sort by.
     * @return A sort field based on the specified property and sort order.
     */
    private SortField<?> ascOrDesc(boolean isAscending, String property) {
        Field<?> field = property.equals(COMMENT_NUM)
                ? DSL.field(COMMENT_NUM_DB)
                : ITEM.field(property);
        return isAscending ? field.asc() : field.desc();
    }

    /**
     * This method returns a list of conditions for filtering item records using the non-null fields of
     * the {@link ItemPageFilter} object.
     * The returned conditions can be used in the WHERE clause of a SQL query.
     *
     * @param filter the {@link ItemPageFilter} object used to determine the conditions for filtering the item records
     * @return a list of conditions for filtering the item records based on the non-null properties of the
     * {@link ItemPageFilter} object
     */
    private List<Condition> forWhereClause(ItemPageFilter filter) {
        return Stream.of(
                        Optional.ofNullable(filter.getAuthor())
                                .map(author -> DSL.field("author")
                                        .eq(author)),
                        Optional.ofNullable(filter.getCreatedFrom())
                                .map(createdFrom -> DSL.field("created")
                                        .greaterOrEqual(LocalDateTime.ofInstant(
                                                Instant.ofEpochMilli(createdFrom), ZoneId.of("UTC")))),
                        Optional.ofNullable(filter.getCreatedTo())
                                .map(createdTo -> DSL.field("created")
                                        .lessOrEqual(LocalDateTime.ofInstant(
                                                Instant.ofEpochMilli(createdTo), ZoneId.of("UTC"))
                                        )))
                .flatMap(Optional::stream)
                .toList();
    }

    /**
     * This method returns a list of conditions for the HAVING clause using the non-null fields of the
     * {@link ItemPageFilter} object.
     * The conditions are created based on the number of comments for each item, using the 'count'
     * function and the 'id' field
     * of the 'comment' table.
     *
     * @param filter ItemPageFilter object that contains the values to be used in the conditions
     * @return a list of conditions for the HAVING clause
     */
    private List<Condition> forHavingClause(ItemPageFilter filter) {
        return Stream.of(
                        Optional.ofNullable(filter.getCommentNumFrom())
                                .map(author -> DSL.field(COUNT_PUBLIC_COMMENT_ID)
                                        .greaterOrEqual(filter.getCommentNumFrom())),
                        Optional.ofNullable(filter.getCommentNumTo())
                                .map(author -> DSL.field(COUNT_PUBLIC_COMMENT_ID)
                                        .lessOrEqual((filter.getCommentNumTo()))))
                .flatMap(Optional::stream)
                .toList();
    }
}