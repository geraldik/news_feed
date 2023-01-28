/*
 * This file is generated by jOOQ.
 */
package com.gmail.geraldik.newsfeed.pesristence;


import com.gmail.geraldik.newsfeed.pesristence.tables.Comment;
import com.gmail.geraldik.newsfeed.pesristence.tables.Item;

import java.util.Arrays;
import java.util.List;

import org.jooq.Catalog;
import org.jooq.Sequence;
import org.jooq.Table;
import org.jooq.impl.SchemaImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Public extends SchemaImpl {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public</code>
     */
    public static final Public PUBLIC = new Public();

    /**
     * Comment on the news
     */
    public final Comment COMMENT = Comment.COMMENT;

    /**
     * Created news
     */
    public final Item ITEM = Item.ITEM;

    /**
     * No further instances allowed
     */
    private Public() {
        super("public", null);
    }


    @Override
    public Catalog getCatalog() {
        return DefaultCatalog.DEFAULT_CATALOG;
    }

    @Override
    public final List<Sequence<?>> getSequences() {
        return Arrays.<Sequence<?>>asList(
            Sequences.COMMENT_ID_SEQ,
            Sequences.ITEM_ID_SEQ);
    }

    @Override
    public final List<Table<?>> getTables() {
        return Arrays.<Table<?>>asList(
            Comment.COMMENT,
            Item.ITEM);
    }
}
