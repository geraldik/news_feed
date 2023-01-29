package com.gmail.geraldik.newsfeed.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;


/**
 * This class defines the object included in the update request
 */
@Data
public class ItemUpdateRequest {

    private int id;
    @NotEmpty(message = "The title should not be empty")
    private String title;
    @NotEmpty(message = "The body should not be empty")
    private String body;
    private String author;

}
