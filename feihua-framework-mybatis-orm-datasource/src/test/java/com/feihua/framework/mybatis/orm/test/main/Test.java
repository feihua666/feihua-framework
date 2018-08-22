package com.feihua.framework.mybatis.orm.test.main;

import com.feihua.framework.mybatis.orm.MultipleDataSource;
import com.feihua.framework.mybatis.orm.mapper.NativeSqlMapper;
import com.feihua.framework.mybatis.orm.test.mapper.DataSourceAMapper;
import com.feihua.framework.mybatis.orm.test.mapper.DataSourceBMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

/**
 * Created by yangwei
 * Created at 2018/6/29 13:01
 */
public class Test {

    public static void main(String[] args) {
        //初始化ApplicationContext
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext-mybatis-orm-use.xml");
        //ApplicationContext applicationContext = new GenericXmlApplicationContext("applicationContext-mybatis-orm.xml");

        DataSourceAMapper dataSourceAMapper = applicationContext.getBean(DataSourceAMapper.class);

        DataSourceBMapper dataSourceBMapper = applicationContext.getBean(DataSourceBMapper.class);
        NativeSqlMapper nativeSqlMapper = applicationContext.getBean(NativeSqlMapper.class);

        //设置数据源为MySql,使用了AOP测试时请将下面这行注释
        MultipleDataSource.setDataSourceKey(MultipleDataSource.DataSourceKey.dataSourceDefault.name());

        List list = nativeSqlMapper.selectByNativeSqlForList("select * from base_user");

        List lista = dataSourceAMapper.getAll();
        //设置数据源为SqlServer,使用AOP测试时请将下面这行注释
        MultipleDataSource.setDataSourceKey(PMultipleDataSource.DataSourceKey.dataSourceB.name());

        List listb = dataSourceBMapper.getAll();

        System.out.println(lista.size());
        System.out.println(listb.size());
    }
}
