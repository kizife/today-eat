package com.ssafy.cafe.model.dto;

public class Product {
    private Integer productId;
    private String name;
    private String type;
    private Integer price;
    private String img;
    private String description;
    
    public Product(Integer productId, String name, String type, Integer price, String img, String description) {
        this.productId = productId;
        this.name = name;
        this.type = type;
        this.price = price;
        this.img = img;
        this.description = description;
    }
    
    public Product(String name, String type, Integer price, String img, String description) {
        this.name = name;
        this.type = type;
        this.price = price;
        this.img = img;
        this.description = description;
    }
    public Product() {}
    
	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}
	
	 public String getDescription() {  // description getter
	        return description;
	    }

	    public void setDescription(String description) {  // description setter
	        this.description = description;
	    }

	@Override
	public String toString() {
		return "Product [id=" + productId + ", name=" + name + ", type=" + type + ", price=" + price + ", img=" + img + ", description=" + description + "]";
	}
    
    
    
    
}
