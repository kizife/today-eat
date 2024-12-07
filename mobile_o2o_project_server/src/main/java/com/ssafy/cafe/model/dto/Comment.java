package com.ssafy.cafe.model.dto;

public class Comment {
    private Integer commentId;
    private String userId;
    private Integer productId;
    private Double rating;
    private String comment;
    
	public Comment(Integer commentId, String userId, Integer productId, Double rating, String comment) {
		super();
		this.commentId = commentId;
		this.userId = userId;
		this.productId = productId;
		this.rating = rating;
		this.comment = comment;
	}
	
    public Comment( String userId, Integer productId, Double rating, String comment) {
        this.userId = userId;
        this.productId = productId;
        this.rating = rating;
        this.comment = comment;
    }
    
    public Comment() {
    	
    }

	public Integer getCommentId() {
		return commentId;
	}

	public void setCommentId(Integer commentId) {
		this.commentId = commentId;
	}

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

	@Override
	public String toString() {
		return "Comment [CommentId=" + commentId + ", userId=" + userId + ", productId=" + productId + ", rating=" + rating
				+ ", comment=" + comment + "]";
	}
    
    
    
}