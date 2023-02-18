package com.gmail.geraldik.newsfeed.service;

import com.gmail.geraldik.newsfeed.dto.CommentFullResponse;
import com.gmail.geraldik.newsfeed.dto.CommentSaveRequest;
import com.gmail.geraldik.newsfeed.dto.CommentShortResponse;
import com.gmail.geraldik.newsfeed.dto.CommentUpdateRequest;

import java.util.List;

public interface CommentService {

    CommentShortResponse save(CommentSaveRequest itemSaveRequest);

    CommentShortResponse update(CommentUpdateRequest itemUpdateRequest);

    void delete(int commentId);

    List<CommentFullResponse> findAllForItem(int itemId);

    CommentFullResponse findOne(int commentId);
}
