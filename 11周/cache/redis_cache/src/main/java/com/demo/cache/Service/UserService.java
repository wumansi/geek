package com.demo.cache.Service;

import com.demo.cache.entity.User;
import org.springframework.cache.annotation.CacheConfig;

import java.util.List;

public interface UserService {
    User find(int id);

    List<User> list();

    int requestCount();
}
