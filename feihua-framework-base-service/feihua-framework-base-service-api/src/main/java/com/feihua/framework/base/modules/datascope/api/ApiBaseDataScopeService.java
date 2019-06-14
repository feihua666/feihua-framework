package com.feihua.framework.base.modules.datascope.api;

import com.feihua.exception.BaseException;

import java.util.List;

/**
 * 一个全局数据范围的通用接口
 * Created by yangwei
 * Created at 2018/3/20 15:05
 */
public interface ApiBaseDataScopeService<R> {


    /**
     * 根据用户id查询数据范围定义,以用户绑定的数据范围为优先,角色绑定的数据范围其次,岗位绑定的角色对应的数据范围再其次
     * @param userId
     * @return
     */
    public R selectDataScopeDefineByUserId(String userId,String roleId,String postId);

    /**
     * 判断是否设置为所有数据类型
     * @param dataScopeDefine
     * @return
     */
    public boolean isAllData(R dataScopeDefine);

}
