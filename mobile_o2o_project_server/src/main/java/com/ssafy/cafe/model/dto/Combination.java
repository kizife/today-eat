package com.ssafy.cafe.model.dto;

public class Combination {
    private Integer dosirockId;   // 도시락 ID
    private String userId;        // 사용자 ID
    private Integer main;         // 주요 메뉴 ID
    private Integer sideDish1;    // 사이드 메뉴 1 ID
    private Integer sideDish2;    // 사이드 메뉴 2 ID
    private Integer soup;         // 국 메뉴 ID
    private Integer dosirackPrice; // 도시락 가격
    
    // 기본 생성자
    public Combination() {}

    // 전체 필드를 받는 생성자
    public Combination(String userId, Integer main, Integer sideDish1, Integer sideDish2, Integer soup, Integer dosirackPrice) {
        this.userId = userId;
        this.main = main;
        this.sideDish1 = sideDish1;
        this.sideDish2 = sideDish2;
        this.soup = soup;
        this.dosirackPrice = dosirackPrice;
    }

    // getter, setter 메서드들
    public Integer getDosirockId() {
        return dosirockId;
    }

    public void setDosirockId(Integer dosirockId) {
        this.dosirockId = dosirockId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getMain() {
        return main;
    }

    public void setMain(Integer main) {
        this.main = main;
    }

    public Integer getSideDish1() {
        return sideDish1;
    }

    public void setSideDish1(Integer sideDish1) {
        this.sideDish1 = sideDish1;
    }

    public Integer getSideDish2() {
        return sideDish2;
    }

    public void setSideDish2(Integer sideDish2) {
        this.sideDish2 = sideDish2;
    }

    public Integer getSoup() {
        return soup;
    }

    public void setSoup(Integer soup) {
        this.soup = soup;
    }

    public Integer getDosirackPrice() {
        return dosirackPrice;
    }

    public void setDosirackPrice(Integer dosirackPrice) {
        this.dosirackPrice = dosirackPrice;
    }

    @Override
    public String toString() {
        return "Combination [dosirockId=" + dosirockId + ", userId=" + userId + ", main=" + main + ", sideDish1=" + sideDish1
                + ", sideDish2=" + sideDish2 + ", soup=" + soup + ", dosirackPrice=" + dosirackPrice + "]";
    }
}

