package com.feihua.exception;

/**
 * 参数不正确异常
 * Created by yangwei
 * Created at 2018年12月7日 14:33:52
 */
public class ParamInvalidException extends BaseException {
    public ParamInvalidException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParamInvalidException(String message) {
        super(message);
    }

    public ParamInvalidException(String message, Object data) {
        super(message, data);
    }

    public ParamInvalidException(String message, String code) {
        super(message, code);
    }
    public ParamInvalidException(String message, String code, int httpStatus) {
        super(message, code,httpStatus);
    }
}
