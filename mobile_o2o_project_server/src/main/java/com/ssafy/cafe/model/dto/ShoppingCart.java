package com.ssafy.cafe.model.dto;

public class ShoppingCart {
    private Integer cartId;        // cart_id 컬럼에 해당
    private String userId;         // user_id 컬럼에 해당 (CHAR(15) 타입)
    private Integer dosirockId;    // dosirock_id 컬럼에 해당
    private Integer quantity;      // quantity 컬럼에 해당

    // 기본 생성자
    public ShoppingCart() {}

    // 파라미터가 있는 생성자
    public ShoppingCart(Integer cartId, String userId, Integer dosirockId, Integer quantity) {
        this.cartId = cartId;
        this.userId = userId;
        this.dosirockId = dosirockId;
        this.quantity = quantity;
    }

    // Getter와 Setter 메서드

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getDosirockId() {
        return dosirockId;
    }

    public void setDosirockId(Integer dosirockId) {
        this.dosirockId = dosirockId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    // toString 메서드 (디버깅이나 로깅용)
    @Override
    public String toString() {
        return "ShoppingCart{" +
                "cartId=" + cartId +
                ", userId='" + userId + '\'' +
                ", dosirockId=" + dosirockId +
                ", quantity=" + quantity +
                '}';
    }
}
