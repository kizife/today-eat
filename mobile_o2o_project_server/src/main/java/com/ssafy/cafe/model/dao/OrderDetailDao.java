package com.ssafy.cafe.model.dao;

import com.ssafy.cafe.model.dto.OrderDetail;

public interface OrderDetailDao {
    /**
     * 주문상세정보를 입력한다. (make order에서)
     * 
     * @param detail
     * @return
     */
    int insert(OrderDetail detail);
}
