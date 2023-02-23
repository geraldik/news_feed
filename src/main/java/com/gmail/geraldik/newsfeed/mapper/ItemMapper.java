package com.gmail.geraldik.newsfeed.mapper;

import com.gmail.geraldik.newsfeed.dto.ItemSaveRequest;
import com.gmail.geraldik.newsfeed.dto.ItemShortResponse;
import com.gmail.geraldik.newsfeed.dto.ItemShortWithCommentNum;
import com.gmail.geraldik.newsfeed.dto.ItemUpdateRequest;
import com.gmail.geraldik.newsfeed.pesristence.tables.pojos.Item;
import com.gmail.geraldik.newsfeed.pojo.ItemWithCommentNum;
import org.springframework.stereotype.Component;

@Component
public class ItemMapper {

    /**
     * Convert entering Item to be saved to a POJO version
     */
    public Item toEntity(ItemSaveRequest itemSaveRequest) {
        return new Item()
                .setTitle(itemSaveRequest.getTitle())
                .setBody(itemSaveRequest.getBody())
                .setAuthor(itemSaveRequest.getAuthor());
    }

    /**
     * Convert entering Item to be saved to a short version
     */
    public ItemShortResponse toItemShortResponse(ItemSaveRequest itemSaveRequest, int id) {
        var shortDto = new ItemShortResponse();
        shortDto.setId(id)
                .setTitle(itemSaveRequest.getTitle())
                .setBody(cutTheBody(itemSaveRequest.getBody()))
                .setAuthor(itemSaveRequest.getAuthor());
        return shortDto;
    }

    /**
     * Convert entering Item to be updated to a short version
     */
    public ItemShortResponse toItemShortResponse(ItemUpdateRequest itemUpdateRequest) {
        return new ItemShortResponse()
                .setId(itemUpdateRequest.getId())
                .setTitle(itemUpdateRequest.getTitle())
                .setBody(cutTheBody(itemUpdateRequest.getBody()))
                .setAuthor(itemUpdateRequest.getAuthor())
                .setDisable(itemUpdateRequest.isDisable());
    }

    /**
     * Convert POJO Item with commentaries number to a short version
     */
    public ItemShortWithCommentNum toItemShortCommentNumResponse(ItemWithCommentNum item) {
        return new ItemShortWithCommentNum()
                .setId(item.getId())
                .setTitle(item.getTitle())
                .setBody(cutTheBody(item.getBody()))
                .setAuthor(item.getAuthor())
                .setCommentNum(item.getCommentNum());
    }

    /**
     * Convert entering Item to be updated to a short version
     */
    public Item toUpdateEntity(ItemUpdateRequest itemUpdateRequest) {
        return new Item()
                .setId(itemUpdateRequest.getId())
                .setTitle(itemUpdateRequest.getTitle())
                .setBody(itemUpdateRequest.getBody())
                .setAuthor(itemUpdateRequest.getAuthor())
                .setDisable(itemUpdateRequest.isDisable());
    }

    /**
     * Cutting text of item to a short version
     */
    private String cutTheBody(String body) {
        return body.length() > 50 ? body.substring(0, 50) : body;
    }
}
