package com.feihua.wechat.publicplatform.dto;

/**
 * 消息转发到客服,通用小程序
 * 
 * @author Administrator
 * 
 */
public class ResponseTransfer2CustomerServiceMessage extends ResponseMessage{

	public ResponseTransfer2CustomerServiceMessage(){
		super();
		this.setMsgType("transfer_customer_service");
	}

}
