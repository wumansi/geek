package com.shardingsphere.demo.shardingSphere;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ShardingTestController {
    @Autowired
    private UserInfoDao dao;

    @RequestMapping("userinfo")
    public void addUserInfo(){
        UserInfo userInfo = new UserInfo();
        userInfo.setId(4);
        userInfo.setName("KK06");
        userInfo.setComment("master");
        dao.save(userInfo);
    }

    @RequestMapping("findAll")
    public String findAll() {
        List<UserInfo> res = dao.findAll();
        return res.toString();
    }
}
