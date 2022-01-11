package com.wms.hmilytcc.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class Order implements Serializable{
    private static final long serialVersionUID = -1395008436551995931L;
    private String number;
    private int count;

    private BigDecimal total;

    private int userId;

    private Date createTime;

    private int status;
    private String productId;
}
