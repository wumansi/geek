package com.example.demo.jdbc;

import java.sql.*;

public class JdbcTest {
    public static void main(String[] args){
        query();
        final String insertSql = "insert into student(id,username,password,email) values(3,'sisis','12356','edsdf@.126.com')";
        modify(insertSql);
        final String updatesql = "update student set username='xiaosi' where id=3";
        modify(updatesql);
        final String deletesql = "delete from student where password is null";
        modify(deletesql);
    }

    public static void query() {
        String sql ="SELECT * FROM student";
        Connection connection = DBUtil.getConnection();
        Statement statement = null;
        ResultSet result = null;
        try {
            statement = connection.createStatement();
            result = statement.executeQuery(sql);
            while (result.next()){
                System.out.println(result.getString("username") + result.getString("email"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(connection, statement, result);
        }
    }

    public static void modify(final String sql){
        Connection connection = DBUtil.getConnection();
        try {
            Statement statement = connection.createStatement();
            statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
