package com.wms.hmilytcc.Service;

import java.math.BigDecimal;

/**
 * 订单接口.
 */
public interface OrderService {
    String payOrder(Integer count, BigDecimal amount);
}
