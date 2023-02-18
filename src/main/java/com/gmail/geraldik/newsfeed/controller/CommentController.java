package com.gmail.geraldik.newsfeed.controller;

import com.gmail.geraldik.newsfeed.dto.CommentFullResponse;
import com.gmail.geraldik.newsfeed.dto.CommentSaveRequest;
import com.gmail.geraldik.newsfeed.dto.CommentShortResponse;
import com.gmail.geraldik.newsfeed.dto.CommentUpdateRequest;
import com.gmail.geraldik.newsfeed.service.CommentService;
import com.gmail.geraldik.newsfeed.utils.UriConsts;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping(UriConsts.API + UriConsts.NEWS)
public class CommentController {

    private final CommentService service;

    @PostMapping(UriConsts.COMMENTS)
    public ResponseEntity<CommentShortResponse> createComment(
            @Valid @RequestBody CommentSaveRequest commentSaveRequest) {
        var commentShortResponse = service.save(commentSaveRequest);
        return new ResponseEntity<>(
                commentShortResponse,
                HttpStatus.OK
        );
    }

    @PutMapping(UriConsts.COMMENTS)
    public ResponseEntity<CommentShortResponse> updateComment(
            @Valid @RequestBody CommentUpdateRequest commentUpdateRequest) {
        var commentShortResponse = service.update(commentUpdateRequest);
        return new ResponseEntity<>(
                commentShortResponse,
                HttpStatus.OK
        );
    }

    @DeleteMapping(UriConsts.COMMENTS + "/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable("commentId") int commentId) {
        service.delete(commentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{itemId}" + UriConsts.COMMENTS)
    public ResponseEntity<List<CommentFullResponse>> getComments(
            @PathVariable("itemId") int itemId) {
        var comments = service.findAllForItem(itemId);
        return new ResponseEntity<>(
                comments,
                HttpStatus.OK
        );
    }

    @GetMapping(UriConsts.COMMENTS + "/{commentId}")
    public ResponseEntity<CommentFullResponse> getComment(
            @PathVariable("commentId") int commentId) {
        var comment = service.findOne(commentId);
        return new ResponseEntity<>(
                comment,
                HttpStatus.OK
        );
    }
}
