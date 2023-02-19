package com.gmail.geraldik.newsfeed.mapper;

import com.gmail.geraldik.newsfeed.dto.CommentSaveRequest;
import com.gmail.geraldik.newsfeed.dto.CommentShortResponse;
import com.gmail.geraldik.newsfeed.dto.CommentUpdateRequest;
import com.gmail.geraldik.newsfeed.pesristence.tables.pojos.Comment;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {

    private static final int LENGTH_SHORT_COMMENT = 50;

    /**
     * Convert entering comment to be saved to a POJO version
     */
    public Comment toEntity(CommentSaveRequest commentSaveRequest) {
        return new Comment()
                .setCommentator(commentSaveRequest.getCommentator())
                .setBody(commentSaveRequest.getBody())
                .setItemId(commentSaveRequest.getItemId());
    }

    /**
     * Convert entering comment to be saved to a short version
     */
    public CommentShortResponse toCommentShortResponse(
            CommentSaveRequest commentSaveRequest, int id) {
        var shortDto = new CommentShortResponse();
        shortDto.setId(id)
                .setCommentator(commentSaveRequest.getCommentator())
                .setBody(cutTheBody(commentSaveRequest.getBody()))
                .setItemId(commentSaveRequest.getItemId());
        return shortDto;
    }

    /**
     * Cutting text of comment to a short version
     */
    private String cutTheBody(String body) {
        return body.length() > LENGTH_SHORT_COMMENT
                ? body.substring(0, LENGTH_SHORT_COMMENT) : body;
    }

    /**
     * Convert entering comment to be updated to a short version
     */
    public Comment toUpdateEntity(CommentUpdateRequest commentUpdateRequest) {
        return new Comment()
                .setId(commentUpdateRequest.getId())
                .setCommentator(commentUpdateRequest.getCommentator())
                .setBody(commentUpdateRequest.getBody());
    }

    /**
     * Convert entering comment to be updated to a short version
     */
    public CommentShortResponse toCommentShortResponse(CommentUpdateRequest commentUpdateRequest) {
        return new CommentShortResponse()
                .setId(commentUpdateRequest.getId())
                .setCommentator(commentUpdateRequest.getCommentator())
                .setBody(cutTheBody(commentUpdateRequest.getBody()))
                .setItemId(commentUpdateRequest.getItemId());
    }
}
