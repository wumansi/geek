package com.sisi.rpcapi.service;

import com.sisi.rpcapi.model.User;

public interface UserService {
    User findById(Integer id);
}
