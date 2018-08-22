package com.feihua.framework.jedis.test;


import com.feihua.framework.jedis.utils.JedisUtils;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by yangwei
 * Created at 2017/8/23 17:33
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "file:src/test/resources/applicationContext.xml"
})
public class Test {
    @org.junit.Test
    public void test(){
        JedisUtils.set("jedisTestKey","mydata",0);

        Assert.assertEquals("mydata",JedisUtils.get("jedisTestKey"));
    }
}
