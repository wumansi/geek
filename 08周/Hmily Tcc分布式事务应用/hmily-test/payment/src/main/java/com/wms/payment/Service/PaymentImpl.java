package com.wms.payment.Service;

import com.wms.payment.mapper.PaymentMapper;
import org.dromara.hmily.annotation.HmilyTCC;
import org.dromara.hmily.common.exception.HmilyRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class PaymentImpl implements PaymentService{
    @Autowired(required = false)
    private PaymentMapper paymentMapper;

    @Override
    @HmilyTCC(confirmMethod = "confirmPay",cancelMethod = "cancelPay")
    @Transactional(rollbackFor = Exception.class)
    public Boolean payment(Integer userId, BigDecimal amount) {
        System.out.println("=======执行try付款接口====");
        int decrease = paymentMapper.payOrder(amount, userId);
        if (decrease != 1) {
            throw new HmilyRuntimeException("账户余额不足");
        }
        return Boolean.TRUE;
    }

    public boolean confirmPay(Integer userId, BigDecimal amount){
        System.out.println("=======执行confirm付款接口====");
        return  paymentMapper.confirm(amount, userId) > 0;
    }

    public boolean cancelPay(Integer userId, BigDecimal amount){
        System.out.println("=======执行cancel付款接口====");
        return  paymentMapper.cancel(amount, userId) > 0;
    }
}
