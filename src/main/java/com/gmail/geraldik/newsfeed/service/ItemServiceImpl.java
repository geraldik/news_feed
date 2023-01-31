package com.gmail.geraldik.newsfeed.service;

import com.gmail.geraldik.newsfeed.dto.ItemSaveRequest;
import com.gmail.geraldik.newsfeed.dto.ItemShortResponse;
import com.gmail.geraldik.newsfeed.dto.ItemUpdateRequest;
import com.gmail.geraldik.newsfeed.mapper.ItemMapper;
import com.gmail.geraldik.newsfeed.pesristence.tables.Item;
import com.gmail.geraldik.newsfeed.pojo.ItemWithCommentNum;
import com.gmail.geraldik.newsfeed.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.jooq.Field;
import org.jooq.OrderField;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static org.jooq.impl.DSL.field;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository repository;
    private final ItemMapper mapper;

    /**
     * Save Item in DB
     */
    @Override
    public ItemShortResponse save(ItemSaveRequest itemSaveRequest) {
        var item = mapper.toEntity(itemSaveRequest);
        int id = repository.insertOne(item);
        return mapper.toItemShortResponse(itemSaveRequest, id);
    }

    /**
     * Update Item in DB
     */
    @Override
    public ItemShortResponse update(ItemUpdateRequest itemUpdateRequest) {
        var item = mapper.toUpdateEntity(itemUpdateRequest);
        if (!repository.updateOne(item)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Item with id = %d not found", item.getId()));
        }
        return mapper.toItemShortResponse(itemUpdateRequest);
    }

    @Override
    public Page<ItemWithCommentNum> findPaginated(Pageable pageable) {
        OrderField<?>[] orderFields = pageAbleToOrderFields(pageable);
        var items = repository.findAllWithLimitAndOffsetAndSort(
                pageable.getPageNumber(), pageable.getPageSize(), orderFields);
        int itemsCount = repository.countAllItem();
        return new PageImpl<>(items, pageable, itemsCount);
    }

    private OrderField<?>[] pageAbleToOrderFields(Pageable pageable) {
        Sort sort = pageable.getSort();
        List<Field<?>> sortFields = sort.stream()
                .map(o -> field(o.getProperty()))
                .collect(Collectors.toList());
        OrderField<?>[] orders = new OrderField[sortFields.size()];
        Iterator<Sort.Order> iterator = sort.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            var order = iterator.next();
            String property = order.getProperty();
            OrderField<?> orderField = order.isAscending() ?
                    Item.ITEM.field(property).asc() : Item.ITEM.field(property).desc();
            orders[i++] = orderField;
        }
        return orders;
    }
}
