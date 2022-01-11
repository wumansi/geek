package com.wms.hmilytcc.mapper;

import com.wms.hmilytcc.entity.Order;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;

public interface OrderMapper {

    @Insert("insert into `order` (number,create_time,count,total_amount,user_id,status,product_id) " +
            " values (#{number},#{createTime},#{count},#{total},#{userId},#{status},#{productId})")
    int saveOrder(Order order);

    @Update("update `order` set status = #{status} where number=#{number}")
    void updateOrderStaus(Order order);
}
