package com.gmail.geraldik.newsfeed.repository;

import static com.gmail.geraldik.newsfeed.pesristence.Tables.ITEM;

import com.gmail.geraldik.newsfeed.pesristence.tables.pojos.Item;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

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
}
