package com.sisi.rpccore.discovery;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * ZK客户端，用于连接ZK
 */
@Slf4j
public class ZookeeperClient {
    static final String REGISTER_ROOT_PATH = "rpc";

    protected CuratorFramework client;

    ZookeeperClient() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        this.client = CuratorFrameworkFactory.builder()
                .connectString("localhost:2181")
                .namespace(REGISTER_ROOT_PATH)
                .retryPolicy(retryPolicy)
                .build();
        this.client.start();

        log.info("zookeeper service register init");
    }
}
