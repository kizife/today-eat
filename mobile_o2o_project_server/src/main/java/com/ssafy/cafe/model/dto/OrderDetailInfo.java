package com.ssafy.cafe.model.dto;

public class OrderDetailInfo {
	private int id;
    private int orderId;
    private int dosirockId;
    private int quantity;
    private String mainDish;      // combination 테이블에서 가져오는 데이터
    private String sideDish1;
    private String sideDish2;
    private String soup;
    private int dosirackPrice;   
   
    private int totalPrice;
    
    public OrderDetailInfo(Integer id, Integer orderId, Integer dosirockId, Integer quantity) {
        super();
        this.id = id;
        this.orderId = orderId;
        this.dosirockId = dosirockId;
        this.quantity = quantity;
    }

    public OrderDetailInfo(Integer dosirockId, Integer quantity) {
        this.dosirockId = dosirockId;
        this.quantity = quantity;
    }

    public OrderDetailInfo(Integer orderId, Integer dosirockId, Integer quantity) {
        this.orderId = orderId;
        this.dosirockId = dosirockId;
        this.quantity = quantity;
    }
    
    public OrderDetailInfo() {}

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

	public String getMainDish() {
        return mainDish;
    }

    public void setMainDish(String mainDish) {
        this.mainDish = mainDish;
    }

    public String getSideDish1() {
        return sideDish1;
    }

    public void setSideDish1(String sideDish1) {
        this.sideDish1 = sideDish1;
    }

    public String getSideDish2() {
        return sideDish2;
    }

    public void setSideDish2(String sideDish2) {
        this.sideDish2 = sideDish2;
    }

    public String getSoup() {
        return soup;
    }

    public void setSoup(String soup) {
        this.soup = soup;
    }

    public int getDosirackPrice() {
        return dosirackPrice;
    }

    public void setDosirackPrice(int dosirackPrice) {
        this.dosirackPrice = dosirackPrice;
    }
    
    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }
	


    
}
