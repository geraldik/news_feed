package com.gmail.geraldik.newsfeed.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.geraldik.newsfeed.NewsFeedApplication;
import com.gmail.geraldik.newsfeed.dto.CommentFullResponse;
import com.gmail.geraldik.newsfeed.dto.CommentSaveRequest;
import com.gmail.geraldik.newsfeed.dto.CommentShortResponse;
import com.gmail.geraldik.newsfeed.dto.CommentUpdateRequest;
import org.jooq.DSLContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.gmail.geraldik.newsfeed.pesristence.Tables.COMMENT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = NewsFeedApplication.class)
@AutoConfigureMockMvc
class CommentControllerTest {
    private static final String ENDPOINT = "/api/v1/comments";

    public static final CommentSaveRequest COMMENT_SAVE_REQUEST = new CommentSaveRequest();
    private static final String COMMENTATOR = "commentator";
    private static final String NEW_COMMENTATOR = "new commentator";
    private static final String BODY = "body";
    private static final String NEW_BODY = "new body";
    private static final Integer ITEM_ID = 1;
    private static final CommentShortResponse COMMENT_SHORT_RESPONSE = new CommentShortResponse();
    private static final Integer COMMENT_ID = 1;
    private static final CommentUpdateRequest COMMENT_UPDATE_REQUEST =
            new CommentUpdateRequest(COMMENT_ID, NEW_COMMENTATOR, NEW_BODY, ITEM_ID);
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private DSLContext dsl;

    @BeforeAll
    public static void startInit() {
        COMMENT_SAVE_REQUEST
                .setCommentator(COMMENTATOR)
                .setBody(BODY)
                .setItemId(ITEM_ID);
        COMMENT_SHORT_RESPONSE
                .setId(COMMENT_ID)
                .setCommentator(COMMENTATOR)
                .setBody(BODY)
                .setItemId(ITEM_ID);
    }

    @BeforeEach
    public void truncateTables() {
        dsl.truncate(COMMENT)
                .restartIdentity()
                .execute();
    }

    @Test
    public void whenCreateCommentThenOk() throws Exception {
        mockMvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(COMMENT_SAVE_REQUEST)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(COMMENT_SHORT_RESPONSE));
    }

    @Test
    public void whenCreateSameCommentThenOk() throws Exception {
        CommentShortResponse response = new CommentShortResponse();
        response.setId(COMMENT_ID + 1)
                .setCommentator(COMMENTATOR)
                .setBody(BODY)
                .setItemId(ITEM_ID);
        postComment(COMMENT_SAVE_REQUEST);
        mockMvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(COMMENT_SAVE_REQUEST)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(response));
    }

    @Test
    public void whenCreateCommentThenValidateViolation() throws Exception {
        CommentSaveRequest invalidRequest = new CommentSaveRequest();
        mockMvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenDeleteCommentThenOk() throws Exception {
        postComment(COMMENT_SAVE_REQUEST);
        mockMvc.perform(delete(ENDPOINT + "/" + COMMENT_ID))
                .andExpect(status().isOk());
    }

    @Test
    public void whenDeleteCommentThenNotFound() throws Exception {
        mockMvc.perform(delete(ENDPOINT + "/" + COMMENT_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenUpdateItemThenOk() throws Exception {
        CommentShortResponse shortResponse = new CommentShortResponse();
        shortResponse.setId(COMMENT_ID)
                .setCommentator(NEW_COMMENTATOR)
                .setBody(NEW_BODY)
                .setItemId(ITEM_ID);
        postComment(COMMENT_SAVE_REQUEST);
        mockMvc.perform(put(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(COMMENT_UPDATE_REQUEST)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(shortResponse));
    }

    @Test
    public void whenUpdateCommentThenNotFound() throws Exception {
        mockMvc.perform(put(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(COMMENT_UPDATE_REQUEST)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenGetCommentThenOk() throws Exception {
        postComment(COMMENT_SAVE_REQUEST);
        CommentFullResponse response = new CommentFullResponse();
        response.setId(COMMENT_ID)
                .setCommentator(COMMENTATOR)
                .setBody(BODY)
                .setItemId(ITEM_ID);
        mockMvc.perform(get(ENDPOINT + "/" + COMMENT_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(response));
    }

    @Test
    public void whenGetCommentThenNotFound() throws Exception {
        mockMvc.perform(get(ENDPOINT + "/" + COMMENT_ID))
                .andExpect(status().isNotFound());
    }

    private void postComment(CommentSaveRequest comment) throws Exception {
        mockMvc.perform(post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(comment)));
    }
}