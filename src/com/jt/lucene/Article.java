package com.jt.lucene;

import java.util.Date;
import java.util.HashMap;

/**
 * @项目名称：lucene
 * @类名称：Article
 * @类描述：这是一个文章实体类
 * @创建时间：2016年8月30日 下午3:11:38
 * @version 1.0.0
 */
public class Article {
	@Override
	public String toString() {
		return "Article [id=" + id + ", title=" + title + ", url=" + url + ", channel=" + channel + ", site=" + site
				+ ", category=" + category + ", date=" + date + "]";
	}
	private Long id;
	//标题
	private String title;
	//预设页面的地址
	private String url;
	//预设页面的html
	private String html;
	private String channel;
	private String channelUrl;
	private String site;
	private String category;//分类
	private Date date;
	private String keyWord;//标签
	//返回与jtcrawler表的字段对应关系，必须小写
	public static String getMapedFieldName(String articleField) {
		if(articleField.equalsIgnoreCase("id")){
			return "XQ_ID";
		}
		if(articleField.equalsIgnoreCase("title")){
			return "XQ_TITLE";
		}
		if(articleField.equalsIgnoreCase("url")){
			return "XQ_URL";
		}
		if(articleField.equalsIgnoreCase("channel")){
			return "LM_NAME";
		}
		if(articleField.equalsIgnoreCase("channelUrl")){
			return "ZDLM_URL";
		}
		if(articleField.equalsIgnoreCase("site")){
			return "ZD_NAME";
		}
		if(articleField.equalsIgnoreCase("category")){
			return "SJFL";
		}
		if(articleField.equalsIgnoreCase("date")){
			return "XQ_PUDATE";
		}
		if(articleField.equalsIgnoreCase("keyWord")){
			return "KEY_WORD";
		}
		return null;
	}
	
	public String getChannelUrl() {
		return channelUrl;
	}

	public void setChannelUrl(String channelUrl) {
		this.channelUrl = channelUrl;
	}

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getSite() {
		return site;
	}
	public void setSite(String site) {
		this.site = site;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getHtml() {
		return html;
	}
	public void setHtml(String html) {
		this.html = html;
	}
	
}