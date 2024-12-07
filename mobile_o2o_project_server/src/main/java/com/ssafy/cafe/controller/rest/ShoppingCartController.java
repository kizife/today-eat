package com.ssafy.cafe.controller.rest;

import com.ssafy.cafe.model.dto.ShoppingCart;
import com.ssafy.cafe.model.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    @Autowired
    public ShoppingCartController(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    // 장바구니 항목 추가
    @PostMapping
    public String addCartItem(@RequestBody ShoppingCart shoppingCart) {
        int rowsAffected = shoppingCartService.addCartItem(shoppingCart);
        if (rowsAffected > 0) {
            return "장바구니에 항목이 추가되었습니다.";
        } else {
            return "장바구니에 항목 추가 실패.";
        }
    }

    // 장바구니 항목 수정
    @PutMapping("/{cartId}")
    public String updateCartItem(@PathVariable Integer cartId, @RequestBody ShoppingCart shoppingCart) {
        shoppingCart.setCartId(cartId);
        int rowsAffected = shoppingCartService.updateCartItem(shoppingCart);
        if (rowsAffected > 0) {
            return "장바구니 항목이 수정되었습니다.";
        } else {
            return "장바구니 항목 수정 실패.";
        }
    }

    // 장바구니 항목 삭제
    @DeleteMapping("/{cartId}")
    public String removeCartItem(@PathVariable Integer cartId) {
        int rowsAffected = shoppingCartService.removeCartItem(cartId);
        if (rowsAffected > 0) {
            return "장바구니 항목이 삭제되었습니다.";
        } else {
            return "장바구니 항목 삭제 실패.";
        }
    }

    // 장바구니 항목 조회 (ID로)
    @GetMapping("/{cartId}")
    public ShoppingCart getCartItem(@PathVariable Integer cartId) {
        return shoppingCartService.getCartItem(cartId);
    }

    // 특정 사용자 장바구니 항목 조회
    @GetMapping("/user/{userId}")
    public List<ShoppingCart> getCartItemsByUser(@PathVariable String userId) {
        return shoppingCartService.getCartItemsByUser(userId);
    }

    // 특정 도시락 아이템 장바구니 조회
    @GetMapping("/dosirock/{dosirockId}")
    public List<ShoppingCart> getCartItemsByDosirock(@PathVariable Integer dosirockId) {
        return shoppingCartService.getCartItemsByDosirock(dosirockId);
    }

    // 모든 장바구니 항목 조회
    @GetMapping("/all")
    public List<ShoppingCart> getAllCartItems() {
        return shoppingCartService.getAllCartItems();
    }
}

