package com.feihua.framework.message.dto;

import com.feihua.framework.base.modules.loginclient.dto.BaseLoginClientDto;
import feihua.jdbc.api.pojo.BaseDto;

import java.util.List;

/**
 * 消息发送客户端参数，只支持虚拟客户端类型
 * 该参数所有属性均是虚拟客户端参数，属性前都加了v以区域是虚拟客户端
 * 虚拟客户端用来承载不能根据系统用户查找的情况，如：用户注册需要发送短信这时还没有系统用户；想直接发送三方消息比如直接发送微信公众号模板消息系统中没有对应用户时等等
 * 该参数每个目标虚拟客户端单独消费自己数据
 * Created by yangwei
 * Created at 2019/5/7 14:10
 */
public class BaseMessageSendVClientParamDto extends BaseDto {

    /**
     * 客户端用来推送三方消息
     */
    private BaseLoginClientDto vclient;

    /**
     * targetType 目标人类型，如office=机构下的人等，self=自定义人
     */
    private String vtargetType;

    /**
     * targetValues 自定义目标人的值，比如userId集合
     */
    private List<String> vtargetValues;

    public BaseLoginClientDto getVclient() {
        return vclient;
    }

    public void setVclient(BaseLoginClientDto vclient) {
        this.vclient = vclient;
    }

    public String getVtargetType() {
        return vtargetType;
    }

    public void setVtargetType(String vtargetType) {
        this.vtargetType = vtargetType;
    }

    public List<String> getVtargetValues() {
        return vtargetValues;
    }

    public void setVtargetValues(List<String> vtargetValues) {
        this.vtargetValues = vtargetValues;
    }
}
