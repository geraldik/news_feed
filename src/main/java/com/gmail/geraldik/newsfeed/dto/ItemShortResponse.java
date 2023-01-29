package com.gmail.geraldik.newsfeed.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;


/**
 * This class defines the returning object for the request to save item
*/
@Data
public class ItemShortResponse {

    @NotEmpty(message = "Id should not be empty")
    private int id;
    private String title;
    private String body;
    private String author;

}
