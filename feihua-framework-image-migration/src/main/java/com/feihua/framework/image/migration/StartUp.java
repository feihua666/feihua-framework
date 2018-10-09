package com.feihua.framework.image.migration;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 运行入口
 * Created by yangwei
 * Created at 2018/10/9 9:54
 */
public class StartUp {
    public static void main(String[] args) {
        //初始化ApplicationContext
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        ApiMain apiMain = applicationContext.getBean(ApiMain.class);
        apiMain.go();
    }
}
