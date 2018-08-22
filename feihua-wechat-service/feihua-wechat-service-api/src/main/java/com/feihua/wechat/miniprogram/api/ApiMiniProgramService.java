package com.feihua.wechat.miniprogram.api;

import com.feihua.wechat.ParamsDto;
import com.feihua.wechat.miniprogram.dto.LoginCredentialsDto;

/**
 * 小程序api
 * Created by yangwei
 * Created at 2018/4/27 18:21
 */
public interface ApiMiniProgramService {


    /**
     * 处理微信平台发来的消息
     * @param postXmlData
     * @param which
     * @return
     */
    public String processXmlMsg(String postXmlData, final String which);

    /**
     * 处理微信平台发来的消息
     * @param postJsonData
     * @param which
     * @return
     */
    public String processjsonMsg(String postJsonData, final String which);
    /**
     * 根据临时登录凭证获取登录凭证
     * @param code 临时登录凭证
     * @return
     */
    public LoginCredentialsDto fetchLoginCredentials(String code,ParamsDto paramsDto);


}
