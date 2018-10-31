package com.feihua.wechat.common.dto;

/**
 * @Auther: wzn
 * @Date: 2018/10/30 15:18
 * @Description: 微信模板消息
 */
public class WxTemplate {
    /**
     * 模板ID
     */
    private String template_id;
    /**
     * 模板标题
     */
    private String title;
    /**
     * 模板所属行业的一级行业
     */
    private String primary_industry;
    /**
     * 模板所属行业的二级行业
     */
    private String deputy_industry;
    /**
     * 模板内容
     */
    private String content;
    /**
     * 模板示例
     */
    private String example;

    public WxTemplate() {
    }

    /**
     * {
     * "template_list": [{
     * "template_id": "iPk5sOIt5X_flOVKn5GrTFpncEYTojx6ddbt8WYoV5s",
     * "title": "领取奖金提醒",
     * "primary_industry": "IT科技",
     * "deputy_industry": "互联网|电子商务",
     * "content": "{ {result.DATA} }\n\n领奖金额:{ {withdrawMoney.DATA} }\n领奖  时间:{ {withdrawTime.DATA} }\n银行信息:{ {cardInfo.DATA} }\n到账时间:  { {arrivedTime.DATA} }\n{ {remark.DATA} }",
     * "example": "您已提交领奖申请\n\n领奖金额：xxxx元\n领奖时间：2013-10-10 12:22:22\n银行信息：xx银行(尾号xxxx)\n到账时间：预计xxxxxxx\n\n预计将于xxxx到达您的银行卡"
     * }]
     * }
     *
     * @param template_id
     * @param title
     * @param primary_industry
     * @param deputy_industry
     * @param content
     * @param example
     */
    public WxTemplate(String template_id, String title, String primary_industry, String deputy_industry, String content, String example) {
        this.template_id = template_id;
        this.title = title;
        this.primary_industry = primary_industry;
        this.deputy_industry = deputy_industry;
        this.content = content;
        this.example = example;
    }

    public String getTemplate_id() {
        return template_id;
    }

    public void setTemplate_id(String template_id) {
        this.template_id = template_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrimary_industry() {
        return primary_industry;
    }

    public void setPrimary_industry(String primary_industry) {
        this.primary_industry = primary_industry;
    }

    public String getDeputy_industry() {
        return deputy_industry;
    }

    public void setDeputy_industry(String deputy_industry) {
        this.deputy_industry = deputy_industry;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    @Override
    public String toString() {
        return "WxTemplate{" +
                "template_id='" + template_id + '\'' +
                ", title='" + title + '\'' +
                ", primary_industry='" + primary_industry + '\'' +
                ", deputy_industry='" + deputy_industry + '\'' +
                ", content='" + content + '\'' +
                ", example='" + example + '\'' +
                '}';
    }
}
