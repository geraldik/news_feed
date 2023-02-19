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

@Controller
@RequiredArgsConstructor
@RequestMapping(UriConsts.API + UriConsts.COMMENTS)
public class CommentController {

    private final CommentService service;

    @PostMapping()
    public ResponseEntity<CommentShortResponse> createComment(
            @Valid @RequestBody CommentSaveRequest commentSaveRequest) {
        var commentShortResponse = service.save(commentSaveRequest);
        return new ResponseEntity<>(
                commentShortResponse,
                HttpStatus.OK
        );
    }

    @PutMapping()
    public ResponseEntity<CommentShortResponse> updateComment(
            @Valid @RequestBody CommentUpdateRequest commentUpdateRequest) {
        var commentShortResponse = service.update(commentUpdateRequest);
        return new ResponseEntity<>(
                commentShortResponse,
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable("commentId") int commentId) {
        service.delete(commentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<CommentFullResponse> getComment(
            @PathVariable("commentId") int commentId) {
        var comment = service.findOne(commentId);
        return new ResponseEntity<>(
                comment,
                HttpStatus.OK
        );
    }
}
