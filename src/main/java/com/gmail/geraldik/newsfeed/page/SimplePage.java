package com.gmail.geraldik.newsfeed.page;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class SimplePage<T> {
    private final List<T> content;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private final Sort sort;
    private final int total;
    private final int page;
    private final int size;
}
