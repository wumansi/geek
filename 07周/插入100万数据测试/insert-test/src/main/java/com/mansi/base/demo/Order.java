package com.mansi.base.demo;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Date;

@Data
public class Order {
    private String orderNum;
    private String goodNum;
    private Integer quantity;
    private BigDecimal price;
    private int userid;
    private int status;
    private Date sCreateTime;
    private Date sLastTime;
}
