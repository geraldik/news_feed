package com.gmail.geraldik.newsfeed.repository;

import static com.gmail.geraldik.newsfeed.pesristence.Tables.COMMENT;
import static com.gmail.geraldik.newsfeed.pesristence.Tables.ITEM;
import static org.jooq.impl.DSL.count;

import com.gmail.geraldik.newsfeed.pesristence.tables.pojos.Item;
import com.gmail.geraldik.newsfeed.pojo.ItemWithCommentNum;
import lombok.RequiredArgsConstructor;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.OrderField;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

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
            int page, int size, OrderField[] orders,
            List<Condition> whereConditions, List<Condition> havingConditions) {
        return dsl.select(
                        ITEM.ID,
                        ITEM.TITLE,
                        ITEM.BODY,
                        ITEM.AUTHOR,
                        count(COMMENT.ID).as("comment_num"))
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
}