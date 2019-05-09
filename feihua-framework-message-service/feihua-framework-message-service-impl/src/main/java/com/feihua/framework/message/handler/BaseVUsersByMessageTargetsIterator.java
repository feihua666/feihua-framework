package com.feihua.framework.message.handler;

import com.feihua.framework.base.modules.rel.api.ApiBaseUserRoleRelPoService;
import com.feihua.framework.base.modules.rel.dto.BaseUserRoleRelDto;
import com.feihua.framework.base.modules.user.api.ApiBaseUserPoService;
import com.feihua.framework.base.modules.user.dto.SearchUsersConditionDsfDto;
import com.feihua.framework.base.modules.user.po.BaseUserPo;
import com.feihua.framework.constants.DictEnum;
import com.feihua.utils.collection.CollectionUtils;
import com.feihua.utils.spring.SpringContextHolder;
import feihua.jdbc.api.pojo.PageAndOrderbyParamDto;
import feihua.jdbc.api.pojo.PageResultDto;
import feihua.jdbc.api.service.impl.AbstractPageIteratorImpl;
import feihua.jdbc.api.utils.PageUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangwei
 * Created at 2018/11/1 20:49
 */
public class BaseVUsersByMessageTargetsIterator extends AbstractPageIteratorImpl<String> {

    private String targetType;
    private List<String> targetValues;

    public BaseVUsersByMessageTargetsIterator(int pageNo, int pageSize, String targetType, List<String> targetValues){
        super(pageNo,pageSize);
        this.targetType = targetType;
        this.targetValues = targetValues;
    }

    @Override
    public List<String> next() {
        if(targetValues != null && !targetValues.isEmpty()) {
            int start = PageUtils.getFirstOffset(this.getPage());

            int end = start + this.getPage().getPageSize();
            try {
                List<String> r = CollectionUtils.subList(start,end ,targetValues);
                super.pageNoPlus(getPage());
                return r;
            }catch (Exception e){}
        }


        return null;
    }
}
