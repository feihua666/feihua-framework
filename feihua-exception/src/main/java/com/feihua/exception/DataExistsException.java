package com.feihua.exception;

/**
 * Created by yangwei
 * Created at 2018/1/26 15:58
 */
public class DataExistsException extends BaseException {
    public DataExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataExistsException(String message) {
        super(message);
    }

    public DataExistsException(String message, Object data) {
        super(message, data);
    }

    public DataExistsException(String message, String code) {
        super(message, code);
    }
    public DataExistsException(String message, String code,int httpStatus) {
        super(message, code,httpStatus);
    }
}
