package com.feihua.framework.image.self;

import com.feihua.framework.image.migration.sourcefrom.ApiSourceImpl;

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
