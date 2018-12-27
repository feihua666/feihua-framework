package com.feihua.framework.mq.test;

import com.feihua.framework.mq.impl.AbstractMqConsumerServiceImpl;
import org.springframework.amqp.core.Message;

import java.io.UnsupportedEncodingException;

/**
 * Created by yangwei
 * Created at 2018/12/15 15:22
 */
public class TestConsumer extends AbstractMqConsumerServiceImpl {
    @Override
    public void onMessage(Message message) {
        System.out.println("收到消息");
        try {
            System.out.println(new String(message.getBody(),"utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
