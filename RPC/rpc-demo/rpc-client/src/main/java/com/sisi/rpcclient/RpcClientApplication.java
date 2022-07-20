package com.sisi.rpcclient;

import com.alibaba.fastjson.parser.ParserConfig;
import com.sisi.rpcapi.model.Order;
import com.sisi.rpcapi.model.User;
import com.sisi.rpcapi.service.OrderService;
import com.sisi.rpcapi.service.UserService;
import com.sisi.rpccore.filter.Retry;
import com.sisi.rpccore.proxy.RpcClient;

import java.util.Arrays;

public class RpcClientApplication {

	public static void main(String[] args) {
		ParserConfig.getGlobalInstance().addAccept("com.sisi.rpcapi.model.Order");
		ParserConfig.getGlobalInstance().addAccept("com.sisi.rpcapi.model.User");

		Retry.setRetryLimit(3);

		RpcClient client = new RpcClient();
		OrderService orderService = client.create(OrderService.class);
		Order order = orderService.findById(11888);
		System.out.println(String.format("find order name=%s, user=%d", order.getName(), order.getUserId()));

		OrderService orderService1 = client.create(OrderService.class, "group2", "v2", Arrays.asList("tag1", "tag2"));
		Order order1 = orderService1.findById(11888);
		System.out.println(String.format("find order name=%s, user=%d", order1.getName(), order1.getUserId()));

		UserService userService = client.create(UserService.class, "group2", "v2");
		User user = userService.findById(1);
		System.out.println(String.format("find User id=1 from server" + user.getName()));

	}

}
