package com.ssafy.cafe.model.dto;

public class CommentInfo {
    private Integer commentId;
    private String userId;
    private Integer productId;
    private Double rating;
    private String comment;

    private String userName;

    // 기존 생성자들 그대로 유지
    public CommentInfo(Integer commentId, String userId, Integer productId, Double rating, String comment) {
        super();
        this.commentId = commentId;
        this.userId = userId;
        this.productId = productId;
        this.rating = rating;
        this.comment = comment;
    }

    public CommentInfo(String userId, Integer productId, Double rating, String comment) {
        this.userId = userId;
        this.productId = productId;
        this.rating = rating;
        this.comment = comment;
    }

    public CommentInfo() {
    }

    // getId -> getCommentId로 변경
    public Integer getCommentId() {
        return commentId;
    }

    // setId -> setCommentId로 변경
    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    // 기존 getter/setter들 그대로 유지
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "Comment [commentId=" + commentId + ", userId=" + userId + ", productId=" + productId + ", rating=" + rating
                + ", comment=" + comment + "]";
    }
}
