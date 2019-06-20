package com.feihua.framework.base.modules.user.dto;

import feihua.jdbc.api.pojo.BaseUpdateParamDto;

import java.util.Date;

/**
 * Created by yangwei
 * Created at 2018/3/15 18:45
 */
public class BaseUserAccessLasttimeParamDto extends BaseUpdateParamDto {


    private String userId;

    private String userNickname;

    private String clientId;

    private String clientName;

    private String accessIp;

    private Date accessLasttime;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getAccessIp() {
        return accessIp;
    }

    public void setAccessIp(String accessIp) {
        this.accessIp = accessIp;
    }

    public Date getAccessLasttime() {
        return accessLasttime;
    }

    public void setAccessLasttime(Date accessLasttime) {
        this.accessLasttime = accessLasttime;
    }
}
