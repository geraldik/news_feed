package com.gmail.geraldik.newsfeed.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.geraldik.newsfeed.NewsFeedApplication;
import com.gmail.geraldik.newsfeed.dto.*;
import com.gmail.geraldik.newsfeed.page.SimplePage;
import jakarta.servlet.ServletException;
import org.jooq.DSLContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static com.gmail.geraldik.newsfeed.pesristence.Tables.ITEM;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = NewsFeedApplication.class)
@AutoConfigureMockMvc
class ItemControllerTest {

    public static final Integer ID_1 = 1;
    public static final Integer ID_2 = 2;
    public static final String TITLE_ONE = "title1";
    public static final String TITLE_TWO = "title2";
    public static final String BODY_ONE = "body1";
    public static final String BODY_TWO = "body2";
    public static final String AUTHOR_ONE = "author1";
    public static final String AUTHOR_TWO = "author2";
    private static final String ENDPOINT_NEWS = "/api/v1/news";
    private static final String ENDPOINT_COMMENTS = "/api/v1/comments";
    private static final String ENDPOINT_GET_COMMENTS_FOR_ONE = "/api/v1/news/1/comments";

    public static final ItemSaveRequest ITEM_SAVE_REQUEST_ONE = new ItemSaveRequest();
    public static final ItemSaveRequest ITEM_SAVE_REQUEST_TWO = new ItemSaveRequest();
    private static final ItemShortResponse ITEM_SHORT_RESPONSE = new ItemShortResponse();
    private static final ItemShortWithCommentNum ITEM_SHORT_WITH_COMMENT_NUM_ONE =
            new ItemShortWithCommentNum();
    private static final ItemShortWithCommentNum ITEM_SHORT_WITH_COMMENT_NUM_TWO =
            new ItemShortWithCommentNum();
    public static final ItemUpdateRequest ITEM_UPDATE_REQUEST =

            new ItemUpdateRequest(ID_1, TITLE_TWO, BODY_TWO, AUTHOR_TWO);
    private static final String COMMENTATOR = "commentator";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DSLContext dsl;

    @BeforeAll
    public static void startInit() {
        ITEM_SAVE_REQUEST_ONE
                .setTitle(TITLE_ONE)
                .setBody(BODY_ONE)
                .setAuthor(AUTHOR_ONE);
        ITEM_SAVE_REQUEST_TWO
                .setTitle(TITLE_TWO)
                .setBody(BODY_TWO)
                .setAuthor(AUTHOR_TWO);
        ITEM_SHORT_RESPONSE
                .setId(ID_1)
                .setTitle(TITLE_ONE)
                .setBody(BODY_ONE)
                .setAuthor(AUTHOR_ONE);
        ITEM_SHORT_WITH_COMMENT_NUM_ONE
                .setId(ID_1)
                .setTitle(TITLE_ONE)
                .setBody(BODY_ONE)
                .setAuthor(AUTHOR_ONE);
        ITEM_SHORT_WITH_COMMENT_NUM_TWO
                .setId(ID_2)
                .setTitle(TITLE_TWO)
                .setBody(BODY_TWO)
                .setAuthor(AUTHOR_TWO);
    }

    @BeforeEach
    public void truncateTables() {
        dsl.truncate(ITEM)
                .restartIdentity()
                .cascade()
                .execute();
    }

    @Test
    public void whenCreateItemThenOk() throws Exception {
        mockMvc.perform(post(ENDPOINT_NEWS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ITEM_SAVE_REQUEST_ONE)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(ITEM_SHORT_RESPONSE));
    }

    @Test
    public void whenCreateItemWithSameTitleThenGetException() throws Exception {
        postItem(ITEM_SAVE_REQUEST_ONE);
        assertThrows(ServletException.class, () -> mockMvc.perform(post(ENDPOINT_NEWS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ITEM_SAVE_REQUEST_ONE)))
                .andExpect(status().isBadRequest()));
    }

    @Test
    public void whenUpdateItemThenOk() throws Exception {
        ItemShortResponse itemShortResponse = new ItemShortResponse();
        itemShortResponse.setId(ID_1)
                .setTitle(TITLE_TWO)
                .setBody(BODY_TWO)
                .setAuthor(AUTHOR_TWO);
        postItem(ITEM_SAVE_REQUEST_ONE);
        mockMvc.perform(put(ENDPOINT_NEWS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ITEM_UPDATE_REQUEST)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(itemShortResponse));
    }

    @Test
    public void whenUpdateItemWithSameTitleThenGetException() throws Exception {
        postItem(ITEM_SAVE_REQUEST_ONE);
        postItem(ITEM_SAVE_REQUEST_TWO);
        assertThrows(ServletException.class, () -> mockMvc.perform(put(ENDPOINT_NEWS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ITEM_UPDATE_REQUEST)))
                .andExpect(status().isBadRequest()));
    }

    @Test
    public void whenGetWithoutSortThenDefaultSort() throws Exception {
        postItem(ITEM_SAVE_REQUEST_ONE);
        postItem(ITEM_SAVE_REQUEST_TWO);
        MvcResult result = mockMvc.perform(get(ENDPOINT_NEWS))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        SimplePage<ItemShortWithCommentNum> response =
                objectMapper.readValue(result.getResponse().getContentAsString(),
                        new TypeReference<>() {
                        });
        List<ItemShortWithCommentNum> content = response.getContent();
        assertThat(content).isEqualTo(List.of(
                ITEM_SHORT_WITH_COMMENT_NUM_TWO, ITEM_SHORT_WITH_COMMENT_NUM_ONE));
    }

    @Test
    public void whenGetSortedByTitleASCThenOk() throws Exception {
        postItem(ITEM_SAVE_REQUEST_ONE);
        postItem(ITEM_SAVE_REQUEST_TWO);
        MvcResult result = mockMvc.perform(get(ENDPOINT_NEWS + "?sort=title,ASC"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        SimplePage<ItemShortWithCommentNum> response =
                objectMapper.readValue(result.getResponse().getContentAsString(),
                        new TypeReference<>() {
                        });
        List<ItemShortWithCommentNum> content = response.getContent();
        assertThat(content).isNotEmpty();
        assertThat(content).isEqualTo(List.of(
                ITEM_SHORT_WITH_COMMENT_NUM_ONE, ITEM_SHORT_WITH_COMMENT_NUM_TWO));
    }

    @Test
    public void whenGetSortedByAuthorDESCThenOk() throws Exception {
        postItem(ITEM_SAVE_REQUEST_ONE);
        postItem(ITEM_SAVE_REQUEST_TWO);
        MvcResult result = mockMvc.perform(get(ENDPOINT_NEWS + "?sort=author,DESC"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        SimplePage<ItemShortWithCommentNum> response =
                objectMapper.readValue(result.getResponse().getContentAsString(),
                        new TypeReference<>() {
                        });
        List<ItemShortWithCommentNum> content = response.getContent();
        assertThat(content).isNotEmpty();
        assertThat(content).isEqualTo(List.of(
                ITEM_SHORT_WITH_COMMENT_NUM_TWO, ITEM_SHORT_WITH_COMMENT_NUM_ONE));
    }

    @Test
    public void whenGetSortedByCreatedASCWithPageZeroAndSizeOneThenOk() throws Exception {
        postItem(ITEM_SAVE_REQUEST_ONE);
        postItem(ITEM_SAVE_REQUEST_TWO);
        MvcResult result = mockMvc.perform(get(
                        ENDPOINT_NEWS + "?sort=author,ASC&page=0&size=1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        SimplePage<ItemShortWithCommentNum> response =
                objectMapper.readValue(result.getResponse().getContentAsString(),
                        new TypeReference<>() {
                        });
        List<ItemShortWithCommentNum> content = response.getContent();
        assertThat(content).isNotEmpty();
        assertThat(content).isEqualTo(List.of(
                ITEM_SHORT_WITH_COMMENT_NUM_ONE));
    }

    @Test
    public void whenGetFilteredByAuthorThenOk() throws Exception {
        postItem(ITEM_SAVE_REQUEST_ONE);
        postItem(ITEM_SAVE_REQUEST_TWO);
        MvcResult result = mockMvc.perform(get(
                        ENDPOINT_NEWS + "?author=" + AUTHOR_ONE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        SimplePage<ItemShortWithCommentNum> response =
                objectMapper.readValue(result.getResponse().getContentAsString(),
                        new TypeReference<>() {
                        });
        List<ItemShortWithCommentNum> content = response.getContent();
        assertThat(content).isNotEmpty();
        assertThat(content).isEqualTo(List.of(
                ITEM_SHORT_WITH_COMMENT_NUM_ONE));
    }

    @Test
    public void whenGetFilteredByAuthorThatNotExistThenGetEmptyList() throws Exception {
        postItem(ITEM_SAVE_REQUEST_ONE);
        MvcResult result = mockMvc.perform(get(
                        ENDPOINT_NEWS + "?author="))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        SimplePage<ItemShortWithCommentNum> response =
                objectMapper.readValue(result.getResponse().getContentAsString(),
                        new TypeReference<>() {
                        });
        List<ItemShortWithCommentNum> content = response.getContent();
        assertThat(content).isEmpty();
    }

    @Test
    public void whenGetFilteredByCreatedThenOk() throws Exception {
        postItem(ITEM_SAVE_REQUEST_ONE);
        postItem(ITEM_SAVE_REQUEST_TWO);
        MvcResult result = mockMvc.perform(get(
                        ENDPOINT_NEWS + "?createdFrom=" + (Instant.now().toEpochMilli() - 500L)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        SimplePage<ItemShortWithCommentNum> response =
                objectMapper.readValue(result.getResponse().getContentAsString(),
                        new TypeReference<>() {
                        });
        List<ItemShortWithCommentNum> content = response.getContent();
        assertThat(content).isNotEmpty();
        assertThat(content).isEqualTo(List.of(
                ITEM_SHORT_WITH_COMMENT_NUM_TWO,
                ITEM_SHORT_WITH_COMMENT_NUM_ONE));
    }

    @Test
    public void whenGetFilteredByCreatedThenGetEmptyList() throws Exception {
        postItem(ITEM_SAVE_REQUEST_ONE);
        MvcResult result = mockMvc.perform(get(
                        ENDPOINT_NEWS + "?createdFrom=" + Instant.now().toEpochMilli()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        SimplePage<ItemShortWithCommentNum> response =
                objectMapper.readValue(result.getResponse().getContentAsString(),
                        new TypeReference<>() {
                        });
        List<ItemShortWithCommentNum> content = response.getContent();
        assertThat(content).isEmpty();
    }

    @Test
    public void whenGetFilteredByCreatedThenGetOne() throws Exception {
        var fromTime = Instant.now().toEpochMilli();
        postItem(ITEM_SAVE_REQUEST_ONE);
        var toTime = Instant.now().toEpochMilli();
        postItem(ITEM_SAVE_REQUEST_TWO);
        MvcResult result = mockMvc.perform(get(
                        ENDPOINT_NEWS + "?createdFrom=" + fromTime
                                + "&createdTo=" + toTime))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        SimplePage<ItemShortWithCommentNum> response =
                objectMapper.readValue(result.getResponse().getContentAsString(),
                        new TypeReference<>() {
                        });
        List<ItemShortWithCommentNum> content = response.getContent();
        assertThat(content).isNotEmpty();
        assertThat(content).isEqualTo(List.of(
                ITEM_SHORT_WITH_COMMENT_NUM_ONE));
    }

    @Test
    public void whenGetFilteredByCommentNumThenOk() throws Exception {
        postItem(ITEM_SAVE_REQUEST_ONE);
        MvcResult result = mockMvc.perform(get(
                        ENDPOINT_NEWS + "?CommentNumFrom=0"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        SimplePage<ItemShortWithCommentNum> response =
                objectMapper.readValue(result.getResponse().getContentAsString(),
                        new TypeReference<>() {
                        });
        List<ItemShortWithCommentNum> content = response.getContent();
        assertThat(content).isNotEmpty();
        assertThat(content).isEqualTo(List.of(
                ITEM_SHORT_WITH_COMMENT_NUM_ONE));
    }

    @Test
    public void whenGetAllCommentsThenOk() throws Exception {
        var commentSaveRequest = new CommentSaveRequest();
        commentSaveRequest.setCommentator(COMMENTATOR)
                .setBody(BODY_ONE)
                .setItemId(ID_1);
        var commentResponse1 = new CommentFullResponse();
        commentResponse1.setId(ID_1)
                .setCommentator(COMMENTATOR)
                .setBody(BODY_ONE)
                .setItemId(ID_1);
        var commentResponse2 = new CommentFullResponse();
        commentResponse2.setId(ID_2)
                .setCommentator(COMMENTATOR)
                .setBody(BODY_ONE)
                .setItemId(ID_1);
        postItem(ITEM_SAVE_REQUEST_ONE);
        postComment(commentSaveRequest);
        postComment(commentSaveRequest);
        var result = mockMvc.perform(get(ENDPOINT_GET_COMMENTS_FOR_ONE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        List<CommentFullResponse> response =
                objectMapper.readValue(result.getResponse().getContentAsString(),
                        new TypeReference<>() {
                        });
        assertThat(response).isNotEmpty();
        assertThat(response).isEqualTo(List.of(commentResponse1, commentResponse2));
    }

    private void postComment(CommentSaveRequest comment) throws Exception {
        mockMvc.perform(post(ENDPOINT_COMMENTS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(comment)));
    }

    private void postItem(ItemSaveRequest item) throws Exception {
        mockMvc.perform(post(ENDPOINT_NEWS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(item)));
    }
}