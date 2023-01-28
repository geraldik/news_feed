package com.gmail.geraldik.newsfeed.service;

import com.gmail.geraldik.newsfeed.dto.ItemSaveRequest;
import com.gmail.geraldik.newsfeed.dto.ItemShortResponse;
import com.gmail.geraldik.newsfeed.mapper.ItemMapper;
import com.gmail.geraldik   .newsfeed.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService{

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
}
