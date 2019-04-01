package com.feihua.wechat.publicplatform;

import com.feihua.wechat.publicplatform.api.MsgTypeHandler;
import org.springframework.stereotype.Service;

/**
 * Created by yangwei
 * Created at 2018/7/20 11:42
 */

/**
 * 扫描带参数二维码事件处理，事件来源分两种，一种是未关注，一种是已关注，参数不同，请注意
 */
@Service("default_wx_public_event_SCAN")
public class ScanQuSceneMsgHandler implements MsgTypeHandler {

    public String handleMsg( String postXmlData, String which) {
        return null;
    }
}
