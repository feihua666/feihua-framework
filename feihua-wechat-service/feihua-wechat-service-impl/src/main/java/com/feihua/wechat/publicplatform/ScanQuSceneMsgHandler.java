package com.feihua.wechat.publicplatform;

import com.feihua.wechat.publicplatform.api.MsgTypeHandler;
import org.springframework.stereotype.Service;

/**
 * Created by yangwei
 * Created at 2018/7/20 11:42
 */

/**
 * 扫描带参数二维码事件处理
 */
@Service
public class ScanQuSceneMsgHandler implements MsgTypeHandler {

    public String handleMsg( String postXmlData, String which) {
        return null;
    }
}
