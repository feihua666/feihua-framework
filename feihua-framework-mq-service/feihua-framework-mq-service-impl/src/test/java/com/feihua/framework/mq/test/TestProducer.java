package com.feihua.framework.mq.test;

import com.feihua.framework.mq.api.ApiMqProducerService;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by yangwei
 * Created at 2018/12/15 15:22
 */
public class TestProducer implements ApiMqProducerService {
    @Autowired
    private AmqpTemplate amqpTemplate;


    @Override
    public void sendMessage(Object o) {
        amqpTemplate.convertAndSend(o);
    }
}
