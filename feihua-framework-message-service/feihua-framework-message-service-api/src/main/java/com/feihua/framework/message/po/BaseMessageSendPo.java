package com.feihua.framework.message.po;

import feihua.jdbc.api.pojo.BasePo;

/**
 * This class was generated by MyBatis Generator.
 * @author yangwei 2019-05-07 14:28:52
 * Database Table Remarks:
 *   消息发送表,记录消息的发送参数
 *
 * This class corresponds to the database table base_message_send
 * @mbg.generated do_not_delete_during_merge 2019-05-07 14:28:52
*/
public class BaseMessageSendPo extends feihua.jdbc.api.pojo.BasePo<String> {
    /**
     * Database Column Remarks:
     *   消息id
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column base_message_send.MESSAGE_ID
     *
     * @mbg.generated 2019-05-07 14:28:52
     */
    private String messageId;

    /**
     * Database Column Remarks:
     *   发送人id
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column base_message_send.SEND_USER_ID
     *
     * @mbg.generated 2019-05-07 14:28:52
     */
    private String sendUserId;

    /**
     * Database Column Remarks:
     *   发送人昵称
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column base_message_send.SEND_USER_NICKNAME
     *
     * @mbg.generated 2019-05-07 14:28:52
     */
    private String sendUserNickname;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getSendUserId() {
        return sendUserId;
    }

    public void setSendUserId(String sendUserId) {
        this.sendUserId = sendUserId;
    }

    public String getSendUserNickname() {
        return sendUserNickname;
    }

    public void setSendUserNickname(String sendUserNickname) {
        this.sendUserNickname = sendUserNickname;
    }

    public com.feihua.framework.message.api.ApiBaseMessageSendPoService service() {
        return com.feihua.utils.spring.SpringContextHolder.getBean(com.feihua.framework.message.api.ApiBaseMessageSendPoService.class);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", messageId=").append(messageId);
        sb.append(", sendUserId=").append(sendUserId);
        sb.append(", sendUserNickname=").append(sendUserNickname);
        sb.append("]");
        return sb.toString();
    }
}