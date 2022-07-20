package com.sisi.rpcserver.impl2;

import com.sisi.rpcapi.model.User;
import com.sisi.rpcapi.service.UserService;
import com.sisi.rpccore.anotation.ProviderService;

/**
 * 基于tag标签路由
 */
@ProviderService(service = "com.sisi.rpcapi.service.UserService", group = "group2", version = "v2")
public class UserServiceIV2Impl implements UserService{
    @Override
    public User findById(Integer id) {
        return new User(id, "rpc group2 v2");
    }
}
