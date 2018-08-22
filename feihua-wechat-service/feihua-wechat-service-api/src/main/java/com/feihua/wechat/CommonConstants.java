package com.feihua.wechat;

import com.feihua.framework.constants.DictEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * 通用常量
 * Created by yangwei
 * Created at 2018/4/27 18:12
 */
public class CommonConstants {

    public final static String PARAM_APPID = "APPID";
    public final static String PARAM_APPSECRET = "APPSECRET";

    public static Map<String,String> genderMapping = new HashMap<String, String>();
    static {
        genderMapping.put("0", DictEnum.Gender.unknown.name());
        genderMapping.put("1", DictEnum.Gender.male.name());
        genderMapping.put("2", DictEnum.Gender.female.name());
    }
}
