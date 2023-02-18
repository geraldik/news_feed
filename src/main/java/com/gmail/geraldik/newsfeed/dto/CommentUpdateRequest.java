package com.gmail.geraldik.newsfeed.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CommentUpdateRequest {

    @NotNull
    private Integer id;
    private String  commentator;
    private String  body;
    private int itemId;
}
