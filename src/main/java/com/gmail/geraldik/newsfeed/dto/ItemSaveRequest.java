package com.gmail.geraldik.newsfeed.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * This class defines the object included in the save request
 */
@Data
@Accessors(chain = true)
public class ItemSaveRequest {

    @NotEmpty(message = "The title should not be empty")
    private String title;
    @NotEmpty(message = "The body should not be empty")
    private String body;
    private String author;
}
