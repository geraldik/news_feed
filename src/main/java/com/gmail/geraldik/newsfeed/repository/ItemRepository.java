package com.gmail.geraldik.newsfeed.repository;

import static com.gmail.geraldik.newsfeed.pesristence.Tables.ITEM;

import com.gmail.geraldik.newsfeed.pesristence.tables.pojos.Item;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;


@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final DSLContext dsl;

    public Integer insert(Item item) {
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

    public boolean update(Item item) {
        System.out.println(dsl.configuration().dialect().getName());
        return dsl.update(ITEM)
                .set(ITEM.TITLE, item.getTitle())
                .set(ITEM.BODY, item.getBody())
                .set(ITEM.AUTHOR, item.getAuthor())
                .set(ITEM.CREATED, LocalDateTime.now(ZoneOffset.UTC))
                .where(ITEM.ID.eq(item.getId()))
                .execute() > 0;
    }

    public Page<Item> findPaginated(Pageable pageable) {
        List<Item> content = dsl.selectFrom(ITEM)
                .offset(pageable.getPageSize() * pageable.getPageNumber())
                .limit(pageable.getPageSize())
                .fetchInto(Item.class);
        int total = dsl.fetchCount(ITEM);
        return new PageImpl<>(content, pageable, total);
    }
}