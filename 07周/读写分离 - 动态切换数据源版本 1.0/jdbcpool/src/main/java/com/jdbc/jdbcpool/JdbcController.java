package com.jdbc.jdbcpool;

import com.alibaba.druid.pool.DruidDataSource;
import com.jdbc.jdbcpool.muldatasource.DynamicRoutingDataSource;
import com.jdbc.jdbcpool.muldatasource.MultiDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@RestController
public class JdbcController {
    @Autowired
    private JdbcTemplate jdbcTemplate;

//    @RequestMapping("/testHikari")
//    @ResponseBody
//    public String test() {
//        System.out.println("数据源" + hikariDataSource.getClass().getName());
//        Connection connection = null;
//        try {
//            connection = hikariDataSource.getConnection();
//            ResultSet resultSet = connection.prepareStatement("SELECT count(*) FROM student").executeQuery();
//            String str = "";
//            while (resultSet.next()) {
//                str = resultSet.getString(1);
//            }
//            connection.close();
//            return str;
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return "";
//    }

    @ResponseBody
    @RequestMapping("/testJdbc")
    @DynamicRoutingDataSource("dataSource2")
    public String test2(){
        System.out.println("开始执行查询...");
        long stime = System.currentTimeMillis();
        List<Map<String, Object>> list = jdbcTemplate.queryForList("SELECT * FROM t1");
        System.out.println("结束执行，耗时：" + (System.currentTimeMillis() - stime));
//        System.out.println(list.get(0).get("good_num").toString());
//        return list.get(0).get("good_num").toString();
        System.out.println(list.get(0).get("id").toString());
        return list.get(0).get("id").toString();
    }
}
