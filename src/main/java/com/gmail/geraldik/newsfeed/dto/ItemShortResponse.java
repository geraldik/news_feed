package com.gmail.geraldik.newsfeed.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.experimental.Accessors;


/**
 * This class defines the returning object for the request to save item
*/
@Data
@Accessors(chain = true)
public class ItemShortResponse {

    @NotEmpty(message = "Id should not be empty")
    private int id;
    private String title;
    private String body;
    private String author;

}
