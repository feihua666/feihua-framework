package com.feihua.framework.mybatis.orm;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.springframework.core.NestedIOException;
import org.springframework.core.io.Resource;

import java.io.IOException;

/**
 * 由于mybatis的原因，如果有xml有错误，spring报不出错，会一直启动，这里修改，如果有错，退出
 * Created by yw on 2016/8/10.
 */
public class SqlSessionBeanFactory extends SqlSessionFactoryBean {

    private static Logger logger = org.slf4j.LoggerFactory.getLogger(SqlSessionBeanFactory.class);
    @Override
    protected SqlSessionFactory buildSqlSessionFactory() throws IOException {
        try {
            return super.buildSqlSessionFactory();
        } catch (NestedIOException e) {
            logger.error(e.getMessage(), e);
            System.exit(1);
        }
        return null;
    }

    @Override
    public void setMapperLocations(Resource[] mapperLocations) {
        super.setMapperLocations(mapperLocations);
    }
}
