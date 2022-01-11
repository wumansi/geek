package com.wms.hmilytcc.Controller;

import com.wms.hmilytcc.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping(value = "/payOrder")
    public String payOrder(@RequestParam(value = "count") Integer count,
                           @RequestParam(value = "amount") BigDecimal amount){
        return orderService.payOrder(count, amount);
    }
}
