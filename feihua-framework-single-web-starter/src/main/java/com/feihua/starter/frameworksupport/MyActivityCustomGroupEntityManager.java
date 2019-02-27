package com.feihua.starter.frameworksupport;


import com.feihua.exception.BaseException;
import com.feihua.framework.activity.impl.ext.DefaultActivityCustomGroupEntityManager;
import com.feihua.framework.base.modules.role.api.ApiBaseRolePoService;
import com.feihua.framework.base.modules.role.dto.BaseRoleDto;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.GroupQuery;
import org.activiti.engine.impl.GroupQueryImpl;
import org.activiti.engine.impl.Page;
import org.activiti.engine.impl.persistence.entity.GroupEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by yw on 2017/2/4.
 */
@Service
public class MyActivityCustomGroupEntityManager extends DefaultActivityCustomGroupEntityManager {

    @Autowired
    private ApiBaseRolePoService apiBaseRolePoService;

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
        BaseRoleDto role = apiBaseRolePoService.selectByUserId(userId);

        if (role == null){
            return null;
        }
        GroupEntity groupEntity = new GroupEntity();
        groupEntity.setId(role.getId());
        groupEntity.setName(role.getName());
        groupEntity.setType(role.getType());
        groupEntity.setRevision(1);
        List<Group> list = new ArrayList<>();
        list.add(groupEntity);
        return list;
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
