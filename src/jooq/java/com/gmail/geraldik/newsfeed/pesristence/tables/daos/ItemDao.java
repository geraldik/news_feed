/*
 * This file is generated by jOOQ.
 */
package com.gmail.geraldik.newsfeed.pesristence.tables.daos;


import com.gmail.geraldik.newsfeed.pesristence.tables.Item;
import com.gmail.geraldik.newsfeed.pesristence.tables.records.ItemRecord;

import java.time.LocalDateTime;
import java.util.List;

import org.jooq.Configuration;
import org.jooq.impl.DAOImpl;


/**
 * Created news
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class ItemDao extends DAOImpl<ItemRecord, com.gmail.geraldik.newsfeed.pesristence.tables.pojos.Item, Integer> {

    /**
     * Create a new ItemDao without any configuration
     */
    public ItemDao() {
        super(Item.ITEM, com.gmail.geraldik.newsfeed.pesristence.tables.pojos.Item.class);
    }

    /**
     * Create a new ItemDao with an attached configuration
     */
    public ItemDao(Configuration configuration) {
        super(Item.ITEM, com.gmail.geraldik.newsfeed.pesristence.tables.pojos.Item.class, configuration);
    }

    @Override
    public Integer getId(com.gmail.geraldik.newsfeed.pesristence.tables.pojos.Item object) {
        return object.getId();
    }

    /**
     * Fetch records that have <code>id BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<com.gmail.geraldik.newsfeed.pesristence.tables.pojos.Item> fetchRangeOfId(Integer lowerInclusive, Integer upperInclusive) {
        return fetchRange(Item.ITEM.ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>id IN (values)</code>
     */
    public List<com.gmail.geraldik.newsfeed.pesristence.tables.pojos.Item> fetchById(Integer... values) {
        return fetch(Item.ITEM.ID, values);
    }

    /**
     * Fetch a unique record that has <code>id = value</code>
     */
    public com.gmail.geraldik.newsfeed.pesristence.tables.pojos.Item fetchOneById(Integer value) {
        return fetchOne(Item.ITEM.ID, value);
    }

    /**
     * Fetch records that have <code>title BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<com.gmail.geraldik.newsfeed.pesristence.tables.pojos.Item> fetchRangeOfTitle(String lowerInclusive, String upperInclusive) {
        return fetchRange(Item.ITEM.TITLE, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>title IN (values)</code>
     */
    public List<com.gmail.geraldik.newsfeed.pesristence.tables.pojos.Item> fetchByTitle(String... values) {
        return fetch(Item.ITEM.TITLE, values);
    }

    /**
     * Fetch a unique record that has <code>title = value</code>
     */
    public com.gmail.geraldik.newsfeed.pesristence.tables.pojos.Item fetchOneByTitle(String value) {
        return fetchOne(Item.ITEM.TITLE, value);
    }

    /**
     * Fetch records that have <code>body BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<com.gmail.geraldik.newsfeed.pesristence.tables.pojos.Item> fetchRangeOfBody(String lowerInclusive, String upperInclusive) {
        return fetchRange(Item.ITEM.BODY, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>body IN (values)</code>
     */
    public List<com.gmail.geraldik.newsfeed.pesristence.tables.pojos.Item> fetchByBody(String... values) {
        return fetch(Item.ITEM.BODY, values);
    }

    /**
     * Fetch records that have <code>author BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<com.gmail.geraldik.newsfeed.pesristence.tables.pojos.Item> fetchRangeOfAuthor(String lowerInclusive, String upperInclusive) {
        return fetchRange(Item.ITEM.AUTHOR, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>author IN (values)</code>
     */
    public List<com.gmail.geraldik.newsfeed.pesristence.tables.pojos.Item> fetchByAuthor(String... values) {
        return fetch(Item.ITEM.AUTHOR, values);
    }

    /**
     * Fetch records that have <code>created BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<com.gmail.geraldik.newsfeed.pesristence.tables.pojos.Item> fetchRangeOfCreated(LocalDateTime lowerInclusive, LocalDateTime upperInclusive) {
        return fetchRange(Item.ITEM.CREATED, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>created IN (values)</code>
     */
    public List<com.gmail.geraldik.newsfeed.pesristence.tables.pojos.Item> fetchByCreated(LocalDateTime... values) {
        return fetch(Item.ITEM.CREATED, values);
    }
}
