package com.gmail.geraldik.newsfeed;

import com.gmail.geraldik.newsfeed.dto.ItemSaveRequest;
import com.gmail.geraldik.newsfeed.dto.ItemShortResponse;
import com.gmail.geraldik.newsfeed.service.ItemService;
import com.gmail.geraldik.newsfeed.utils.UriConsts;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

    @GetMapping()
    public ResponseEntity<Void> test(){
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
