package com.example.shoppingmall.domain.cart.domain;

import com.example.shoppingmall.domain.common.BaseTimeEntity;
import com.example.shoppingmall.domain.item.domain.Item;
import com.example.shoppingmall.domain.item.domain.ItemStock;
import com.example.shoppingmall.domain.item.excepction.ItemException;
import com.example.shoppingmall.domain.item.type.ItemStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.example.shoppingmall.global.exception.ErrorCode.*;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PRIVATE)
@Builder(access = PRIVATE)
@Getter
@Entity
public class CartItem extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "cart_item_id")
    private Long id;

    @JoinColumn(name = "cart_id")
    @ManyToOne(fetch = LAZY)
    private Cart cart;

    @JoinColumn(name = "item_id")
    @ManyToOne(fetch = LAZY)
    private Item item;

    @JoinColumn(name = "item_stock_id")
    @ManyToOne(fetch = LAZY)
    private ItemStock itemStock;

    private int quantity;

    public static CartItem of(Cart cart, Item item, ItemStock itemStock) {
        return CartItem.builder()
                .cart(cart)
                .item(item)
                .itemStock(itemStock)
                .build();
    }

    // 장바구기 담기 (장바구니 수정기능 X)
    public void addQuantity(int quantity) {
        if (quantity < 1) quantity = 1;

        // 물품에 속하는 모든 옵션(사이즈 등) 의 재고가 없을 때 or 만료일자
        if (item.getStatus() == ItemStatus.ALL_OUT_OF_STOCK ||
                item.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new ItemException(PRODUCT_NOT_FOR_SALE);
        }

        // 현재 재고보다 많은 수량을 담을 수 없음
        if ((this.quantity + quantity) > itemStock.getStock()) {
            throw new ItemException(CART_QUANTITY_EXCEEDS_STOCK);
        }


        this.quantity += quantity;
    }

    // 장바구니 수정 (수량 변경)
    public void modifyQuantity(int quantity) {
        // 이미 담아놨던 물품이 만료일자인지, 품절처리 상태인지 확인
        if ((item.getStatus() == null) || item.getStatus().equals(ItemStatus.ALL_OUT_OF_STOCK)) {
            throw new ItemException(PRODUCT_NOT_FOR_SALE);
        }
        // 판매중이면, 넣어놓은 옵션(사이즈) 의 물품의 개별 재고를 확인
        if (quantity > itemStock.getStock()) {
            throw new ItemException(CART_QUANTITY_EXCEEDS_STOCK);
        }

        if(quantity < 1){
            throw new ItemException(CART_ITEM_QUANTITY_MIN_LIMIT);
        }

        this.quantity = quantity;
    }
}
