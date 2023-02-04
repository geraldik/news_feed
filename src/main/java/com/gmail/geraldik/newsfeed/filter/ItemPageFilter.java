package com.gmail.geraldik.newsfeed.filter;

import lombok.Data;

@Data
public class ItemPageFilter {

    private String author;
    private Long createdFrom;
    private Long createdTo;
    private Long commentNumFrom;
    private Long commentNumTo;
}

