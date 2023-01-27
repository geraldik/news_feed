/*
 * This file is generated by jOOQ.
 */
package com.gmail.geraldik.newsfeed.pesristence.tables;


import com.gmail.geraldik.newsfeed.pesristence.Keys;
import com.gmail.geraldik.newsfeed.pesristence.Public;
import com.gmail.geraldik.newsfeed.pesristence.tables.records.ItemRecord;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row5;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * Создаваемая новость
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Item extends TableImpl<ItemRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.item</code>
     */
    public static final Item ITEM = new Item();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<ItemRecord> getRecordType() {
        return ItemRecord.class;
    }

    /**
     * The column <code>public.item.id</code>. Идентификатор новости
     */
    public final TableField<ItemRecord, Integer> ID = createField(DSL.name("id"), SQLDataType.INTEGER.nullable(false).identity(true), this, "Идентификатор новости");

    /**
     * The column <code>public.item.title</code>. Заголовк новости
     */
    public final TableField<ItemRecord, String> TITLE = createField(DSL.name("title"), SQLDataType.CLOB.nullable(false), this, "Заголовк новости");

    /**
     * The column <code>public.item.body</code>. Текст новости
     */
    public final TableField<ItemRecord, String> BODY = createField(DSL.name("body"), SQLDataType.CLOB.nullable(false), this, "Текст новости");

    /**
     * The column <code>public.item.author</code>. Автор новости
     */
    public final TableField<ItemRecord, String> AUTHOR = createField(DSL.name("author"), SQLDataType.CLOB.nullable(false), this, "Автор новости");

    /**
     * The column <code>public.item.created</code>. Дата создания новости
     */
    public final TableField<ItemRecord, LocalDateTime> CREATED = createField(DSL.name("created"), SQLDataType.LOCALDATETIME(6).nullable(false), this, "Дата создания новости");

    private Item(Name alias, Table<ItemRecord> aliased) {
        this(alias, aliased, null);
    }

    private Item(Name alias, Table<ItemRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment("Создаваемая новость"), TableOptions.table());
    }

    /**
     * Create an aliased <code>public.item</code> table reference
     */
    public Item(String alias) {
        this(DSL.name(alias), ITEM);
    }

    /**
     * Create an aliased <code>public.item</code> table reference
     */
    public Item(Name alias) {
        this(alias, ITEM);
    }

    /**
     * Create a <code>public.item</code> table reference
     */
    public Item() {
        this(DSL.name("item"), null);
    }

    public <O extends Record> Item(Table<O> child, ForeignKey<O, ItemRecord> key) {
        super(child, key, ITEM);
    }

    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    @Override
    public Identity<ItemRecord, Integer> getIdentity() {
        return (Identity<ItemRecord, Integer>) super.getIdentity();
    }

    @Override
    public UniqueKey<ItemRecord> getPrimaryKey() {
        return Keys.ITEM_PKEY;
    }

    @Override
    public List<UniqueKey<ItemRecord>> getKeys() {
        return Arrays.<UniqueKey<ItemRecord>>asList(Keys.ITEM_PKEY, Keys.ITEM_TITLE_KEY);
    }

    @Override
    public Item as(String alias) {
        return new Item(DSL.name(alias), this);
    }

    @Override
    public Item as(Name alias) {
        return new Item(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Item rename(String name) {
        return new Item(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Item rename(Name name) {
        return new Item(name, null);
    }

    // -------------------------------------------------------------------------
    // Row5 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row5<Integer, String, String, String, LocalDateTime> fieldsRow() {
        return (Row5) super.fieldsRow();
    }
}
