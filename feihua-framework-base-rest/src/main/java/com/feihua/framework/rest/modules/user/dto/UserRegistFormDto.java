package com.feihua.framework.rest.modules.user.dto;

/**
 * Created by yangwei
 * Created at 2019/1/7 15:35
 */
public class UserRegistFormDto {

    // 帐号信息
    private String account;
    // 密码信息
    private String password;
    // 基本信息
    private String serialNo;
    private String nickname;
    private String mobile;
    private String email;
    // 客户端编码，必填
    private String clientCode;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getClientCode() {
        return clientCode;
    }

    public void setClientCode(String clientCode) {
        this.clientCode = clientCode;
    }
}
