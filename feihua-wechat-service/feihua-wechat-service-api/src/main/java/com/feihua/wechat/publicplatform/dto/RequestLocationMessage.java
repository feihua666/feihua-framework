package com.feihua.wechat.publicplatform.dto;




/**
 * 用户地理位置
 * Created by yw on 2016/3/14.
 */
public class RequestLocationMessage extends RequestMessage {

    private String latitude;

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getPrecision() {
        return precision;
    }

    public void setPrecision(String precision) {
        this.precision = precision;
    }

    private String longitude;
    private String precision;

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
}
