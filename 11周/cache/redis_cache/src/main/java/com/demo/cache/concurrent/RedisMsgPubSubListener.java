package com.demo.cache.concurrent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPubSub;

public class RedisMsgPubSubListener extends JedisPubSub{
    private Logger logger = LoggerFactory.getLogger(RedisMsgPubSubListener.class);
    @Override
    public void onMessage(String channel, String message) {
        logger.info("onMessage: channel[{}], message[{}]",channel, message);
    }

    @Override
    public void onPMessage(String pattern, String channel, String message) {
        logger.info("onPMessage: pattern[{}], channel[{}], message[{}]", pattern, channel, message);
    }

    @Override
    public void onSubscribe(String channel, int subscribedChannels) {
        logger.info("onSubscribe: channel[{}], subscribedChannels[{}]", channel, subscribedChannels);
    }

    @Override
    public void onPUnsubscribe(String pattern, int subscribedChannels) {
        logger.info("onPUnsubscribe: pattern[{}], subscribedChannels[{}]", pattern, subscribedChannels);
    }

    @Override
    public void onPSubscribe(String pattern, int subscribedChannels) {
        logger.info("onPSubscribe: pattern[{}], subscribedChannels[{}]", pattern, subscribedChannels);
    }

    @Override
    public void onUnsubscribe(String channel, int subscribedChannels) {
        logger.info("channel:{} is been subscribed:{}", channel, subscribedChannels);
    }
}
