package com.wms.payment.Service;

import java.math.BigDecimal;

public interface PaymentService {
    Boolean payment(Integer userId, BigDecimal amount);
}
