package com.gmail.geraldik.newsfeed.repository;

import com.gmail.geraldik.newsfeed.pesristence.tables.pojos.Item;
import com.gmail.geraldik.newsfeed.pojo.ItemWithCommentNum;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.SortField;
import org.jooq.impl.DSL;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Set;

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
        System.out.println(dsl.configuration().dialect().getName());
        return dsl.update(ITEM)
                .set(ITEM.TITLE, item.getTitle())
                .set(ITEM.BODY, item.getBody())
                .set(ITEM.AUTHOR, item.getAuthor())
                .set(ITEM.CREATED, LocalDateTime.now(ZoneOffset.UTC))
                .where(ITEM.ID.eq(item.getId()))
                .execute() > 0;
    }

    public List<ItemWithCommentNum> findAllWithLimitAndOffsetAndSort(int page, int size, Sort sort) {
        var sortFields = toSortField(sort);
        return dsl.select(
                        ITEM.ID,
                        ITEM.TITLE,
                        ITEM.BODY,
                        ITEM.AUTHOR,
                        count(COMMENT.ID).as("comment_num"))
                .from(ITEM.leftJoin(COMMENT)
                        .on(ITEM.ID.eq(COMMENT.ITEM_ID)))
                .groupBy(ITEM.ID)
                .orderBy(sortFields)
                .offset(size * page)
                .limit(size)
                .fetchInto(ItemWithCommentNum.class);
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
     * @param isAscending Specifies the sort order. Set to `true` for ascending, and `false` for descending.
     * @param property Specifies the property to sort by.
     * @return A sort field based on the specified property and sort order.
     */
    private SortField<?> ascOrDesc(boolean isAscending, String property) {
        Field<?> field = property.equals(COMMENT_NUM)
                ? DSL.field(COMMENT_NUM_DB)
                : ITEM.field(property);
        return isAscending ? field.asc() : field.desc();
    }
}