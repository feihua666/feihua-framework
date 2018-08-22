package com.feihua.wechat.miniprogram.dto;

import com.feihua.wechat.ParamsDto;

/**
 * 小程序参数基类
 * Created by yangwei
 * Created at 2018/6/26 14:30
 */
public class MiniParamsDto extends ParamsDto {
    /**
     * 类型，可以标识是哪个小程序
     */
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
