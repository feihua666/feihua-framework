package com.feihua.framework.activity.test;

import com.feihua.framework.activity.user.MyActivityCustomGroupEntityManager;
import org.activiti.engine.identity.Group;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by yangwei
 * Created at 2018/7/30 10:03
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "file:src/test/resources/applicationContext.xml"
})
public class Test {
    @Autowired
    private MyActivityCustomGroupEntityManager  myActivityCustomGroupEntityManager;

    @org.junit.Test
    public void test1(){

        String userid = "f1c09a573e1e11e8a4ba4439c4325934";
        List<Group> list = myActivityCustomGroupEntityManager.findGroupsByUser(userid);
        Assert.assertEquals(list.size(),1l);
    }
}
