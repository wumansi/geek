# 分库分表
create schema demo_ds_0;
create schema demo_ds_1;
 
CREATE TABLE IF NOT EXISTS t_order (order_id BIGINT NOT NULL AUTO_INCREMENT, user_id INT NOT NULL, status VARCHAR(50), PRIMARY KEY (order_id));

insert into t_order(user_id,status) values(1,'OK'),(1,'FAIL');
insert into t_order(user_id,status) values(2,'OK'),(2,'FAIL');

select * from t_order;
select * from t_order where user_id=2 and order_id=684171775491682305;

 delete from t_order where  user_id=2;

 update t_order set status='DOING' where user_id=2 and order_id=684171775491682304;