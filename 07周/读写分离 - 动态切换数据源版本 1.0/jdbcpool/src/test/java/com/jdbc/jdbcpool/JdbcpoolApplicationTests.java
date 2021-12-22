package com.jdbc.jdbcpool;

import com.alibaba.druid.pool.DruidDataSource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Random;
import java.util.UUID;

@SpringBootTest
class JdbcpoolApplicationTests {

//    @Resource
//    DruidDataSource dataSource;

    @Test
    public void contextLoads() throws SQLException {
//        Connection connection = hikariDataSource.getConnection();
//        DruidDataSource dataSource = (DruidDataSource) hikariDataSource;
//        System.out.println("druidDataSource 数据源最大连接数：" + dataSource.getMaxActive());
//        System.out.println("druidDataSource 数据源初始化连接数：" + dataSource.getInitialSize());
//        connection.close();
    }

    @Test
    public void importdata() {
        try {
            File writeName = new File("D:/t_order_info.txt");
            //如果没有则新建一个文件，有同名的则覆盖
            writeName.createNewFile();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sdf.format(new java.util.Date());
            try (FileWriter writer = new FileWriter(writeName);
                 BufferedWriter out = new BufferedWriter(writer)
            ) {
                for (int i= 1;i<1000001;i++){
                    out.write(UUID.randomUUID().toString()+"||"+  //自增主键
                            "product"+i+"||"+
                            new Random().nextInt(10)+"||"+
                            "2.5"+"||"+
                            i+"||"+
                            1+"||"+
                            sdf.format(new java.util.Date())+"||"+
                            sdf.format(new java.util.Date())+"\n");
                }
                out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testInsert() throws SQLException {
//        Connection connection = dataSource.getConnection();
//        PreparedStatement statement = null;
//        final String sql = "insert into t_order(order_num,good_num,quantity,price,userid,status,s_create_time,s_last_time)" +
//                " values(?,?,?,?,?,0,?,?)";
////        final String sql = "insert into t_order(order_num,good_num) values(?,?)";
////        final String sql = "insert into student(id,username)values(?,?)";
//        try {
//            connection.setAutoCommit(false);
//            statement = connection.prepareStatement(sql);
//            System.out.println("开始循环sql....");
//            long stime = System.currentTimeMillis();
//            for (int i= 1; i<=1000000;i++){
////                statement.setInt(1,i);
//                statement.setString(1, UUID.randomUUID().toString());
//                statement.setString(2, "product"+i);
//                statement.setInt(3, new Random().nextInt(10));
//                statement.setBigDecimal(4, new BigDecimal(2.5));
//                statement.setInt(5, i);
//                statement.setDate(6, new Date(System.currentTimeMillis()));
//                statement.setDate(7, new Date(System.currentTimeMillis()));
//                statement.addBatch();
//            }
//            System.out.println("结束循环sql，耗时：" + (System.currentTimeMillis() - stime));
//            stime = System.currentTimeMillis();
//            System.out.println("开始执行插入....");
//            statement.executeBatch();
//            connection.commit();
//            System.out.println("结束执行插入，耗时：" + (System.currentTimeMillis() - stime));
//        } catch (SQLException e){
//            if (connection != null){
//                connection.rollback();
//            }
//            e.printStackTrace();
//        } finally {
//            if (statement != null){
//                try {
//                    statement.close();
//                } catch (SQLException e){
//                    e.printStackTrace();
//                }
//            }
//            if (connection != null){
//                try {
//                    connection.close();
//                } catch (SQLException e){
//                    e.printStackTrace();
//                }
//            }
//        }
    }

}
