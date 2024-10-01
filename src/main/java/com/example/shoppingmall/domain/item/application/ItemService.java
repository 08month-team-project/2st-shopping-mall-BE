package com.example.shoppingmall.domain.item.application;

import com.example.shoppingmall.domain.item.dao.ImageRepository;
import com.example.shoppingmall.domain.item.dao.ItemRepository;
import com.example.shoppingmall.domain.item.domain.Item;
import com.example.shoppingmall.domain.item.dto.ItemDetailResponse;
import com.example.shoppingmall.domain.item.dto.ItemResponse;
import com.example.shoppingmall.domain.item.excepction.ItemException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.shoppingmall.global.exception.ErrorCode.NOT_FOUND_ITEM;
import static org.springframework.data.domain.Sort.Direction.DESC;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final ImageRepository imageRepository;

    public Page<ItemResponse> getItemMainPage(int pageNum) {
        PageRequest pageRequest = PageRequest.of(pageNum, 12, Sort.by(DESC, "created_at"));

        return itemRepository
                .findMainPage(pageRequest).map(ItemResponse::new);
    }

    public ItemDetailResponse getItemDetail(long itemId) {

        Item item = itemRepository.findItemAndStockAndSeller(itemId)
                .orElseThrow(() -> new ItemException(NOT_FOUND_ITEM));

        return new ItemDetailResponse(item, imageRepository.findAllByItemId(item.getId()));
    }
}
