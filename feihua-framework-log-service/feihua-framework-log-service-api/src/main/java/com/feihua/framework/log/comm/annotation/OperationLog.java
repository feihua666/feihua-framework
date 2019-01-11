package com.feihua.framework.log.comm.annotation;

import com.feihua.framework.log.comm.LogType;

import java.lang.annotation.*;

/**
 * @Auther: wzn
 * @Date: 2019/1/10 13:51
 * @Description:
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperationLog {
    /**
     * 是否添加到数据库
     *
     * @return
     */
    boolean isInsert() default true;

    /**
     * 操作模块
     *
     * @return
     */
    String operation() default "";

    /**
     * 操作日志内容
     *
     * @return
     */
    String content() default "";

    /**
     * 日志类型
     *
     * @return
     */
    LogType type() default LogType.OPERATION;
}
