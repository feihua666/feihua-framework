package com.feihua.exception;

/**
 * Created by yangwei
 * Created at 2018/1/26 15:56
 */
public class DataNotFoundException extends BaseException {
    public DataNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataNotFoundException(String message) {
        super(message);
    }

    public DataNotFoundException(String message, Object data) {
        super(message, data);
    }

    public DataNotFoundException(String message, String code) {
        super(message, code);
    }

    public DataNotFoundException(String message, String code,int httpStatus) {
        super(message, code,httpStatus);
    }
}
