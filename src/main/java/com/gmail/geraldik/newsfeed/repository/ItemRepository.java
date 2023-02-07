package com.gmail.geraldik.newsfeed.repository;

import static com.gmail.geraldik.newsfeed.pesristence.Tables.COMMENT;
import static com.gmail.geraldik.newsfeed.pesristence.Tables.ITEM;
import static org.jooq.impl.DSL.count;

import com.gmail.geraldik.newsfeed.filter.ItemPageFilter;
import com.gmail.geraldik.newsfeed.pesristence.tables.pojos.Item;
import com.gmail.geraldik.newsfeed.pojo.ItemWithCommentNum;
import lombok.RequiredArgsConstructor;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.OrderField;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

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
        System.out.println(dsl.configuration().dialect().getName());
        return dsl.update(ITEM)
                .set(ITEM.TITLE, item.getTitle())
                .set(ITEM.BODY, item.getBody())
                .set(ITEM.AUTHOR, item.getAuthor())
                .set(ITEM.CREATED, LocalDateTime.now(ZoneOffset.UTC))
                .where(ITEM.ID.eq(item.getId()))
                .execute() > 0;
    }

    public List<ItemWithCommentNum> findAllWithLimitAndOffsetAndSortAndFilter(
            int page, int size, OrderField[] orders, ItemPageFilter filter) {
        List<Condition> whereConditions = forWhereClause(filter);
        List<Condition> havingConditions = forHavingClause(filter);
        return dsl.select(
                        ITEM.ID,
                        ITEM.TITLE,
                        ITEM.BODY,
                        ITEM.AUTHOR,
                        count(COMMENT.ID))
                .from(ITEM.leftJoin(COMMENT)
                        .on(ITEM.ID.eq(COMMENT.ITEM_ID)))
                .where(whereConditions)
                .groupBy(ITEM.ID)
                .having(havingConditions)
                .orderBy(orders)
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
                                        .greaterOrEqual(new Timestamp(createdFrom))),
                        Optional.ofNullable(filter.getCreatedTo())
                                .map(createdTo -> DSL.field("created")
                                        .lessOrEqual(new Timestamp(createdTo))))
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