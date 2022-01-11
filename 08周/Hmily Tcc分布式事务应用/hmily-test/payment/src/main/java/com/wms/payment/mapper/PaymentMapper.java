package com.wms.payment.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;

public interface PaymentMapper {
    @Update("update account set balance = balance - #{amount}," +
            "freeze_amount = freeze_amount + #{amount}, update_time=now()" +
            " where user_id=#{userId} and balance >= #{amount}")
    int payOrder(@Param("amount") BigDecimal amount, @Param("userId") Integer userId);

    @Update("update account set freeze_amount = freeze_amount - #{amount}" +
            " where user_id=#{userId} and freeze_amount >= #{amount}")
    int confirm(@Param("amount") BigDecimal amount, @Param("userId") Integer userId);

    @Update("update account set balance = balance + #{amount},freeze_amount = freeze_amount - #{amount}" +
            " where user_id=#{userId} and freeze_amount >= #{amount}")
    int cancel(@Param("amount") BigDecimal amount, @Param("userId")Integer userId);
}
