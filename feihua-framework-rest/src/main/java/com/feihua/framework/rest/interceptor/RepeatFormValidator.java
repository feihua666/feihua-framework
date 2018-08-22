package com.feihua.framework.rest.interceptor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 定义一个注释，加在controller方法上以开启防重复提交验证
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RepeatFormValidator {
    /**
     * 默认验证，可以指定不验证重复提交
     * @return
     */
    RepeatFormValidatorLogic value() default RepeatFormValidatorLogic.VALIDATE;
}
