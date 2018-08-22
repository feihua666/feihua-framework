package com.feihua.utils.string;

/**
 * 收集一些正则表达式用
 * Created by yangwei
 * Created at 2018/1/29 20:31
 */
public enum  RegularExpression {
    /**
     * 手机号
     * reg (13|14|15|18|17)[0-9]{9}
     */
    mobile("(13|14|15|18|17)[0-9]{9}"),
    /**
     * 邮件地址
     * reg [\w!#$%&'*+/=?^_`{|}~-]+(?:\.[\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\w](?:[\w-]*[\w])?\.)+[\w](?:[\w-]*[\w])?
     */
    email("[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?"),
    /**
     * 网址url
     * reg [a-zA-z]+://[^\s]*
     */
    url("[a-zA-z]+://[^\\s]*"),
    /**
     * 中国邮政编码
     * reg [1-9]\d{5}(?!\d)
     */
    postalcode_china("[0-9]\\d{5}(?!\\d)"),
    /**
     * 中文字符
     * reg [\u4e00-\u9fa5]
     */
    chineseCharacters("[\\u4e00-\\u9fa5]"),
    /**
     * 正整数
     * reg ^[1-9]\d*$
     */
    positiveInteger("^[1-9]\\d*$"),
    /**
     * 负整数
     * reg ^-[1-9]\d*$
     */
    negativeInteger("^-[1-9]\\d*$"),
    /**
     * 整数
     * reg ^-?[1-9]\d*$
     */
    integer("^-?[1-9]\\d*$"),
    /**
     * 非负整数(正整数加一个零)
     * reg ^[1-9]\d*|0$
     */
    nonnegativeInteger("^[1-9]\\d*|0$"),
    /**
     * 非正整数(负整数加一个零)
     * reg ^-[1-9]\d*|0$
     */
    nonPositiveInteger("^-[1-9]\\d*|0$"),
    /**
     * 12月
     * reg ^(0?[1-9]|1[0-2])$
     */
    month_12("^(0?[1-9]|1[0-2])$"),
    /**
     * 31天
     * reg ^((0?[1-9])|((1|2)[0-9])|30|31)$
     */
    day_31("^((0?[1-9])|((1|2)[0-9])|30|31)$"),
    /**
     * 保留两位小数(大于零的小数)
     * reg ^([1-9]+|0)(.[0-9]{2})$
     */
    decimal_2("^([1-9]+|0)(.[0-9]{2})$");



    private String reg;
    RegularExpression(String reg) {
        this.reg = reg;
    }

    public String getReg() {
        return reg;
    }
}
