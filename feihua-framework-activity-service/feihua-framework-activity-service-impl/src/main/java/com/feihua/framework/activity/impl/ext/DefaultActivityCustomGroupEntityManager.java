package com.feihua.framework.activity.impl.ext;


import com.feihua.exception.BaseException;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.GroupQuery;
import org.activiti.engine.impl.GroupQueryImpl;
import org.activiti.engine.impl.Page;
import org.activiti.engine.impl.persistence.entity.GroupEntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by yw on 2017/2/4.
 */

public class DefaultActivityCustomGroupEntityManager extends GroupEntityManager {
    
    @Override
    public void insertGroup(Group group) {
        throw new BaseException("not implement method.you can extends this class and then inject into spring");
    }

    @Override
    public void updateGroup(Group updatedGroup) {
        throw new BaseException("not implement method.you can extends this class and then inject into spring");
    }

    @Override
    public void deleteGroup(String groupId) {
        throw new BaseException("not implement method.you can extends this class and then inject into spring");
    }

    @Override
    public GroupQuery createNewGroupQuery() {
        throw new BaseException("not implement method.you can extends this class and then inject into spring");
    }

    @Override
    public List<Group> findGroupByQueryCriteria(GroupQueryImpl query, Page page) {
        throw new BaseException("not implement method.you can extends this class and then inject into spring");
    }

    @Override
    public long findGroupCountByQueryCriteria(GroupQueryImpl query) {
        throw new BaseException("not implement method.you can extends this class and then inject into spring");
    }

    @Override
    public List<Group> findGroupsByUser(String userId) {
        throw new BaseException("not implement method.you can extends this class and then inject into spring");
    }

    @Override
    public List<Group> findGroupsByNativeQuery(Map<String, Object> parameterMap, int firstResult, int maxResults) {
        throw new BaseException("not implement method.you can extends this class and then inject into spring");
    }

    @Override
    public long findGroupCountByNativeQuery(Map<String, Object> parameterMap) {
        throw new BaseException("not implement method.you can extends this class and then inject into spring");
    }

    @Override
    public boolean isNewGroup(Group group) {
        return super.isNewGroup(group);
    }
}
