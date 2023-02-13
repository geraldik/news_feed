package com.gmail.geraldik.newsfeed.controller;

import com.gmail.geraldik.newsfeed.dto.ItemSaveRequest;
import com.gmail.geraldik.newsfeed.dto.ItemShortResponse;
import com.gmail.geraldik.newsfeed.dto.ItemShortWithCommentNum;
import com.gmail.geraldik.newsfeed.dto.ItemUpdateRequest;
import com.gmail.geraldik.newsfeed.filter.ItemPageFilter;
import com.gmail.geraldik.newsfeed.page.SimplePage;
import com.gmail.geraldik.newsfeed.service.ItemService;
import com.gmail.geraldik.newsfeed.utils.UriConsts;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(UriConsts.API + UriConsts.NEWS)
public class ItemController {

    private final ItemService service;

    @PostMapping()
    public ResponseEntity<ItemShortResponse> createItem(
            @Valid @RequestBody ItemSaveRequest itemSaveRequest) {
        var itemShortResponse = service.save(itemSaveRequest);
        return new ResponseEntity<>(
                itemShortResponse,
                HttpStatus.OK
        );
    }

    @PutMapping()
    public ResponseEntity<ItemShortResponse> updateItem(
           @Valid @RequestBody ItemUpdateRequest itemUpdateRequest) {
        var itemShortResponse = service.update(itemUpdateRequest);
        return new ResponseEntity<>(
                itemShortResponse,
                HttpStatus.OK
        );
    }

    @GetMapping()
    public ResponseEntity<SimplePage<ItemShortWithCommentNum>> getItems(
            @RequestParam (value = "page", required = false, defaultValue = "0") int page,
            @RequestParam (value = "size", required = false, defaultValue = "10") int size,
            Sort sort,
            ItemPageFilter filter) {
        var resultPage = service.findPaginated(page, size, sort, filter);
        return new ResponseEntity<>(
                resultPage,
                HttpStatus.OK);
    }
}
