package com.gmail.geraldik.newsfeed.controller;

import com.gmail.geraldik.newsfeed.dto.ItemSaveRequest;
import com.gmail.geraldik.newsfeed.dto.ItemShortResponse;
import com.gmail.geraldik.newsfeed.dto.ItemUpdateRequest;
import com.gmail.geraldik.newsfeed.service.ItemService;
import com.gmail.geraldik.newsfeed.utils.UriConsts;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(UriConsts.API + UriConsts.NEWS)
public class ItemController {

    private final ItemService service;

    @PostMapping()
    public ResponseEntity<ItemShortResponse> createItem(@Valid @RequestBody ItemSaveRequest itemSaveRequest) {
        var itemShortResponse = service.save(itemSaveRequest);
        return new ResponseEntity<>(
                itemShortResponse,
                HttpStatus.OK
        );
    }

    @PutMapping()
    public ResponseEntity<ItemShortResponse> updateItem(@RequestBody ItemUpdateRequest itemUpdateRequest) {
        var itemShortResponse = service.update(itemUpdateRequest);
        return new ResponseEntity<>(
                itemShortResponse,
                HttpStatus.OK
        );
    }

    @GetMapping(params = {"page", "size"})
    public ResponseEntity<Page<ItemShortResponse>> getItems(@RequestParam("page") int page,
                                                            @RequestParam("size") int size) {
        var resultPage = service.findPaginated(PageRequest.of(page, size));
        return new ResponseEntity<>(
                resultPage,
                HttpStatus.OK);
    }
}
