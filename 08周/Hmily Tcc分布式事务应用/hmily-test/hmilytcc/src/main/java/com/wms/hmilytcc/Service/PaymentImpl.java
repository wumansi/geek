package com.wms.hmilytcc.Service;

import com.wms.hmilytcc.Client.PaymentClient;
import com.wms.hmilytcc.entity.Order;
import com.wms.hmilytcc.enums.OrderStatusEnum;
import com.wms.hmilytcc.mapper.OrderMapper;
import org.dromara.hmily.annotation.HmilyTCC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@SuppressWarnings("all")
public class PaymentImpl implements PayService {
    @Autowired(required=false)
    private OrderMapper orderMapper;

    @Autowired(required=false)
    private PaymentClient paymentClient;

    @HmilyTCC(confirmMethod = "confirm", cancelMethod = "cancel")
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void makePay(Order order) {
        System.out.println("======开始try保存订单...");
        orderMapper.saveOrder(order);
        paymentClient.payOrder(10000, order.getTotal());
    }
    public void confirm(Order order){
        System.out.println("========执行订单confirm接口=======");
        order.setStatus(OrderStatusEnum.PAY_SUCCESS.getCode());
        orderMapper.updateOrderStaus(order);
        System.out.println("========执行订单confirm操作完成=======");
    }

    public void cancel(Order order){
        System.out.println("========执行订单cancel接口=======");
        order.setStatus(OrderStatusEnum.PAY_FAIL.getCode());
        orderMapper.updateOrderStaus(order);
        System.out.println("========进行订单cancel操作完成=======");
    }
}
