package com.gmail.geraldik.newsfeed.service;

import com.gmail.geraldik.newsfeed.dto.ItemSaveRequest;
import com.gmail.geraldik.newsfeed.dto.ItemShortResponse;
import com.gmail.geraldik.newsfeed.dto.ItemShortWithCommentNum;
import com.gmail.geraldik.newsfeed.dto.ItemUpdateRequest;
import com.gmail.geraldik.newsfeed.page.SimplePage;
import org.springframework.data.domain.Sort;

/**
 * Defines a service layer of items
 */

public interface ItemService {

    ItemShortResponse save(ItemSaveRequest itemSaveRequest);

    ItemShortResponse update(ItemUpdateRequest itemUpdateRequest);

    SimplePage<ItemShortWithCommentNum> findPaginated(int page, int size, Sort sort);
}
