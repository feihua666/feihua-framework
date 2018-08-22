package com.feihua.framework.shiro;

import com.feihua.framework.shiro.pojo.PasswordAndSalt;
import com.feihua.framework.shiro.pojo.ShiroUser;
import com.feihua.framework.shiro.pojo.token.AccountPasswordToken;
import com.feihua.framework.shiro.utils.ShiroUtils;
import com.feihua.utils.encode.EncodeUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.util.ByteSource;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by yangwei
 * Created at 2017/7/26 16:18
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "file:src/main/resources/applicationContext-shiro.xml",
        "file:src/test/resources/applicationContext-shiro-test.xml"
})
public class Test {

    @Autowired
    private SecurityManager securityManager;

    @org.junit.Test
    public void testLogin(){
        AccountPasswordToken token = new AccountPasswordToken("1","123456");
        token.setPassword("123456".toCharArray());
        token.setAccount("1");
        SecurityUtils.setSecurityManager(securityManager);
        SecurityUtils.getSubject().login(token);

        ShiroUser u = ShiroUtils.getCurrentUser();

        Assert.assertEquals(u.getId(),"1");
    }
    @org.junit.Test
    public void testPasswordMatcher(){
        String oldPassword = "123456";
        AccountPasswordToken token = new AccountPasswordToken("1","123456");

        PasswordAndSalt passwordAndSalt = PasswordAndSalt.entryptPassword("123456");
        byte[] salt = EncodeUtils.decodeHex(passwordAndSalt.getSalt());
        AuthenticationInfo info = new SimpleAuthenticationInfo("1",
                passwordAndSalt.getPassword(), ByteSource.Util.bytes(salt), "realm");

        Assert.assertTrue(PasswordAndSalt.getCredentialsMatcher().doCredentialsMatch(token,info));

    }
}
