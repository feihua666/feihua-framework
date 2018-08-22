package com.feihua.wechat.publicplatform.dto;

import com.feihua.wechat.publicplatform.PublicConstants;

/**
 * 接收微信文本信息
 * 
 * @author feihua
 * 
 */
public class RequestTextMessage extends RequestMessage{
	private String content;
	private int msgId;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getMsgId() {
		return msgId;
	}

	public void setMsgId(int msgId) {
		this.msgId = msgId;
	}

}
