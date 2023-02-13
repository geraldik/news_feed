package com.gmail.geraldik.newsfeed.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;


/**
 * This class defines the object included in the update request
 */
@Data
@Accessors(chain = true)
public class ItemUpdateRequest {

    @NotNull(message = "The id should not be empty")
    private final Integer id;
    @NotEmpty(message = "The title should not be empty")
    private final String title;
    @NotEmpty(message = "The body should not be empty")
    private final String body;
    private final String author;

}
