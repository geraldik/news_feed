package com.gmail.geraldik.newsfeed.service;

import com.gmail.geraldik.newsfeed.dto.ItemSaveRequest;
import com.gmail.geraldik.newsfeed.dto.ItemShortResponse;
import com.gmail.geraldik.newsfeed.dto.ItemUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Defines a service layer of items
 */

public interface ItemService {

    ItemShortResponse save(ItemSaveRequest itemSaveRequest);

    ItemShortResponse update(ItemUpdateRequest itemUpdateRequest);

     Page<ItemShortResponse> findPaginated(Pageable pageable);
}
