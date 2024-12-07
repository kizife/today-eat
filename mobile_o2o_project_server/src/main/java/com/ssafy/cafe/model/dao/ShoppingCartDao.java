package com.ssafy.cafe.model.dao;

import java.util.List;
import com.ssafy.cafe.model.dto.ShoppingCart;

public interface ShoppingCartDao {

    /**
     * 장바구니에 항목 추가
     * @param shoppingCart
     * @return 추가된 레코드 수
     */
    int insert(ShoppingCart shoppingCart);

    /**
     * 장바구니 항목 수정
     * @param shoppingCart
     * @return 수정된 레코드 수
     */
    int update(ShoppingCart shoppingCart);

    /**
     * 장바구니 항목 삭제
     * @param cartId
     * @return 삭제된 레코드 수
     */
    int delete(Integer cartId);

    /**
     * 장바구니 단건 조회
     * @param cartId
     * @return 해당 장바구니 항목
     */
    ShoppingCart select(Integer cartId);

    /**
     * 특정 사용자에 대한 장바구니 조회
     * @param userId
     * @return 해당 사용자의 장바구니 항목 리스트
     */
    List<ShoppingCart> selectByUser(String userId);

    /**
     * 특정 도시락에 대한 장바구니 조회
     * @param dosirockId
     * @return 해당 도시락을 담은 장바구니 항목 리스트
     */
    List<ShoppingCart> selectByDosirock(Integer dosirockId);

    /**
     * 장바구니 항목 전체 조회
     * @return 모든 장바구니 항목 리스트
     */
    List<ShoppingCart> selectAll();
}

