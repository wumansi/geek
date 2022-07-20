package com.sisi.rpcapi.service;


import com.sisi.rpcapi.model.Order;

public interface OrderService {
    Order findById(Integer id);
}
