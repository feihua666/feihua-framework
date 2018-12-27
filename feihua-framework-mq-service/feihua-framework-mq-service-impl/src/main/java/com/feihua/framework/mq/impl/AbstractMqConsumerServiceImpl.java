package com.feihua.framework.mq.impl;

import com.feihua.framework.mq.api.ApiMqConsumerService;
import org.springframework.amqp.core.MessageListener;

/**
 * Created by yangwei
 * Created at 2018/12/15 15:28
 */
public abstract class AbstractMqConsumerServiceImpl implements ApiMqConsumerService, MessageListener {
}
