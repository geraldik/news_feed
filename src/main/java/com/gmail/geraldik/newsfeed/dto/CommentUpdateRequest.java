package com.gmail.geraldik.newsfeed.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentUpdateRequest {

    @NotNull(message = "The id should not be empty")
    private Integer id;
    private String  commentator;
    private String  body;
    private int itemId;
}
