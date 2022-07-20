package com.sisi.rpcserver;

import com.sisi.rpccore.Netty.server.RpcNettyServer;
import com.sisi.rpccore.proxy.ProviderServiceManagement;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.crypto.spec.RC2ParameterSpec;

@SpringBootApplication
public class RpcServerApplication {

	public static void main(String[] args) throws Exception {

		final int port = 8080;
		ProviderServiceManagement.init("com.sisi.rpcserver", port);

		final RpcNettyServer rpcNettyServer = new RpcNettyServer(port);

		try {
			rpcNettyServer.run();
		} catch (Exception e){
			e.printStackTrace();
		} finally {
			rpcNettyServer.destroy();
		}
	}

}
