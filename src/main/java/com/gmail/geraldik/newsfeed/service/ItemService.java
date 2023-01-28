package com.gmail.geraldik.newsfeed.service;

import com.gmail.geraldik.newsfeed.dto.ItemSaveRequest;
import com.gmail.geraldik.newsfeed.dto.ItemShortResponse;

/**
 * Defines a service layer of items
 */

public interface ItemService {

    ItemShortResponse save(ItemSaveRequest itemSaveRequest);

}
