package com.feihua.framework.activity.impl.ext;

import org.activiti.engine.impl.interceptor.Session;
import org.activiti.engine.impl.interceptor.SessionFactory;
import org.activiti.engine.impl.persistence.entity.GroupEntityManager;
import org.activiti.engine.impl.persistence.entity.GroupIdentityManager;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by yw on 2017/2/4.
 */
public class ActivityCustomGroupEntityManagerFactory implements SessionFactory {


    @Autowired
    private GroupEntityManager customGroupEntityManager;

    @Override
    public Class<?> getSessionType() {
        //注意此处必须为Activiti原生的类，否则自定义类不会生效
        return GroupIdentityManager.class;
    }

    @Override
    public Session openSession() {
        return customGroupEntityManager;
    }
}
