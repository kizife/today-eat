package com.ssafy.cafe.model.dto;

public class User {
    private String id;
    private String name;
    private String pass;
    private String phone;  // 추가된 필드
    private String address; // 추가된 필드

    // 생성자 수정: stamp 관련 필드 삭제
    public User(String id, String name, String pass, String phone, String address) {
        this.id = id;
        this.name = name;
        this.pass = pass;
        this.phone = phone;
        this.address = address;
    }

    public User() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", name=" + name + ", pass=" + pass + ", phone=" + phone + ", address=" + address + "]";
    }
}
