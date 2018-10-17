package com.feihua.framework.image.self;

import com.feihua.framework.image.migration.Config;
import com.feihua.framework.image.migration.source.ApiSourceImpl;
import com.feihua.framework.mybatis.orm.MultipleDataSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * Created by yangwei
 * Created at 2018/10/9 13:14
 */
public class ApiSourceSelfImpl  extends ApiSourceImpl{

    @Override
    public String getPrefix(boolean isText) {
        if (isText) {
            return "http://staticimg-source.test.com";
        }
        return "";
    }
}
