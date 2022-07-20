package com.sisi.rpcserver.impl2;

import com.sisi.rpcapi.model.Order;
import com.sisi.rpcapi.service.OrderService;
import com.sisi.rpccore.anotation.ProviderService;

@ProviderService(service = "com.sisi.rpcapi.service.OrderService", weight = 8, tags = "tag2")
public class OrderServiceImpl implements OrderService {
    @Override
    public Order findById(Integer id) {
        return new Order(1, "rpc", 1);
    }
}
