/*
 * This file is generated by jOOQ.
 */
package com.gmail.geraldik.newsfeed.pesristence.tables.pojos;


import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * Создаваемая новость
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Item implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer       id;
    private String        title;
    private String        body;
    private String        author;
    private LocalDateTime created;

    public Item() {}

    public Item(Item value) {
        this.id = value.id;
        this.title = value.title;
        this.body = value.body;
        this.author = value.author;
        this.created = value.created;
    }

    public Item(
        Integer       id,
        String        title,
        String        body,
        String        author,
        LocalDateTime created
    ) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.author = author;
        this.created = created;
    }

    /**
     * Getter for <code>public.item.id</code>. Идентификатор новости
     */
    public Integer getId() {
        return this.id;
    }

    /**
     * Setter for <code>public.item.id</code>. Идентификатор новости
     */
    public Item setId(Integer id) {
        this.id = id;
        return this;
    }

    /**
     * Getter for <code>public.item.title</code>. Заголовк новости
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Setter for <code>public.item.title</code>. Заголовк новости
     */
    public Item setTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * Getter for <code>public.item.body</code>. Текст новости
     */
    public String getBody() {
        return this.body;
    }

    /**
     * Setter for <code>public.item.body</code>. Текст новости
     */
    public Item setBody(String body) {
        this.body = body;
        return this;
    }

    /**
     * Getter for <code>public.item.author</code>. Автор новости
     */
    public String getAuthor() {
        return this.author;
    }

    /**
     * Setter for <code>public.item.author</code>. Автор новости
     */
    public Item setAuthor(String author) {
        this.author = author;
        return this;
    }

    /**
     * Getter for <code>public.item.created</code>. Дата создания новости
     */
    public LocalDateTime getCreated() {
        return this.created;
    }

    /**
     * Setter for <code>public.item.created</code>. Дата создания новости
     */
    public Item setCreated(LocalDateTime created) {
        this.created = created;
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Item other = (Item) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        }
        else if (!id.equals(other.id))
            return false;
        if (title == null) {
            if (other.title != null)
                return false;
        }
        else if (!title.equals(other.title))
            return false;
        if (body == null) {
            if (other.body != null)
                return false;
        }
        else if (!body.equals(other.body))
            return false;
        if (author == null) {
            if (other.author != null)
                return false;
        }
        else if (!author.equals(other.author))
            return false;
        if (created == null) {
            if (other.created != null)
                return false;
        }
        else if (!created.equals(other.created))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        result = prime * result + ((this.title == null) ? 0 : this.title.hashCode());
        result = prime * result + ((this.body == null) ? 0 : this.body.hashCode());
        result = prime * result + ((this.author == null) ? 0 : this.author.hashCode());
        result = prime * result + ((this.created == null) ? 0 : this.created.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Item (");

        sb.append(id);
        sb.append(", ").append(title);
        sb.append(", ").append(body);
        sb.append(", ").append(author);
        sb.append(", ").append(created);

        sb.append(")");
        return sb.toString();
    }
}
