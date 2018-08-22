package com.feihua.framework.rest.utils;

import feihua.jdbc.api.pojo.BasePo;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 通用工具类
 * Created by yangwei
 * Created at 2017/9/15 17:11
 */
public class Utils {
    /**
     * N 或 Y 转boolean
     * @param yesNo
     * @return
     */
    public static boolean toBoolean(String yesNo){
        return BooleanUtils.toBoolean(yesNo, BasePo.YesNo.Y.name(),BasePo.YesNo.N.name());
    }

    /**
     * 是否全部为空
     * @param str
     * @return
     */
    public static boolean isAllEmpty(String ...str){
        if(null == str) return true;
        for (String s : str) {
            if(StringUtils.isNotEmpty(s)){
                return false;
            }
        }
        return true;
    }
}
