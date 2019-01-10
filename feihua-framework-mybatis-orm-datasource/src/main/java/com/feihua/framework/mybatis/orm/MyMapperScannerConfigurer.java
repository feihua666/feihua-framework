package com.feihua.framework.mybatis.orm;

import com.feihua.utils.properties.PropertiesUtils;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.PropertyResourceConfigurer;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;
import java.util.Map;

import static org.springframework.util.Assert.notNull;

/**
 * 一定要将 processPropertyPlaceHolders 属性设置为false，默认是false，可以不配置
 * 为了解决mybatis MapperScannerConfigurer 不能注入属性问题
 * Created by yangwei
 * Created at 2018/7/18 18:15
 */
public class MyMapperScannerConfigurer extends MapperScannerConfigurer{

    private Map<String,MybatisMapperConfig> mybatisMapperConfigs;
    private ApplicationContext applicationContext;
    private static String cofigName = DefaultMybatisMapperConfig.class.getName();
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) {

        if (mybatisMapperConfigs == null) {
            String names[] = applicationContext.getBeanDefinitionNames();
            DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
            BeanDefinition beanDefinition = null;
            for (String name : names) {
                    beanDefinition = ((ConfigurableApplicationContext) applicationContext)
                            .getBeanFactory().getBeanDefinition(name);
                if(cofigName.equals(beanDefinition.getBeanClassName())){
                    factory.registerBeanDefinition(name, beanDefinition);
                }
            }
            mybatisMapperConfigs = factory.getBeansOfType(MybatisMapperConfig.class);
        }
        StringBuffer sb = new StringBuffer();
        for (String key : mybatisMapperConfigs.keySet()) {
            sb.append(",");
            sb.append(mybatisMapperConfigs.get(key).getBasePackage());
        }
        setBasePackage(sb.substring(1));
        super.postProcessBeanDefinitionRegistry(registry);
    }
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        super.setApplicationContext(this.applicationContext);
    }
    @Override
    public void afterPropertiesSet() throws Exception {
    }
}
