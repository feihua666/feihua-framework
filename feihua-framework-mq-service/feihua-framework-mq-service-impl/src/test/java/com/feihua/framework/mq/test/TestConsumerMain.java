package com.feihua.framework.mq.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static java.lang.Thread.*;

/**
 * Created by yangwei
 * Created at 2018/12/15 14:53
 */
public class TestConsumerMain {
    public static void main(String[] args) {
        //初始化ApplicationContext
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext-mq-service-test.xml");

        try {
            sleep(5000);
            ((ClassPathXmlApplicationContext) applicationContext).close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
