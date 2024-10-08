package com.example.shoppingmall.domain.item.dto;

import com.example.shoppingmall.domain.item.domain.Item;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Builder
@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ItemDetailResponse {

    private long itemId;
    private String itemName;
    private int itemPrice;
    private String description;
    private long hits;

    private long sellerId;
    private String sellerNickname;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime expiredAt;

    private List<ItemSizeStockResponse> sizeStockList;

    public ItemDetailResponse(Item item) {

        this.itemId = item.getId();
        this.itemName = item.getName();
        this.itemPrice = item.getPrice();
        this.description = item.getDescription();
        this.hits = item.getHitCount();
        this.sellerId = item.getUser().getId();
        this.sellerNickname = item.getUser().getNickname();
        this.createdAt = item.getCreatedAt();
        this.expiredAt = item.getExpiredAt();
        this.sizeStockList = item.getStocks().stream()
                .map(ItemSizeStockResponse::from).toList();
    }
}
