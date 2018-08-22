package com.feihua.exception;

/**
 * Created by yangwei
 * Created at 2018/1/26 15:38
 */
public class BaseException extends RuntimeException {
    private Object data;
    private String code = "0";
    private int httpStatus = 500;
    private boolean httpException = false;
    public BaseException(String message, Throwable cause) {
        super(message,cause);
    }
    public BaseException(String message) {
        super(message);
    }
    public BaseException(String message, Object data) {
        super(message);
        this.data = data;
    }
    /**
     * @param message 错误信息
     * @param code    错误代码
     */
    public BaseException(String message, String code) {
        super(message);
        this.code = code;
    }
    public BaseException(String message, String code,int httpStatus) {
        this(message,code);
        this.httpStatus = httpStatus;
        this.httpException = true;
    }
    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(int httpStatus) {
        this.httpStatus = httpStatus;
    }

    public boolean isHttpException() {
        return httpException;
    }

    public void setHttpException(boolean httpException) {
        this.httpException = httpException;
    }
}
