package com.ssafy.cafe.model.service;

import java.util.List;
import com.ssafy.cafe.model.dto.ShoppingCart;

public interface ShoppingCartService {

    /**
     * 장바구니에 항목을 추가한다.
     * @param shoppingCart
     * @return 추가된 레코드 수
     */
    int addCartItem(ShoppingCart shoppingCart);

    /**
     * 장바구니 항목을 수정한다.
     * @param shoppingCart
     * @return 수정된 레코드 수
     */
    int updateCartItem(ShoppingCart shoppingCart);

    /**
     * 장바구니 항목을 삭제한다.
     * @param cartId
     * @return 삭제된 레코드 수
     */
    int removeCartItem(Integer cartId);

    /**
     * 장바구니 항목을 조회한다.
     * @param cartId
     * @return 해당 장바구니 항목
     */
    ShoppingCart getCartItem(Integer cartId);

    /**
     * 특정 사용자에 대한 장바구니 항목 목록을 반환한다.
     * @param userId
     * @return 사용자 장바구니 항목 목록
     */
    List<ShoppingCart> getCartItemsByUser(String userId);

    /**
     * 특정 도시락에 대한 장바구니 항목 목록을 반환한다.
     * @param dosirockId
     * @return 도시락이 담긴 장바구니 항목 목록
     */
    List<ShoppingCart> getCartItemsByDosirock(Integer dosirockId);

    /**
     * 장바구니 항목 전체 목록을 반환한다.
     * @return 모든 장바구니 항목 목록
     */
    List<ShoppingCart> getAllCartItems();
}
