package com.feihua.wechat.publicplatform.dto;


import com.feihua.wechat.publicplatform.PublicConstants;


/**
 * 微信发送文本消息
 * 
 * @author Administrator
 * 
 */
public class ResponseTextMessage extends ResponseMessage{

	private String content;
	public String getContent() {
		return content;
	}

	public ResponseTextMessage(){
		super();
		this.setMsgType(PublicConstants.MessageType.text.name());
	}
	public void setContent(String content) {
		this.content = content;
	}

}
