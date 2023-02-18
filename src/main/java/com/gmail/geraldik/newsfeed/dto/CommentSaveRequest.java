package com.gmail.geraldik.newsfeed.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentSaveRequest {

    @Size(min = 3, max = 30, message = "The name of commentator must be in range of 3-300 characters")
    @Size()
    private String  commentator;
    @Size(min = 1, max = 500, message = "Comment length must be in the range of 1-500 characters")
    private String  body;
    private Integer itemId;
}
