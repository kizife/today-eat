package com.ssafy.cafe.model.service;

import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.cafe.model.dao.OrderDao;
import com.ssafy.cafe.model.dao.OrderDetailDao;
import com.ssafy.cafe.model.dao.UserDao;
import com.ssafy.cafe.model.dto.Order;
import com.ssafy.cafe.model.dto.OrderDetail;
import com.ssafy.cafe.model.dto.OrderDetailInfo;
import com.ssafy.cafe.model.dto.OrderInfo;
import com.ssafy.cafe.model.dto.User;

/**
 * @since 2021. 6. 23.
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderDao oDao;
    
    @Autowired
    OrderDetailDao dDao;
    
  
    
    @Autowired
    UserDao uDao;

    @Override
    @Transactional
    public void makeOrder(Order order) {
        // client에서 들어온 id는 무시해야 오류가 발생하지 않음.
        order.setOrderId(null);  // 이 부분은 orderId를 null로 설정하여 자동 생성하도록 함.

        // 주문 테이블 저장 (여기서 orderId가 자동 생성될 것)
        oDao.insert(order);

        // 생성된 orderId를 가져와서 OrderDetail에 설정
        Integer generatedOrderId = order.getOrderId();

        List<OrderDetail> details = order.getDetails();
        int quantitySum = 0;
        for (OrderDetail detail : details) {
            // OrderDetail에 orderId를 설정
            detail.setOrderId(generatedOrderId);
            dDao.insert(detail);
            quantitySum += detail.getQuantity();
        }
        

        //orderTime 10초 후 completed 상태를 Y로 변경
        Date orderTime = order.getOrderTime();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Order updatedOrder = new Order();
                updatedOrder.setOrderId(generatedOrderId);
                updatedOrder.setCompleted('Y');
                updatedOrder.setDeliveryTime(new Date()); // 예시로 현재 시간을 배송 시간으로 설정
                oDao.update(updatedOrder); // completed 컬럼 업데이트
            }
        }, new Date(orderTime.getTime() + 10000));  // 10초 후에 실행
    }
    
    public void updateOrderStatus(Order order) {
    	oDao.update(order);
    }
    
    
    


    @Override
    public List<Order> getOrderByUser(String id) {
        return oDao.selectByUser(id);
    }

    @Override
    public void updateOrder(Order order) {
        oDao.update(order);
    }

    @Override
//    public OrderInfo getOrderInfo(Integer orderId) {
////        return oDao.selectOrderInfo(id);
//        List<OrderInfo> orderInfos = oDao.selectOrderInfo(orderId);
//        if (orderInfos != null && !orderInfos.isEmpty()) {
//            return orderInfos.get(0);  // 첫 번째 결과를 반환
//        } else {
//            return null;  // 결과가 없을 경우 처리
//        }
//    }
    public List<OrderInfo> getOrderInfo(Integer id) {
        return oDao.selectOrderInfo(id);  // List<OrderInfo> 반환
    }

    @Override
    public List<OrderInfo> getLastMonthOrder(String id) {
    	List<OrderInfo> info = oDao.getLastMonthOrder(id); 
    	for (OrderInfo orderInfo : info) {
        	List<OrderDetailInfo> detailInfo = oDao.getOrderDetailInfo(orderInfo.getOrderId());
        	
        	orderInfo.setDetails(detailInfo);
		}
    	
        return info;
    }
    
    @Override
    public List<OrderInfo> getLast6MonthOrder(String id) {
        return oDao.getLast6MonthOrder(id);
    }
    
    @Override
    public int updateOrderStatus(Integer orderId) {
       return oDao.updateOrderStatus(orderId);
        // rowsUpdated 값을 이용해 추가 로직을 구현할 수 있습니다.
    }

}
