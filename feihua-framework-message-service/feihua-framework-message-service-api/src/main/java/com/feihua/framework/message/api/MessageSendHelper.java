package com.feihua.framework.message.api;


import com.feihua.framework.message.dto.MessageSendForUserParamsDto;

/**
 * Created by yangwei
 * Created at 2019/5/14 15:53
 */
public interface MessageSendHelper {

    void messageSendForUser(MessageSendForUserParamsDto paramsDto);
}
