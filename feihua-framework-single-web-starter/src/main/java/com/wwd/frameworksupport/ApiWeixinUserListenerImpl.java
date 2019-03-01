package com.wwd.frameworksupport;

import com.feihua.framework.constants.DictEnum;
import com.feihua.framework.shiro.pojo.ShiroUser;
import com.feihua.wechat.common.api.ApiWeixinUserListener;
import com.feihua.wechat.common.dto.WeixinUserDto;
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
        if(DictEnum.WeixinType.publicplatform.name().equals(weixinUserDto.getType())){
            // 如果是网页授权，先不插入用户认证信息，邀请wwd用户的时候再添加
            return;

        }

        UserAuthHelper.generateUserAuth(weixinUserDto);
    }

}
