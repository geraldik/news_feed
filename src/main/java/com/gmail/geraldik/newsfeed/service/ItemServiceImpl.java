package com.gmail.geraldik.newsfeed.service;

import com.gmail.geraldik.newsfeed.dto.ItemSaveRequest;
import com.gmail.geraldik.newsfeed.dto.ItemShortResponse;
import com.gmail.geraldik.newsfeed.dto.ItemUpdateRequest;
import com.gmail.geraldik.newsfeed.mapper.ItemMapper;
import com.gmail.geraldik   .newsfeed.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository repository;
    private final ItemMapper mapper;

    /**
     * Save Item in DB
     */
    @Override
    public ItemShortResponse save(ItemSaveRequest itemSaveRequest) {
        var item = mapper.toEntity(itemSaveRequest);
        int id = repository.insert(item);
        return mapper.toItemShortResponse(itemSaveRequest, id);
    }

    /**
     * Update Item in DB
     */
    @Override
    public ItemShortResponse update(ItemUpdateRequest itemUpdateRequest) {
        var item = mapper.toUpdateEntity(itemUpdateRequest);
        if (!repository.update(item)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Item with id = %d not found", item.getId()));
        }
        return mapper.toItemShortResponse(itemUpdateRequest);
    }
}
