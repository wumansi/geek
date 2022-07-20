package com.sisi.rpcserver.impl2;

import com.sisi.rpcapi.model.User;
import com.sisi.rpcapi.service.UserService;
import com.sisi.rpccore.anotation.ProviderService;

/**
 * 添加服务端权重负载均衡
 */
@ProviderService(service = "com.sisi.rpcapi.service.UserService", weight = 8)
public class UserServiceImpl implements UserService {
    @Override
    public User findById(Integer id) {
        return new User(id, "rpc weight 2");
    }
}
