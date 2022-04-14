package com.mansi.base.demo;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Random;
import java.util.UUID;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
class MillionInsert {
	@Resource
	DataSource dataSource;

	@Autowired
	JdbcTemplate jdbcTemplate;

	long timeStart = 1577808000000L;

	/**
	 * 满多少条提交记录.
	 */
	int commitCount = 10000;

	int total = 1_100_000;

	/**
	 * 生成csv文件.
	 * @throws IOException
	 */
	@ Test
	public void initFile() throws IOException {
		BufferedWriter fileOutputStream = null;
		try {
			String fullPath = "D:/test1.csv";
			File file = new File(fullPath);
			if (file.exists()){
				file.delete();
			}
			file = new File(fullPath);
			file.createNewFile();

			fileOutputStream = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "GB2312"), 1024);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for (int i=0;i<1000000;i++){
				fileOutputStream.write("\"" + UUID.randomUUID().toString() + "\"" + ",");
				fileOutputStream.write("\"" +"product" + i + "\"" + ",");
				fileOutputStream.write("\"" + new Random().nextInt(10) + "\"" + ",");
				fileOutputStream.write("\"" + "2.5" + "\"" + ",");
				fileOutputStream.write("\"" + i + "\"" + ",");
				fileOutputStream.write("\"" + "1" + "\"" + ",");
				fileOutputStream.write("\"" + sdf.format(new java.util.Date()) + "\""+ ",");
				fileOutputStream.write("\"" + sdf.format(new java.util.Date()) + "\"");
				if (i!=1000000){
					fileOutputStream.newLine();
				}
			}
			fileOutputStream.flush();
		} catch (Exception e){
			e.printStackTrace();
		} finally {
			fileOutputStream.close();
		}
	}

	/**
	 * 多线程批量插入百万数据.
	 * 多线程插入完成，核心数：4,数量：1100000,耗时：987053ms
	 * @throws InterruptedException
	 */
	@Test
	public void testManyThread() throws InterruptedException {
		jdbcTemplate.execute("truncate table t_order;");
		int cores = Runtime.getRuntime().availableProcessors();
		Thread[] threads = new Thread[cores];
		int perSize = total/cores;
		Order[] data = buildData();
		long startInsert = System.currentTimeMillis();
		for (int i=0;i<cores;i++){
			int start = i*perSize;
			if (i == cores - 1){
				int value = i;
				threads[i] = new Thread(()->{
					try {
						runInsertBatch(start, total-(value*perSize), data);
					}catch (Exception e){
						e.printStackTrace();
					}
				});
			} else {
				threads[i] = new Thread(()->{
					try {
						runInsertBatch(start, perSize, data);
					}catch (Exception e){
						e.printStackTrace();
					}
				});
			}
			threads[i].setName("thread:"+ i);
			threads[i].start();
		}
		for (int i=0;i<cores;i++){
			threads[i].join();
		}
		long costInsert = System.currentTimeMillis() - startInsert;
		log.info("多线程插入完成，核心数：{},数量：{},耗时：{}ms",cores,total,costInsert);
	}

	private void runInsertBatch(int start, int left, Order[] data) throws Exception {
		Connection connection = dataSource.getConnection();
		connection.setAutoCommit(false);
		String insert = "INSERT INTO `T_ORDER` (order_num,good_num,quantity,price,userid,status,s_create_time,s_last_time) values" +
				"(?,?,?,?,?,?,?,?)";
		PreparedStatement preparedStatement = connection.prepareStatement(insert);
		int len = left+start;
		int count = 0;
		int total = 0;
		for (int i=start;i<len;i++){
			Order o = data[i];
			preparedStatement.setString(1, o.getOrderNum());
			preparedStatement.setString(2, o.getGoodNum());
			preparedStatement.setInt(3, o.getQuantity());
			preparedStatement.setBigDecimal(4, o.getPrice());
			preparedStatement.setInt(5, o.getUserid());
			preparedStatement.setInt(6, o.getStatus());
			preparedStatement.setDate(7, o.getSCreateTime());
			preparedStatement.setDate(8, o.getSLastTime());
			preparedStatement.addBatch();
			count++;
			total++;
			if (count >= commitCount){
				preparedStatement.executeBatch();
				connection.commit();
				count = 0;
				log.info("{}:插入了：{}条", Thread.currentThread().getName(), total);
			}
		}
		if (count > 0){
			preparedStatement.executeBatch();
			connection.commit();
			log.info("{}:循环结束,插入了：{}条", Thread.currentThread().getName(), total);
		}
	}

	private Order[] buildData(){
		long day = 1000 * 60 * 60 * 24;
		Order[] result = new Order[total];
		for (int i =0;i<total;i++){
			Order order = new Order();
			order.setOrderNum(UUID.randomUUID().toString());
			order.setGoodNum("product"+ i);
			order.setPrice(new BigDecimal(new Random().nextInt(999)+1));
			order.setQuantity(i);
			order.setStatus(1);
			order.setUserid(new Random().nextInt(100)+1);
			order.setSCreateTime(new Date(timeStart + (new Random().nextInt(365) * day)));
			order.setSLastTime(new Date(timeStart + (new Random().nextInt(365) * day)));
			result[i] = order;
		}
		return result;
	}

	private BufferedWriter out = null;

	/**
	 * 多线程插入数据到文件.
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@Test
	public void buildInsertData() throws IOException, InterruptedException {
		String filePath = "D:\\testInsert\\testMillion.csv";
		Path path = Paths.get(filePath);
		if (!Files.exists(path)){
			Files.createFile(path);
		}
		out = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(new File(filePath), true)));
		long startTime = System.currentTimeMillis();
		Order[] orders = buildData();
		long costCreateObj = System.currentTimeMillis()-startTime;
		log.info("创建对象完成，耗时：{}ms", costCreateObj);

		int cores = Runtime.getRuntime().availableProcessors();
		int perSize = total/cores;
		Thread[] threads = new Thread[cores];
		long startInsert = System.currentTimeMillis();
		for (int i = 0; i<cores; i++){
			int start = i*perSize;
			int value = i;
			if (i == cores - 1){
				threads[i] = new Thread(()->{
					try {
						buildDataToFile(start, total-(value*perSize), orders);
					} catch (Exception e) {
						e.printStackTrace();
					}
				});
			} else{
				threads[i] = new Thread(()->{
					try {
						buildDataToFile(start, perSize, orders);
					} catch (Exception e) {
						e.printStackTrace();
					}
				});
			}
			threads[i].setName("thread:" + i);
			threads[i].start();
		}
		for (int i = 0;i<cores;i++){
			threads[i].join();
		}
		long costInsert = System.currentTimeMillis()-startInsert;
		log.info("多线程插入csv完成，核心数：{},数量：{},耗时：{}",cores,total,costInsert);
	}

	/**
	 * 插入数据到csv文件中.
	 * @param start 开始位置
	 * @param left 偏移位置
	 * @param data 插入源数据
	 * @throws Exception
	 */
	private void buildDataToFile(int start, int left, Order[] data) throws Exception{
		int len = start+left;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		StringBuilder sb = new StringBuilder();
		int count = 0;
		int total = 0;
		for (int i=start;i<len;i++){
			Order o = data[i];
			sb.append(o.getOrderNum()).append(",");
			sb.append(o.getGoodNum()).append(",");
			sb.append(o.getQuantity()).append(",");
			sb.append(o.getPrice()).append(",");
			sb.append(o.getUserid()).append(",");
			sb.append(o.getStatus()).append(",");
			sb.append(sdf.format(o.getSCreateTime())).append(",");
			sb.append(sdf.format(o.getSLastTime())).append("\n");
			count++;
			total++;
			if (count >= commitCount){
				out.append(sb.toString());
				out.flush();
				count = 0;
				sb = new StringBuilder();
				log.info("{}:插入了：{}条",Thread.currentThread().getName(),total);
			}
		}

		if (count > 0){
			out.append(sb.toString());
			out.flush();
			log.info("{}:循环结束，插入了：{}条",Thread.currentThread().getName(),total);
		}
	}
}
