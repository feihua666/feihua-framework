package com.feihua.framework.base.modules.user.api;

import com.feihua.framework.base.modules.user.dto.BaseUserAuthDto;
import com.feihua.framework.base.modules.user.dto.BaseUserDto;
import com.feihua.framework.base.modules.user.po.BaseUserPo;

/**
 * Created by yangwei
 * Created at 2019/1/7 15:33
 */
public interface ApiBaseUserListener {
    /**
     * 注册成功监听
     * @param userDto
     * @param userAuthDto
     */
    public void onRegistSuccess(BaseUserDto userDto, BaseUserAuthDto userAuthDto);
}
