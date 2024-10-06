package com.example.shoppingmall.domain.item.domain;

import com.example.shoppingmall.domain.common.BaseTimeEntity;
import com.example.shoppingmall.domain.item.type.ItemStatus;
import com.example.shoppingmall.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Item extends BaseTimeEntity {


    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "item_id")
    private Long id;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = LAZY)
    private User user;

    @Column(name = "item_name")
    private String name;

    @Column(name = "item_price", nullable = false)
    private Integer price;

    @Builder.Default
    @OneToMany(mappedBy = "item", cascade = CascadeType.PERSIST)
    private List<ItemImage> images = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "item", cascade = CascadeType.PERSIST)
    private List<ItemStock> stocks = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "item", cascade = CascadeType.PERSIST)
    private List<CategoryItem> categoryItems = new ArrayList<>();

    @Column(name = "thumbnail_url", columnDefinition = "TEXT")
    private String thumbnailUrl;

    @Column(nullable = false, name = "expired_at")
    private LocalDateTime expiredAt;


    // TODO 조회 수 구현하게 될 때 생각해볼 예정
    @Column(nullable = false, name = "hit_count")
    private Long hitCount;

    @Column(nullable = false)
    private String description;

    @Enumerated(STRING)
    @Column(nullable = false, name = "item_status")
    private ItemStatus status;

    public void addItemStock(ClothingSize size, int stock) {

        // 물품에 이미 등록돼있는 사이즈옵션이라면 재고수량을 추가
        for (ItemStock itemStock : stocks) {
            if (itemStock != null && itemStock.getClothingSize().getId().equals(size.getId())) {
                itemStock.addStock(stock);
                return;
            }
        }
        // 등록된 적 없는 사이즈옵션이라면 새로 추가
        stocks.add(new ItemStock(this, size, stock));
    }

    public void addImage(String imageUrl) {
        images.add(new ItemImage(this, imageUrl));
    }

    public void addCategory(Category category){
        categoryItems.add(new CategoryItem(category, this));
    }

    @PrePersist
    public void hitCountAndItemStatus() {
        this.hitCount = 0L;
        this.status = ItemStatus.IN_STOCK;
    }

}
