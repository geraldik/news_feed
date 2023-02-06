package com.gmail.geraldik.newsfeed.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;


/**
 * This class defines the object included in the update request
 */
@Data
@Accessors(chain = true)
@Builder
public class ItemUpdateRequest {

    @NotEmpty(message = "The id should not be empty")
    private int id;
    @NotEmpty(message = "The title should not be empty")
    private String title;
    @NotEmpty(message = "The body should not be empty")
    private String body;
    private String author;

}
