package com.feihua.framework.rest.interceptor;

/**
 * 表单重复提交逻辑
 * Created by yangwei
 * Created at 2017/8/21 11:59
 */
public enum  RepeatFormValidatorLogic {
    /**
     * 默认验证
     */
    VALIDATE,
    /**
     * 指定ignore不验证表单重复提交
     */
    IGNORE
}
