package com.feihua.framework.shiro.pojo;

import com.feihua.utils.digest.DigestUtils;
import com.feihua.utils.encode.EncodeUtils;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;

/**
 * 加密后的密码字符串和盐
 * Created by yw on 2016/1/13.
 */
public class PasswordAndSalt {
    public static final String HASH_ALGORITHM = "SHA-1";
    public static final int HASH_INTERATIONS = 1024;
    public static final int SALT_SIZE = 8;



    /**
     * 加密密码
     *
     * @param plainPassword
     * @return
     */
    public static PasswordAndSalt entryptPassword(String plainPassword) {
        PasswordAndSalt ps = new PasswordAndSalt();
        byte[] salt = DigestUtils.generateSalt(SALT_SIZE);
        ps.setSalt(EncodeUtils.encodeHex(salt));

        byte[] hashPassword = DigestUtils.sha1(plainPassword.getBytes(), salt, HASH_INTERATIONS);
        ps.setPassword(EncodeUtils.encodeHex(hashPassword));
        return ps;
    }
    /**
     * 生成密码校验器
     *
     * @return
     */
    public static HashedCredentialsMatcher getCredentialsMatcher() {
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(HASH_ALGORITHM);
        matcher.setHashIterations(HASH_INTERATIONS);

        return matcher;
    }

    private String password;
    private String salt;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
    @Override
    public String toString() {
        return password + "," + salt;
    }
}
