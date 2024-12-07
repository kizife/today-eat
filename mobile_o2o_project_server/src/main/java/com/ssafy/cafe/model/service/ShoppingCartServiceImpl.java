package com.ssafy.cafe.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.cafe.model.dao.ShoppingCartDao;
import com.ssafy.cafe.model.dto.ShoppingCart;

/**
 * @since 2021. 6. 23.
 */
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartDao shoppingCartDao;

    @Override
    @Transactional
    public int addCartItem(ShoppingCart shoppingCart) {
        return shoppingCartDao.insert(shoppingCart);
    }

    @Override
    public ShoppingCart getCartItem(Integer cartId) {
        return shoppingCartDao.select(cartId);
    }

    @Override
    @Transactional
    public int removeCartItem(Integer cartId) {
        return shoppingCartDao.delete(cartId);
    }

    @Override
    @Transactional
    public int updateCartItem(ShoppingCart shoppingCart) {
        return shoppingCartDao.update(shoppingCart);
    }

    @Override
    public List<ShoppingCart> getCartItemsByUser(String userId) {
        return shoppingCartDao.selectByUser(userId);
    }

    @Override
    public List<ShoppingCart> getCartItemsByDosirock(Integer dosirockId) {
        return shoppingCartDao.selectByDosirock(dosirockId);
    }

    @Override
    public List<ShoppingCart> getAllCartItems() {
        return shoppingCartDao.selectAll();
    }
}
