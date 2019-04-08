package com.feihua.framework.shiro.pojo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 当前登录用户
 * Created by yangwei on 2015/8/19.
 */

public class ShiroUser implements Serializable{
    private static final long serialVersionUID = 1L;

    private static final String role_key = "role";
    private static final String office_key = "office";
    private static final String area_key = "area";

    public ShiroUser(){}

    /**
     * 用户的id
     */
    private String id;

    /**
     * 用户的帐号
     */
    private String account;

    /**
     * 用户的编号
     */
    private String serialNo;

    /**
     * 用户的昵称
     */
    private String nickname;

    /**
     * 用户的邮箱
     */
    private String email;
    /**
     * 用户的性别
     */
    private String gender;

    /**
     * 用户的头像
     */
    private String photo;

    /**
     * 用户的手机号
     */
    private String mobile;

    /**
     * 用户的QQ
     */
    private String qq;

    /**
     * 用户的来源
     */
    private String source;

    /**
     * 是否锁定
     */
    private boolean locked;


    private boolean superAdmin = false;

    /**
     * 用户登录方式
     */
    private String loginType;

    private String loginClient;

    /**
     * 用户登录IP
     */
    private String host;

    /**
     * 附加信息
     */
    private Map<String,Object> additionalAttr = new HashMap<>();

    public void setRole(Object role){
        additionalAttr.put(role_key,role);
    }
    public Object getRole(){
        return additionalAttr.get(role_key);
    }
    public void setOffice(Object office){
        additionalAttr.put(office_key,office);
    }
    public Object getOffice(){
        return additionalAttr.get(office_key);
    }
    public void setArea(Object area){
        additionalAttr.put(area_key,area);
    }
    public Object getArea(){
        return additionalAttr.get(area_key);
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public boolean isSuperAdmin() {
        return superAdmin;
    }

    public void setSuperAdmin(boolean superAdmin) {
        this.superAdmin = superAdmin;
    }

    public Map<String, Object> getAdditionalAttr() {
        return additionalAttr;
    }

    public void setAdditionalAttr(Map<String, Object> additionalAttr) {
        this.additionalAttr = additionalAttr;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getLoginClient() {
        return loginClient;
    }

    public void setLoginClient(String loginClient) {
        this.loginClient = loginClient;
    }
}
