package com.feihua.framework.mybatis.orm;

import com.feihua.utils.properties.PropertiesUtils;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;

import static org.springframework.util.Assert.notNull;

/**
 * 一定要将 processPropertyPlaceHolders 属性设置为false，默认是false，可以不配置
 * 为了解决mybatis MapperScannerConfigurer 不能注入属性问题
 * Created by yangwei
 * Created at 2018/7/18 18:15
 */
public class MyMapperScannerConfigurer extends MapperScannerConfigurer{
    private String mybasePackage;
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) {

        if(mybasePackage.indexOf("${")>=0 && mybasePackage.indexOf("}") > 0){
            String express = mybasePackage.substring(mybasePackage.indexOf("${"),mybasePackage.indexOf("}") + 1);
            PropertiesUtils.addPropertyPath(new String[]{"classpath:mybatis-orm-config.properties"});
            String _basePackage =  PropertiesUtils.getProperty(express.substring(2,express.length()-1)).toString();
            _basePackage = mybasePackage.replace(express,_basePackage);
            setBasePackage(_basePackage);
        }

        super.postProcessBeanDefinitionRegistry(registry);
    }

    public String getMybasePackage() {
        return mybasePackage;
    }

    public void setMybasePackage(String mybasePackage) {
        this.mybasePackage = mybasePackage;
    }
}
