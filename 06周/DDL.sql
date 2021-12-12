CREATE TABLE T_USE(
USEID INT PRIMARY KEY COMMENT '用户id',
username VARCHAR(255) COMMENT '用户名',
phonenum int COMMENT '电话号码'
) COMMENT='用户信息';

create TABLE t_harvest_address(
gid VARCHAR(36) PRIMARY KEY COMMENT '主键',
userid INT,
receiver VARCHAR(36) COMMENT '收货人姓名',
area VARCHAR(200) COMMENT '所在地区',
address VARCHAR(500) COMMENT '详细地址',
contract INT COMMENT '联系方式'
)COMMENT='收获相关信息';

CREATE TABLE t_goods(
good_num VARCHAR(36) PRIMARY KEY COMMENT '商品编号',
good_name VARCHAR(255) COMMENT '商品名称',
category_id VARCHAR(36) COMMENT '所属类别',
price NUMERIC(18,2) COMMENT '当前价格'
) COMMENT='商品信息';

CREATE TABLE t_order(
order_num VARCHAR(36) PRIMARY KEY COMMENT '订单编号',
good_num VARCHAR(36) COMMENT '商品编号',
quantity INT COMMENT '商品数量',
price NUMERIC(18,2) COMMENT '交易价格',
userid INT COMMENT '用户id',
harvest_address_id VARCHAR(36) COMMENT '收获信息id',
status INT COMMENT'订单状态',
payment_time DATE COMMENT '付款时间',
payment_account VARCHAR(36) COMMENT '付款账号',
logistics_num VARCHAR(36) COMMENT '物流号',
s_create_time DATE COMMENT '创建时间',
s_last_time DATE COMMENT '更新时间'
) COMMENT='订单信息';