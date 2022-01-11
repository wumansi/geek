package com.wms.hmilytcc.Service;

import com.wms.hmilytcc.entity.Order;
import com.wms.hmilytcc.enums.OrderStatusEnum;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class OrderImpl implements OrderService{
    @Autowired(required=false)
    private PayService payService;

    @Override
    public String payOrder(Integer count, BigDecimal amount) {
        Order order = buildOrder(count, amount);
        long start = System.currentTimeMillis();
        payService.makePay(order);
        System.out.println("hmily-test分布式事务耗时：" + (System.currentTimeMillis() - start));
        return "success";
    }

    private Order buildOrder(Integer count, BigDecimal amount) {
        Order order = new Order();
        order.setNumber(RandomStringUtils.random(20, true, true));
        order.setCount(count);
        order.setCreateTime(new Date());
        order.setTotal(amount);
        order.setUserId(10000);
        order.setProductId("2");
        order.setStatus(OrderStatusEnum.PAYING.getCode());
        return order;
    }


}
