package com.gmail.geraldik.newsfeed.mapper;

import com.gmail.geraldik.newsfeed.dto.ItemSaveRequest;
import com.gmail.geraldik.newsfeed.dto.ItemShortResponse;
import com.gmail.geraldik.newsfeed.dto.ItemUpdateRequest;
import com.gmail.geraldik.newsfeed.pesristence.tables.pojos.Item;
import org.springframework.stereotype.Component;

@Component
public class ItemMapper {

    /**
     * Convert entering Item to be saved to a POJO version
     */
    public Item toEntity(ItemSaveRequest itemSaveRequest) {
        Item item = new Item();
        item.setTitle(itemSaveRequest.getTitle());
        item.setBody(itemSaveRequest.getBody());
        item.setAuthor(itemSaveRequest.getAuthor());
        return item;
    }

    /**
     * Convert entering Item to be saved to a short version
     */
    public ItemShortResponse toItemShortResponse(ItemSaveRequest itemSaveRequest, int id) {
        var shortDto = new ItemShortResponse();
        shortDto.setId(id);
        shortDto.setTitle(itemSaveRequest.getTitle());
        shortDto.setBody(cutTheBody(itemSaveRequest.getBody()));
        shortDto.setAuthor(itemSaveRequest.getAuthor());
        return shortDto;
    }

    /**
     * Convert entering Item to be updated to a short version
     */
    public ItemShortResponse toItemShortResponse(ItemUpdateRequest itemUpdateRequest) {
        var shortDto = new ItemShortResponse();
        shortDto.setId(itemUpdateRequest.getId());
        shortDto.setTitle(itemUpdateRequest.getTitle());
        shortDto.setBody(cutTheBody(itemUpdateRequest.getBody()));
        shortDto.setAuthor(itemUpdateRequest.getAuthor());
        return shortDto;
    }

    /**
     * Convert POJO Item to a short version
     */
    public ItemShortResponse toItemShortResponse(Item item) {
        var shortDto = new ItemShortResponse();
        shortDto.setId(item.getId());
        shortDto.setTitle(item.getTitle());
        shortDto.setBody(cutTheBody(item.getBody()));
        shortDto.setAuthor(item.getAuthor());
        return shortDto;
    }

    /**
     * Convert entering Item to be updated to a short version
     */
    public Item toUpdateEntity(ItemUpdateRequest itemUpdateRequest) {
        Item item = new Item();
        item.setId(itemUpdateRequest.getId());
        item.setTitle(itemUpdateRequest.getTitle());
        item.setBody(itemUpdateRequest.getBody());
        item.setAuthor(itemUpdateRequest.getAuthor());
        return item;
    }

    /**
     *Cutting text of item to a short version
     */
    private String cutTheBody(String body) {
        return body.length() > 50 ? body.substring(0, 50) : body;
    }
}
