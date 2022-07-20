package com.demo.cache.Controller;

import com.demo.cache.Service.UserService;
import com.demo.cache.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping("user/{id}")
    @ResponseBody
    User find(@PathVariable(value = "id") Integer id){
        User user1 = userService.find(id);
        User user2 = userService.find(id);
        System.out.println(user1 == user2);
        return user2;
    }

    @RequestMapping("list")
    @ResponseBody
    List<User> list(){
        return userService.list();
    }

    @RequestMapping("/count")
    int requCount(){
        return userService.requestCount();
    }
}
