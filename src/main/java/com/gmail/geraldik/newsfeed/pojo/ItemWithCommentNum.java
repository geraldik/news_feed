package com.gmail.geraldik.newsfeed.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ItemWithCommentNum {

    private Integer id;
    private String title;
    private String body;
    private String author;
    private int commentNum;
}
