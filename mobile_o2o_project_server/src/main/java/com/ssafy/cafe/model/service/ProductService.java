package com.ssafy.cafe.model.service;

import java.util.List;

import com.ssafy.cafe.model.dto.Product;
import com.ssafy.cafe.model.dto.ProductWithComment;

public interface ProductService {
    /**
     * 모든 상품 정보를 반환한다.
     * 
     * @return
     */
    List<Product> getProductList();
    
    /**
     * ID에 해당하는 상품의 comment 갯수, 평점평균, 전체 판매량 정보를 함께 반환
     * 
     * @param productId
     * @return
     */
    ProductWithComment selectWithComment(Integer productId);
    
    Product selectProduct(Integer productId);
}
