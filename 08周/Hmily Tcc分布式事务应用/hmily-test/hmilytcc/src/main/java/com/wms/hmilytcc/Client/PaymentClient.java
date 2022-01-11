package com.wms.hmilytcc.Client;

import org.dromara.hmily.annotation.Hmily;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;

@FeignClient(value = "payment-service")
public interface PaymentClient {
    @RequestMapping("/pay/payment")
    @Hmily
    Boolean payOrder(@RequestParam("userId") Integer userId, @RequestParam("amount") BigDecimal amount);
}
