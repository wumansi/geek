package com.wms.payment.Controller;

import com.wms.payment.Service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/pay")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @RequestMapping(value = "/payment")
    public Boolean payOrder(@RequestParam("userId")Integer userId, @RequestParam("amount")BigDecimal amount){
        return paymentService.payment(userId, amount);
    }
}
