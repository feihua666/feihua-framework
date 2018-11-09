package com.feihua.utils.http.httpServletResponse;

/**
 * Created by yangwei
 * Created at 2017/12/26 10:55
 */
public   enum ResponseCode {
    // 0
    success("0","success"),

    //204
    // 通用提示
    S204_100000("S204_100000","no content"),
    // 请求成功，但无内容
    S204_100001("S204_100001","no content"),

    // 400
    // 通用错误提示
    E400_100000("E400_100000","params is invalid"),
    // 帐号或密码错误
    E400_100001("E400_100001","account or password is invalid"),
    // 账号已被锁定
    E400_100002("E400_100002","account is locked"),
    // 创建token失败，参数不正确
    E400_100003("E400_100003","create login token error,params is invalid"),
    // 创建token失败，LoginType值不正确
    E400_100004("E400_100004","create login token error,loginType is invalid"),
    // 登录验证码不正确
    E400_100005("E400_100005","captcha is invalid"),


    // 401
    // 通用错误提示
    E401_100000("E401_100000","authenticate faild"),
    // 用户认证失败
    E401_100001("E401_100001","authenticate faild"),
    // 用户未认证，需要登录
    E401_100002("E401_100002","unauthenticated,login first please"),
    // 用户未登录，已在其它设置登录
    E401_100003("E401_100003","unauthenticated,logined on another device"),

    // 403
    // 通用错误提示
    E403_100000("E403_100000","no privilege"),
    // 禁止访问，无权限
    E403_100001("E403_100001","no privilege"),
    // 禁止访问，重复请求
    E403_100002("E403_100002","repeated request in a short time"),
    // 禁止删除，存在数据关联
    E403_100003("E403_100003","the data cannot be delete,ralative data may deal first"),
    // 禁止修改，可以用来提示数据可能因为某状态不能改动
    E403_100004("E403_100004","the data cannot be updated"),
    // 禁止操作，可以用来提示数据可能因某状态原因还没有达到操作条件
    E403_100005("E403_100005","the operation is forbidden"),

    // 404
    // 通用错误提示
    // 资源不存在，一般指请求失败，找不到url对应的地址也可以用来表示请求的数据不存在
    E404_100001("E404_100001","resource not exist"),

    // 405
    // 请求方法不支持
    E405_100001("E405_100001","request method not support"),

    // 409
    // 冲突,已存在
    E409_100001("E409_100001","resource conflict,already exist"),

    // 500
    // 通用异常代码
    E500_100000("E500_100000","system error");

    private String code;
    private String msg;

    ResponseCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
