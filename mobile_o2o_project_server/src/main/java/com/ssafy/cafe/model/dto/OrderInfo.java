package com.ssafy.cafe.model.dto;

import java.util.Date;
import java.util.List;

public class OrderInfo {
	private Integer orderId;
	private String userId;
	private Date orderTime;
	private String address;
	private Character completed;
	private Date deliveryTime;

	private List<OrderDetailInfo> details;

	public OrderInfo(Integer orderId, String userId, Date orderTime, String address, Character completed,
			Date deliveryTime) {
		this.orderId = orderId;
		this.userId = userId;
		this.orderTime = orderTime;
		this.address = address;
		this.completed = completed;
		this.deliveryTime = deliveryTime;
	}

	public OrderInfo(String userId, Date orderTime, String address, Character completed) {
		this.userId = userId;
		this.orderTime = orderTime;
		this.address = address;
		this.completed = completed;
	}

	public OrderInfo() {
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Date getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Character getCompleted() {
		return completed;
	}

	public void setCompleted(Character completed) {
		this.completed = completed;
	}
	
	public Date getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Date deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

	public List<OrderDetailInfo> getDetails() {
		return details;
	}

	public void setDetails(List<OrderDetailInfo> details) {
		this.details = details;
	}
	
	 @Override
	    public String toString() {
	        return "OrderInfo{" +
	                "orderId=" + orderId +
	                ", userId='" + userId + '\'' +
	                ", orderTime=" + orderTime +
	                ", address='" + address + '\'' +
	                ", completed=" + completed +
	                ", deliveryTime=" + deliveryTime +
	                ", details=" + details +
	                '}';
	    }

}
