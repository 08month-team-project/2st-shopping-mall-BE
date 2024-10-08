package com.example.shoppingmall.domain.item.api;

import com.example.shoppingmall.domain.item.application.ItemService;
import com.example.shoppingmall.domain.item.application.S3Service;
import com.example.shoppingmall.domain.item.dto.*;
import com.example.shoppingmall.domain.item.type.ItemStatus;
import com.example.shoppingmall.domain.item.type.SortCondition;
import com.example.shoppingmall.domain.item.type.StatusCondition;
import com.example.shoppingmall.global.security.detail.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = {"http://localhost:3000"},
        allowCredentials = "true",maxAge = 3600,
        methods = {RequestMethod.GET,RequestMethod.POST,RequestMethod.DELETE,RequestMethod.PATCH,RequestMethod.PUT,RequestMethod.OPTIONS},
        exposedHeaders = {"Authorization","Content-Type"})
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;
    private final S3Service s3Service;


    @GetMapping("/{item_id}")
    public ResponseEntity<ItemDetailResponse> getItemDetail(
            @PathVariable("item_id") long itemId) {

        return ResponseEntity.ok(itemService.getItemDetail(itemId));
    }

    @GetMapping("/{item_id}/images")
    public ResponseEntity<ItemDetailImages> getItemImages(
            @PathVariable("item_id") long itemId) {

        return ResponseEntity.ok(itemService.getItemImages(itemId));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ItemResponse>> searchItems(
            @RequestParam(name = "category_id", required = false) Long categoryId,
            @RequestParam(name = "item_name", required = false) String itemName,
            @RequestParam(name = "status_condition", required = false) StatusCondition statusCondition,
            @RequestParam(name = "sort_condition", required = false) SortCondition sortCondition,
            @RequestParam(name = "page_number", required = false) Integer pageNumber
    ) {

        return ResponseEntity.ok(itemService.searchItems(
                categoryId, itemName, statusCondition, sortCondition, pageNumber));
    }

    // 이미지 업로드
    @PostMapping("/images/upload")
    public ResponseEntity<List<String>> getResignedUrls(@RequestParam("images") List<MultipartFile> multipartFiles) {
        List<String> urls = s3Service.createUrlsForUpload(multipartFiles);
        return ResponseEntity.ok(urls);
    }

    // 카테고리 조회 -> 추후 캐싱 도전
    @GetMapping("/categories")
    public ResponseEntity<CategoryResponse> getCategoryList() {
        CategoryResponse response = itemService.getCategoryList();
        return ResponseEntity.ok(response);
    }

    // size 조회 -> 추후 캐싱 도전
    @GetMapping("/size")
    public ResponseEntity<SizeResponse> getSizeList() {
        SizeResponse response = itemService.getSizeList();
        return ResponseEntity.ok(response);
    }

    // 상품등록
    @PostMapping("/seller/register")
    public ResponseEntity<SellerResponse> itemResister(@Valid @RequestBody RegisterRequest request,
                                                       @AuthenticationPrincipal CustomUserDetails userDetails) {
        SellerResponse response = itemService.itemRegister(request, userDetails);
        return ResponseEntity.ok(response);
    }

    // 물품 리스트
    @GetMapping("/status")
    public ResponseEntity<Page<SellerItemResponse>> searchItems(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                @RequestParam ItemStatus status,
                                                                @PageableDefault(size = 3) Pageable pageable,
                                                                @RequestParam(defaultValue = "1") Integer page) {
        Page<SellerItemResponse> result = itemService.searchPageComplex(userDetails,status, pageable,page);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{item-id}/stock")
    public ResponseEntity<SellerResponse> updateItemStock(@PathVariable(name = "item-id") Long id,
                                                          @RequestBody UpdateItemRequest request) {
        SellerResponse response = itemService.updateItemStock(id, request);
        return ResponseEntity.ok(response);
    }
}
