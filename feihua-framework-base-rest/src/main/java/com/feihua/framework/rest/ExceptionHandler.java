package com.feihua.framework.rest;


import com.feihua.exception.BaseException;
import com.feihua.utils.http.httpServletRequest.RequestUtils;
import com.feihua.utils.http.httpServletResponse.ResponseCode;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.session.UnknownSessionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * controller 异常处理类
 * Created by yw on 2016/1/13.
 */
@ControllerAdvice
public class ExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(ExceptionHandler.class);

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public ResponseEntity handleControllerException(Exception exception) {
        HttpServletRequest request = RequestUtils.getRequest();
        String reUrl = request.getRequestURI();
        logger.error(request.getMethod() + ":" + reUrl);
        //异常消息内容
        String msg = "";

        ResponseJsonRender responseJsonRender = new ResponseJsonRender();
        int httpcode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        String code = "0";
        try {
            throw exception;
        }
        catch (UnauthenticatedException ex) {
            msg = SecurityUtils.getSubject().isRemembered()? "remembered.login first please" :"login first please";
            httpcode = HttpServletResponse.SC_UNAUTHORIZED;
            code = ResponseCode.E401_100002.getCode();
        }
        catch (UnauthorizedException ex) {
            msg = "unauthorized";
            httpcode = HttpServletResponse.SC_FORBIDDEN;
            code = ResponseCode.E403_100001.getCode();
        } catch (BindException | MissingServletRequestParameterException ex) {
            msg = ResponseCode.E400_100000.getMsg();
            httpcode = HttpServletResponse.SC_NOT_IMPLEMENTED;
            code = ResponseCode.E400_100000.getCode();
            logger.error(ex.getMessage(), ex);
        }catch (HttpRequestMethodNotSupportedException e) {
            msg = "request method not support:" + e.getMethod();
            httpcode = HttpServletResponse.SC_METHOD_NOT_ALLOWED;
            code = ResponseCode.E405_100001.getCode();
        } catch (UnknownSessionException e) {
            msg = "unknown session error";
            code = ResponseCode.E500_100000.getCode();
            logger.error(e.getMessage(), e);
        } catch (BaseException e){
            msg = e.getMessage();
            code = e.getCode();
            if(e.isHttpException()){
                httpcode = e.getHttpStatus();
            }

            logger.error(exception.getMessage(), exception);
        } catch (Exception e) {
            msg = ResponseCode.E500_100000.getMsg();
            code = ResponseCode.E500_100000.getCode();
            logger.error(exception.getMessage(), exception);
        } finally {
            responseJsonRender.setMsg(msg);
            responseJsonRender.setCode(code);
        }
        return new ResponseEntity(responseJsonRender, HttpStatus.valueOf(httpcode));
    }
}
