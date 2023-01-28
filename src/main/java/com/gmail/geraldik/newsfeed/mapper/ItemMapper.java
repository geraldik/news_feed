package com.gmail.geraldik.newsfeed.mapper;

import com.gmail.geraldik.newsfeed.dto.ItemSaveRequest;
import com.gmail.geraldik.newsfeed.dto.ItemShortResponse;
import com.gmail.geraldik.newsfeed.pesristence.tables.pojos.Item;
import org.springframework.stereotype.Component;

@Component
public class ItemMapper {

    /**
     * Convert ItemSaveRequest to a POJO version
     */
    public Item toEntity(ItemSaveRequest itemSaveRequest) {
        Item item = new Item();
        item.setTitle(itemSaveRequest.getTitle());
        item.setBody(itemSaveRequest.getBody());
        item.setAuthor(itemSaveRequest.getAuthor());
        return item;
    }

    /**
     * Convert ItemSaveRequest to a short version for response
     */
    public ItemShortResponse toItemShortResponse(ItemSaveRequest itemSaveRequest, int id) {
        var shortDto = new ItemShortResponse();
        shortDto.setId(id);
        shortDto.setTitle(itemSaveRequest.getTitle());
        shortDto.setBody(cutTheBody(itemSaveRequest.getBody()));
        shortDto.setAuthor(itemSaveRequest.getAuthor());
        return shortDto;
    }

    private String cutTheBody(String body) {
        return body.length() > 50 ? body.substring(0, 50) : body;
    }
}
