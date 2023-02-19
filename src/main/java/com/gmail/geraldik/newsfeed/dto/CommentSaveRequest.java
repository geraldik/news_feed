package com.gmail.geraldik.newsfeed.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CommentSaveRequest {

    @NotNull(message = "The name of commentator must be not null")
    @Size(min = 3, max = 30, message = "The name of commentator must be in range of 3-300 characters")
    private String commentator;
    @NotNull(message = "The comment must be not null")
    @Size(min = 1, max = 500, message = "Comment length must be in the range of 1-500 characters")
    private String body;
    @NotNull(message = "The id of item must be not null")
    private Integer itemId;
}
