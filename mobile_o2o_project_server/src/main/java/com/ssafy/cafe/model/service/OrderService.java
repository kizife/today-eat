package com.ssafy.cafe.model.service;

import java.util.List;

import com.ssafy.cafe.model.dto.Order;
import com.ssafy.cafe.model.dto.OrderInfo;

public interface OrderService {
    /**
     * 새로운 Order를 생성한다.
     * Order와 OrderDetail에 정보를 추가한다.
     * [심화]User 테이블에 사용자의 Stamp 개수를 업데이트 한다.
     * [심화]Stamp 테이블에 Stamp 이력을 추가한다.
     * @param order
     */
    public void makeOrder(Order order);
    
    /**
     * id에 해당하는 사용자의 Order 목록을 주문 번호의 내림차순으로 반환한다.
     * 
     * @param id
     * @return
     */
    public List<Order> getOrderByUser(String id);
    
    /**
     * 주문 정보를 수정한다. - 주문의 상태만 변경된다.
     * @param order
     */
    public void updateOrder(Order order);
    
    /**
     * orderId에 대한 Order와 OrderDetail에 대한 내용까지 반환한다.
     * 그리고, 추가적으로 토탈금액, 상품명 등의 추가적인 정보가 담긴 
     * OrderInfo객체를 리턴한다. 
     * OrderDetail의 내용은 detail id의 오름차순으로 조회한다.
     * 
     * @param id
     * @return
     */
    public List<OrderInfo> getOrderInfo(Integer orderId);

    /**
     * 사용자가 주문한 최근 1개월의 주문 주문번호 내림차순으로 조회된다. 
     * 주문번호의 상세내용은 detail id의 오름차순으로 조회된다. 
     * 관통 6단계에서 사용된다. 
     * @param id
     * @return
     */
    List<OrderInfo> getLastMonthOrder(String id);

    /**
     * 사용자가 주문한 최근 6개월의 주문 주문번호 내림차순으로 조회된다.
     * 주문번호의 상세내용은 detail id의 오름차순으로 조회된다. 
     * 관통 6단계에서 사용된다. 
     * @param id
     * @return
     */
    List<OrderInfo> getLast6MonthOrder(String id);
    
    public int updateOrderStatus(Integer orderId);
}
