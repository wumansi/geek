package com.example.demo.jdbc;

import java.sql.*;

public class BatchHandle {
    public static void main(String[] args) throws SQLException {
        batchInsert();
    }
    public static void batchInsert() throws SQLException {
        PreparedStatement statement = null;
        ResultSet rs = null;
        final String sql = "insert into student(id, username) values(?,?)";
        Connection connection = DBUtil.getConnection();
        try {
            // 开启事务
            connection.setAutoCommit(false);
            statement = connection.prepareStatement(sql);
            for (int i = 5;i<20;i++){
                statement.setInt(1, i);
                statement.setString(2, "sisi" + i);
                statement.addBatch();
            }
            statement.executeBatch();
            connection.commit();
        } catch (SQLException e) {
            if (connection != null){
                connection.rollback();
            }
            e.printStackTrace();
        } finally {
            DBUtil.close(connection, statement, rs);
        }
    }
}
