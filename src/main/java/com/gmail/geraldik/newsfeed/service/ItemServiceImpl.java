package com.gmail.geraldik.newsfeed.service;

import com.gmail.geraldik.newsfeed.dto.ItemSaveRequest;
import com.gmail.geraldik.newsfeed.dto.ItemShortResponse;
import com.gmail.geraldik.newsfeed.dto.ItemShortWithCommentNum;
import com.gmail.geraldik.newsfeed.dto.ItemUpdateRequest;
import com.gmail.geraldik.newsfeed.filter.ItemPageFilter;
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

     Saves the {@code itemSaveRequest} to the database and returns a short response object that represents the saved item.
     @param itemSaveRequest the item save request object that contains the information of the item to be saved
     @return a short response object that represents the saved item (body of item contains no more than 50 chars)
     */
    @Override
    public ItemShortResponse save(ItemSaveRequest itemSaveRequest) {
        var item = mapper.toEntity(itemSaveRequest);
        int id = repository.insertOne(item);
        return mapper.toItemShortResponse(itemSaveRequest, id);
    }

    /**
     Updates an item based on the information provided in {@code itemUpdateRequest}.
     @param itemUpdateRequest the item update request object that contains the updated information
     @return the updated item in a short response format (body of item contains no more than 50 chars)
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
     Finds paginated items with filters and sort.
     @param page the page number, starting from 0
     @param size the page size, must be greater than 0
     @param sort the sort specification
     @param filter the filter specification
     @return a {@link SimplePage} of {@link ItemShortWithCommentNum} objects that match the given conditions
     */
    @Override
    public SimplePage<ItemShortWithCommentNum> findPaginated(int page, int size, Sort sort, ItemPageFilter filter) {
        int itemsCount = repository.countAllItem();
        if (itemsCount < page * size) {
            page = itemsCount / size;
        }
        OrderField<?>[] orderFields = toOrderField(sort);
        List<ItemWithCommentNum> items = repository.findAllWithLimitAndOffsetAndSortAndFilter(
                page, size, orderFields, filter);
        var shortItems = items.stream()
                .map(mapper::toItemShortCommentNumResponse)
                .toList();
        return new SimplePage<>(shortItems, sort, itemsCount, page, size);
    }

    /**

     Converts {@link Sort} object to an array of {@link OrderField} to be used in JOOQ's orderBy(OrderField[]) method.
     If the input sort is unsorted, the default order is descending by the "created" field.
     @param sort the sort criteria
     @return an array of order fields for use in JOOQ's orderBy(OrderField[]) method
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
