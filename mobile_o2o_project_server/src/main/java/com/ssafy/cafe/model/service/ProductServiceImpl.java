package com.ssafy.cafe.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.ssafy.cafe.model.dao.CommentDao;
import com.ssafy.cafe.model.dao.ProductDao;
import com.ssafy.cafe.model.dto.CommentInfo;
import com.ssafy.cafe.model.dto.Product;
import com.ssafy.cafe.model.dto.ProductWithComment;

/**
 * @since 2021. 6. 23.
 */
@Service
public class ProductServiceImpl implements ProductService{
    @Autowired
    private ProductDao pDao;

    @Autowired
    private CommentDao cDao;
    
    @Override
    @Cacheable(value="getProductList")
    public List<Product> getProductList() {
        return pDao.selectAll();
    }

    @Override
    public ProductWithComment selectWithComment(Integer productId) {
    	ProductWithComment product = pDao.selectInfo(productId);

    	if(product != null) {
        	List<CommentInfo> commentInfo = cDao.selectByProduct(productId);
        	product.setComments(commentInfo);
    	}
    	
        return product;
    }
    
    @Override
    public Product selectProduct(Integer productId){
    	Product select = pDao.selectProduct(productId);
    	return select;
    	
    } 
    
    
    
}
