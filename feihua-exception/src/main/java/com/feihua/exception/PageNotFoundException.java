package com.feihua.exception;

/**
 * Created by yangwei
 * Created at 2018/1/26 15:56
 */
public class PageNotFoundException extends BaseException {
    public PageNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public PageNotFoundException(String message) {
        super(message);
    }

    public PageNotFoundException(String message, Object data) {
        super(message, data);
    }

    public PageNotFoundException(String message, String code) {
        super(message, code);
    }

    public PageNotFoundException(String message, String code, int httpStatus) {
        super(message, code,httpStatus);
    }
}
