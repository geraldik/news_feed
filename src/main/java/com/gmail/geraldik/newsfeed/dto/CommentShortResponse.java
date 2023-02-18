package com.gmail.geraldik.newsfeed.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * This class defines the returning object for the request to save comment
 */
@Data
@Accessors(chain = true)
public class CommentShortResponse {

    private Integer id;
    private String  commentator;
    private String  body;
    private Integer itemId;

}