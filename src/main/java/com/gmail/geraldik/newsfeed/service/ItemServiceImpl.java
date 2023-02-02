package com.gmail.geraldik.newsfeed.service;

import com.gmail.geraldik.newsfeed.dto.ItemSaveRequest;
import com.gmail.geraldik.newsfeed.dto.ItemShortResponse;
import com.gmail.geraldik.newsfeed.dto.ItemShortWithCommentNum;
import com.gmail.geraldik.newsfeed.dto.ItemUpdateRequest;
import com.gmail.geraldik.newsfeed.mapper.ItemMapper;
import com.gmail.geraldik.newsfeed.page.SimplePage;
import com.gmail.geraldik.newsfeed.pesristence.tables.Item;
import com.gmail.geraldik.newsfeed.pojo.ItemWithCommentNum;
import com.gmail.geraldik.newsfeed.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.jooq.Field;
import org.jooq.OrderField;
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

    /**
     * Find Item in DB with pagination and sorting
     */
    @Override
    public SimplePage<ItemShortWithCommentNum> findPaginated(int page, int size, Sort sort) {
        int itemsCount = repository.countAllItem();
        if (itemsCount < page * size) {
            page = itemsCount / size;
        }
        OrderField<?>[] orderFields = toOrderField(sort);
        List<ItemWithCommentNum> items = repository.findAllWithLimitAndOffsetAndSort(
                page, size, orderFields);
        var shortItems = items.stream()
                .map(mapper::toItemShortCommentNumResponse)
                .toList();
        return new SimplePage<>(shortItems, sort, itemsCount, page, size);
    }

    /**
     * Create OrderField[] from incoming Sort object.
     * Returns default sorting created.desc
     * when there is now parameters of sorting.
     */
    private OrderField<?>[] toOrderField(Sort sort) {
        if (sort.isUnsorted()) {
            return new OrderField[]{Item.ITEM.field("created").desc()};
        }
        List<Field<?>> sortFields = sort.stream()
                .map(o -> field(o.getProperty()))
                .collect(Collectors.toList());
        OrderField<?>[] orders = new OrderField[sortFields.size()];
        Iterator<Sort.Order> iterator = sort.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            var order = iterator.next();
            String property = order.getProperty();
            OrderField<?> orderField = order.isAscending()
                    ? Item.ITEM.field(property).asc()
                    : Item.ITEM.field(property).desc();
            orders[i++] = orderField;
        }
        return orders;
    }
}
