package com.gmail.geraldik.newsfeed.dto;

import lombok.Data;

/**
 * This class defines the returning object for the get request of one item
 */
@Data
public class    ItemFullResponse {

    private int id;
    private String title;
    private String body;
    private String author;
}
