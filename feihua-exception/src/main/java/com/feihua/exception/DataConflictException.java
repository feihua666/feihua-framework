package com.feihua.exception;

/**
 * Created by yangwei
 * Created at 2018/3/25 14:41
 */
public class DataConflictException extends BaseException {
    public DataConflictException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataConflictException(String message) {
        super(message);
    }

    public DataConflictException(String message, Object data) {
        super(message, data);
    }

    public DataConflictException(String message, String code) {
        super(message, code);
    }
    public DataConflictException(String message, String code,int httpStatus) {
        super(message, code,httpStatus);
    }
}
