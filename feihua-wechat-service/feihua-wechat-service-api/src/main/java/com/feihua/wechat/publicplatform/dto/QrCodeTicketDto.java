package com.feihua.wechat.publicplatform.dto;

import java.io.Serializable;

/**
 * Created by yangwei
 * Created at 2019/5/17 10:01
 */
public class QrCodeTicketDto implements Serializable {
    private String ticket;
    private String url;
    private Integer expireSeconds;

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getExpireSeconds() {
        return expireSeconds;
    }

    public void setExpireSeconds(Integer expireSeconds) {
        this.expireSeconds = expireSeconds;
    }
}
