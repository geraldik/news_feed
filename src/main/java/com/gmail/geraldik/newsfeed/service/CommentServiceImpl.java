package com.gmail.geraldik.newsfeed.service;

import com.gmail.geraldik.newsfeed.dto.CommentFullResponse;
import com.gmail.geraldik.newsfeed.dto.CommentSaveRequest;
import com.gmail.geraldik.newsfeed.dto.CommentShortResponse;
import com.gmail.geraldik.newsfeed.dto.CommentUpdateRequest;
import com.gmail.geraldik.newsfeed.mapper.CommentMapper;
import com.gmail.geraldik.newsfeed.pesristence.tables.pojos.Comment;
import com.gmail.geraldik.newsfeed.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository repository;
    private final CommentMapper mapper;

    @Override
    public CommentShortResponse save(CommentSaveRequest commentSaveRequest) {
        Comment comment = mapper.toEntity(commentSaveRequest);
        int id = repository.insertOne(comment);
        return mapper.toCommentShortResponse(commentSaveRequest, id);
    }

    @Override
    public CommentShortResponse update(CommentUpdateRequest commentUpdateRequest) {
        var comment = mapper.toUpdateEntity(commentUpdateRequest);
        if (!repository.updateOne(comment)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Comment with id = %d not found", comment.getId()));
        }
        return mapper.toCommentShortResponse(commentUpdateRequest);
    }

    @Override
    public void delete(int commentId) {
        if (!repository.deleteOne(commentId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Comment with id = %d not found", commentId));
        }
    }

    @Override
    public List<CommentFullResponse> findAllForItem(int itemId) {
        return repository.findAllForItem(itemId);
    }

    @Override
    public CommentFullResponse findOne(int commentId) {
        Optional<CommentFullResponse> fullResponseOptional =
                repository.findOne(commentId);
        if (fullResponseOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Comment with id = %d not found", commentId));
        }
        return fullResponseOptional.get();
    }
}
