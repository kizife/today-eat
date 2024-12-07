package com.ssafy.cafe.model.dto;

public class OrderDetail {
	private Integer id; // detail_id
    private Integer orderId; // order_id
    private Integer dosirockId; // dosirock_id
    private Integer quantity; // quantity
    private Integer totalPrice; // totalprice

    // combination에서 가져오는 데이터
    private String mainDish;
    private String sideDish1;
    private String sideDish2;
    private String soup;
    private Integer dosirackPrice;
    private String img;
    
    
    public OrderDetail(Integer id, Integer orderId, Integer dosirockId, Integer quantity, Integer totalPrice) {
        this.id = id;
        this.orderId = orderId;
        this.dosirockId = dosirockId;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

	public OrderDetail(Integer orderId, Integer dosirockId, Integer quantity, Integer totalPrice) {
		this.orderId = orderId;
		this.dosirockId = dosirockId;
		this.quantity = quantity;
		this.totalPrice = totalPrice;
	}

	public OrderDetail() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
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

	public Integer getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Integer totalPrice) {
		this.totalPrice = totalPrice;
	}

	@Override
	public String toString() {
		return "OrderDetail [id=" + id + ", orderId=" + orderId + ", dosirockId=" + dosirockId + ", quantity="
				+ quantity + ", totalPrice=" + totalPrice + "]";
	}

}
