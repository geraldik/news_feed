package com.gmail.geraldik.newsfeed.dto;

import lombok.Data;

@Data
public class ItemShortWithCommentNum {

    private Integer id;
    private String title;
    private String body;
    private String author;
    private int commentNum;
}
