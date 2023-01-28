package com.gmail.geraldik.newsfeed.dto;

import lombok.Data;


/**
 * This class defines the returning object for the request to save item
*/
@Data
public class ItemShortResponse {

    private int id;
    private String title;
    private String body;
    private String author;

}
