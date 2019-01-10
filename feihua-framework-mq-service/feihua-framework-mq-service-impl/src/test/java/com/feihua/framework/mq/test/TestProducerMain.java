package com.feihua.framework.mq.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by yangwei
 * Created at 2018/12/15 14:53
 */
public class TestProducerMain {
    public static void main(String[] args) throws InterruptedException {
        //初始化ApplicationContext
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext-mq-service-test.xml");
        TestProducer producer = applicationContext.getBean(TestProducer.class);
        producer.sendMessage("你好");
        Thread.sleep(1000);
        ((ClassPathXmlApplicationContext) applicationContext).close();
    }
}
