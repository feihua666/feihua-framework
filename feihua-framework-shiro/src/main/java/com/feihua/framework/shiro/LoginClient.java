package com.feihua.framework.shiro;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * Created by yangwei
 * Created at 2018/11/7 11:17
 */
public class LoginClient implements Serializable{
    /**
     * 客户端
     */
    private String clientType;
    /**
     * 子客户端
     */
    private String subClientType;


    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }


    public String getSubClientType() {
        return subClientType;
    }

    public void setSubClientType(String subClientType) {
        this.subClientType = subClientType;
    }

    @Override
    public boolean equals(Object o) {
        if(o == null) return false;
        LoginClient loginClient = ((LoginClient) o);
        if (this.clientType != null && this.clientType.equals(loginClient.getClientType())){
            if (StringUtils.isEmpty(this.subClientType) && StringUtils.isEmpty(loginClient.getSubClientType())){
                return true;
            }
            return StringUtils.equals(loginClient.getSubClientType(),this.getSubClientType());
        }
        return false;
    }

}
