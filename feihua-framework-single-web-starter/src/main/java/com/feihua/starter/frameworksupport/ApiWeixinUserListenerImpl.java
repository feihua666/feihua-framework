package com.feihua.starter.frameworksupport;

import com.feihua.framework.base.modules.user.api.ApiBaseUserAuthPoService;
import com.feihua.framework.base.modules.user.api.ApiBaseUserPoService;
import com.feihua.framework.base.modules.user.dto.BaseUserAuthDto;
import com.feihua.framework.base.modules.user.dto.BaseUserDto;
import com.feihua.framework.base.modules.user.po.BaseUserAuthPo;
import com.feihua.framework.base.modules.user.po.BaseUserPo;
import com.feihua.framework.constants.DictEnum;
import com.feihua.framework.shiro.pojo.ShiroUser;
import com.feihua.wechat.CommonConstants;
import com.feihua.wechat.common.api.ApiWeixinUserListener;
import com.feihua.wechat.common.api.ApiWeixinUserPoService;
import com.feihua.wechat.common.dto.WeixinUserDto;
import com.feihua.wechat.common.po.WeixinUserPo;
import feihua.jdbc.api.pojo.BasePo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 实现用户发发现监听，这里不处理
 * Created by yangwei
 * Created at 2018/7/24 18:22
 */
@Service
public class ApiWeixinUserListenerImpl implements ApiWeixinUserListener{


    @Autowired
    private UserAuthHelper UserAuthHelper;
    @Override
    public void onAddWexinUser(WeixinUserDto weixinUserDto) {
        UserAuthHelper.generateUserAuth(weixinUserDto);
    }

}
