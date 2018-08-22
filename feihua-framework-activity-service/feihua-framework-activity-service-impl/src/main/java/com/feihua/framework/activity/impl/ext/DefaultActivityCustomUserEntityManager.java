package com.feihua.framework.activity.impl.ext;

import com.feihua.exception.BaseException;
import com.feihua.framework.activity.ActUtils;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.identity.UserQuery;
import org.activiti.engine.impl.Page;
import org.activiti.engine.impl.UserQueryImpl;
import org.activiti.engine.impl.persistence.entity.GroupEntityManager;
import org.activiti.engine.impl.persistence.entity.IdentityInfoEntity;
import org.activiti.engine.impl.persistence.entity.UserEntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by yw on 2017/2/4.
 */

public class DefaultActivityCustomUserEntityManager extends UserEntityManager {

    @Autowired
    private GroupEntityManager customGroupEntityManager;
    
    @Override
    public void insertUser(User user) {
        throw new BaseException("not implement method.you can extends this class and then inject into spring");
    }

    @Override
    public void updateUser(User updatedUser) {
        throw new BaseException("not implement method.you can extends this class and then inject into spring");
    }

    @Override
    public User findUserById(String userId) {
        throw new BaseException("not implement method.you can extends this class and then inject into spring");
    }

    @Override
    public void deleteUser(String userId) {
        throw new BaseException("not implement method.you can extends this class and then inject into spring");
    }

    @Override
    public List<User> findUserByQueryCriteria(UserQueryImpl query, Page page) {
        throw new BaseException("not implement method.you can extends this class and then inject into spring");
    }

    @Override
    public long findUserCountByQueryCriteria(UserQueryImpl query) {
        throw new BaseException("not implement method.you can extends this class and then inject into spring");
    }

    @Override
    public List<Group> findGroupsByUser(String userId) {
        return customGroupEntityManager.findGroupsByUser(userId);
    }

    @Override
    public UserQuery createNewUserQuery() {
        throw new BaseException("not implement method.you can extends this class and then inject into spring");
    }

    @Override
    public IdentityInfoEntity findUserInfoByUserIdAndKey(String userId, String key) {
        throw new BaseException("not implement method.you can extends this class and then inject into spring");
    }

    @Override
    public List<String> findUserInfoKeysByUserIdAndType(String userId, String type) {
        throw new BaseException("not implement method.you can extends this class and then inject into spring");
    }

    @Override
    public Boolean checkPassword(String userId, String password) {
        throw new BaseException("not implement method.you can extends this class and then inject into spring");
    }

    @Override
    public List<User> findPotentialStarterUsers(String proceDefId) {
        return super.findPotentialStarterUsers(proceDefId);
    }

    @Override
    public List<User> findUsersByNativeQuery(Map<String, Object> parameterMap, int firstResult, int maxResults) {
        throw new BaseException("not implement method.you can extends this class and then inject into spring");
    }

    @Override
    public long findUserCountByNativeQuery(Map<String, Object> parameterMap) {
        throw new BaseException("not implement method.you can extends this class and then inject into spring");
    }

}
