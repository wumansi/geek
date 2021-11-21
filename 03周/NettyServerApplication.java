package com.example.gateway;

import com.example.gateway.inbound.HttpInboundServer;

import java.util.Arrays;

public class NettyServerApplication {
    public final static String GATEWAY_NAME = "NIOGateway";
    public final static String GATEWAY_VERSION = "3.0.0";

    public static void main(String[] args) {
        String proxyPort = System.getProperty("proxyPort", "8888");

        String proxyServer = System.getProperty("proxyServer", "http://localhost:8801,http://localhost:8802");
        int port = Integer.parseInt(proxyPort);
        System.out.println(GATEWAY_NAME + GATEWAY_VERSION + "starting...");
        HttpInboundServer server = new HttpInboundServer(port, Arrays.asList(proxyServer.split(",")));
        System.out.println(GATEWAY_NAME + " " + GATEWAY_VERSION +" started at http://localhost:" + port + " for server:" + server.toString());
        try {
            server.run();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
