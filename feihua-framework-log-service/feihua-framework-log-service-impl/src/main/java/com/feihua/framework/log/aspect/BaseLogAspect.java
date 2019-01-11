package com.feihua.framework.log.aspect;

import com.feihua.framework.log.comm.annotation.OperationLog;
import com.feihua.framework.log.service.api.ApiBaseLogPoService;
import com.feihua.framework.log.service.po.BaseLogPo;
import com.feihua.framework.shiro.pojo.ShiroUser;
import com.feihua.framework.shiro.utils.ShiroUtils;
import com.feihua.utils.exception.ExceptionsUtils;
import com.feihua.utils.http.IPUtils;
import com.feihua.utils.http.httpServletRequest.RequestUtils;
import com.feihua.utils.json.JSONUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * @Auther: wzn
 * @Date: 2019/1/10 14:19
 * @Description:
 */
@Aspect
public class BaseLogAspect {
    private static Logger logger = LoggerFactory.getLogger(BaseLogAspect.class);

    @Autowired
    private ApiBaseLogPoService apiBaseLogPoService;

    @Pointcut("@annotation(com.feihua.framework.log.comm.annotation.OperationLog)")
    public void logPointCut() {
    }

    /**
     * 前置方法，在目标方法的执行之前执行，即在连接点之前进行执行。
     *
     * @param point
     */
    @Before("logPointCut()")
    public void before(JoinPoint point) {
        BaseLogPo logPo = getBaseLogPo(point);
        logger.info("===》before 调用前连接点方法参数为：{}", logPo.toString());
    }

    private BaseLogPo getBaseLogPo(JoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();

        BaseLogPo logPo = new BaseLogPo();
        OperationLog baseLog = method.getAnnotation(OperationLog.class);
        if (baseLog != null) {
            //注解上的描述
            logPo.setOperation(baseLog.operation());
            logPo.setContent(baseLog.content());
            logPo.setType(baseLog.type().name());
        }
        //请求的方法名
        String className = point.getTarget().getClass().getName();
        String methodName = point.getSignature().getName();
        logPo.setMethod(className + methodName);
        List<Object> args = Arrays.asList(point.getArgs());

        String params = null;
        try {
            params = JSONUtils.obj2json(args);
            logPo.setParams(params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return logPo;
    }

    /**
     * 后置方法在连接点方法完成之后执行，无论连接点方法执行成功还是出现异常，都将执行后置方法
     *
     * @param point
     */
    @After("logPointCut()")
    public void after(JoinPoint point) {
        BaseLogPo logPo = getBaseLogPo(point);
        logger.info("===》after 调用后连接点方法参数为：{}", logPo.toString());
    }


    /**
     * 异常通知方法只在连接点方法出现异常后才会执行，否则不执行。在异常通知方法中可以获取连接点方法出现的异常。在切面类中异常通知方法
     * 通过throwing属性指定连接点方法出现异常信息存储在ex变量中，在异常通知方法中就可以从ex变量中获取异常信息了
     *
     * @param point
     * @param e
     */
    @AfterThrowing(pointcut = "logPointCut()", throwing = "e")
    public void afterThrowing(JoinPoint point, Throwable e) {
        String methodName = point.getSignature().getName();
        List<Object> args = Arrays.asList(point.getArgs());
        logger.info("===========afterThrowing连接点方法为：" + methodName + ",参数为：" + args + ",异常为：" + e);
    }

    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {

        long beginTime = System.currentTimeMillis();
        //执行方法
        Object result = point.proceed();
        //执行时长(毫秒)
        long time = System.currentTimeMillis() - beginTime;

        //保存日志
        try {
            saveBaseLog(point, time);
        } catch (Exception e) {
            logger.error("日志处理异常", e);
        }

        return result;
    }

    private void saveBaseLog(ProceedingJoinPoint joinPoint, long time) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        BaseLogPo logPo = new BaseLogPo();
        OperationLog baseLog = method.getAnnotation(OperationLog.class);
        if (baseLog != null) {
            //注解上的描述
            logPo.setOperation(baseLog.operation());
            logPo.setContent(baseLog.content());
            logPo.setType(baseLog.type().name());
        }

        //请求的方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        logPo.setMethod(className + "." + methodName + "()");
        //获取request
        HttpServletRequest request = RequestUtils.getRequest();
        //设置IP地址
        logPo.setIp(IPUtils.getIp(request));

        //用户名
        final ShiroUser currentUser = ShiroUtils.getCurrentUser();

        String username = currentUser.getNickname();
        logPo.setNickname(username);

        //请求的参数
        Object[] args = joinPoint.getArgs();
        try {
            if (args[0] instanceof Throwable) {
                Throwable e = (Throwable) args[0];
                logPo.setParams(ExceptionsUtils.getStackTraceAsString(e));
            } else {
                String params = JSONUtils.obj2json(Arrays.asList(args));
                logPo.setParams(params);
            }
        } catch (Exception e) {
            logger.error("日志转换参数失败", e);
        }



        logPo.setTime(time);
        logger.info("====》Aspect Log : {}", logPo.toString());
        //保存系统日志
        if (baseLog != null && baseLog.isInsert()) {
            apiBaseLogPoService.preInsert(logPo, currentUser.getId());
            apiBaseLogPoService.insert(logPo);
        }
    }
}
