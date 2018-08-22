package com.feihua.wechat.publicplatform.dto;


import com.feihua.wechat.publicplatform.PublicConstants;


import java.util.List;

/**
 * 微信发送图文消息
 * 
 * @author yw 2016年3月17日 15:06:30
 * 
 */
public class ResponseImgArticleMessage extends ResponseMessage{

	private int articleCount;
	private List<Item> articles;
	public ResponseImgArticleMessage(){
		super();
		this.setMsgType(PublicConstants.MessageType.news.name());
	}

	public int getArticleCount() {
		return articleCount;
	}

	public void setArticleCount(int articleCount) {
		this.articleCount = articleCount;
	}

	public List<Item> getArticles() {
		return articles;
	}

	public void setArticles(List<Item> articles) {
		this.articles = articles;
	}


	public static class Item {

		private String title;
		private String description;
		private String picUrl;
		private String url;

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getPicUrl() {
			return picUrl;
		}

		public void setPicUrl(String picUrl) {
			this.picUrl = picUrl;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

	}
}
