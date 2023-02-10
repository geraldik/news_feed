package com.gmail.geraldik.newsfeed.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.geraldik.newsfeed.NewsFeedApplication;
import com.gmail.geraldik.newsfeed.dto.ItemSaveRequest;
import com.gmail.geraldik.newsfeed.dto.ItemShortResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = NewsFeedApplication.class)
@AutoConfigureMockMvc
class ItemControllerTest {

    public static final String TITLE = "title";
    public static final String BODY = "body";
    public static final String AUTHOR = "author";
    private static final String ENDPOINT = "/api/v1/news";
    public static final int ID = 1;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void whenCreateItemThenOk() throws Exception {
        ItemSaveRequest itemSaveRequest = new ItemSaveRequest();
        itemSaveRequest.setTitle(TITLE)
                .setBody(BODY)
                .setAuthor(AUTHOR);
        ItemShortResponse itemShortResponse = new ItemShortResponse();
        itemShortResponse.setId(ID)
                .setTitle(TITLE)
                .setBody(BODY)
                .setAuthor(AUTHOR);
        mockMvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemSaveRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(itemShortResponse));
    }
}