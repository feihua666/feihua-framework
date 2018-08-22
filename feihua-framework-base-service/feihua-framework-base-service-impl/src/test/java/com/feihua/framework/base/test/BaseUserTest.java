package com.feihua.framework.base.test;

import com.feihua.framework.base.modules.user.api.ApiBaseUserPoService;
import com.feihua.framework.base.modules.user.dto.BaseUserDto;
import com.feihua.framework.base.modules.user.dto.SearchUsersConditionDsfDto;

import com.feihua.framework.base.mapper.BaseOfficePoMapper;

import com.feihua.framework.base.modules.office.po.BaseOfficePo;
import feihua.jdbc.api.pojo.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yangwei
 * Created at 2017/8/22 19:29
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "file:src/test/resources/applicationContext.xml"
})
public class BaseUserTest {


    @Autowired
    private BaseOfficePoMapper baseOfficePoMapper;
    @Autowired
    ApiBaseUserPoService apiBaseUserPoService;
    @Test
    public void testEmpty(){

    }
    //@Test
    public void Test1(){
        Map<String,Object> condition = new HashMap<>();
        //condition.put("parentId1","992142d7f44211e794174439c4325934");
        List<BaseOfficePo> list = baseOfficePoMapper.select(condition);

        Assert.assertEquals(list.size(),0);
    }
}
