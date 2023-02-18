package com.gmail.geraldik.newsfeed.dto;

import lombok.Data;

/**
 * This class defines the returning object for the request to get comment
 */

@Data
public class CommentFullResponse {

    private Integer id;
    private String commentator;
    private String body;
    private String itemId;
}
