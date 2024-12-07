package com.ssafy.cafe.model.dto;

import java.util.Date;
import java.util.List;

public class Order {
	  private Integer orderId; 
	    private String userId;
	    private Date orderTime;
	    private String address;
	    private Character completed;
	    private Date deliveryTime;
    
    private List<OrderDetail> details ;
    private List<OrderDetailInfo> detailInfos;
    
    public Order(Integer orderId, String userId, Date orderTime, String address, Character completed, Date deliveryTime) {
        this.orderId = orderId;
        this.userId = userId;
        this.orderTime = orderTime;
        this.address = address;
        this.completed = completed;
        this.deliveryTime = deliveryTime;
    }

    public Order(String userId, Date orderTime, String address, Character completed, Date deliveryTime) {
        this.userId = userId;
        this.orderTime = orderTime;
        this.address = address;
        this.completed = completed;
        this.deliveryTime = deliveryTime;
    }
    
    public Order() {}

        
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

	public List<OrderDetail> getDetails() {
		return details;
	}

	public void setDetails(List<OrderDetail> details) {
		this.details = details;
	}
	
	 public List<OrderDetailInfo> getDetailInfos() {
	        return detailInfos;
	    }

	    public void setDetailInfos(List<OrderDetailInfo> detailInfos) {
	        this.detailInfos = detailInfos;
	    }

	@Override
    public String toString() {
        return "Order [orderId=" + orderId + ", userId=" + userId + ", orderTime=" + orderTime + ", address=" + address
                + ", completed=" + completed + ", deliveryTime=" + deliveryTime + ", details=" + details + "]";
    }
    
}
