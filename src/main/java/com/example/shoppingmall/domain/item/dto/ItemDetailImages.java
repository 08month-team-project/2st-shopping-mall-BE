package com.example.shoppingmall.domain.item.dto;

import com.example.shoppingmall.domain.item.domain.ItemImage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@AllArgsConstructor
@Getter
public class ItemDetailImages {

    private Long itemId;

    List<ItemImageResponse> itemImageResponses;


    public static ItemDetailImages of(Long itemId, List<ItemImage> itemImages) {

        return ItemDetailImages.builder()
                .itemId(itemId)
                .itemImageResponses(itemImages.stream()
                        .map(ItemImageResponse::from).toList())
                .build();
    }
}
