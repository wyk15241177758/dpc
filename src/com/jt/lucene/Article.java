package com.jt.lucene;

import java.util.Date;

/**
 * @项目名称：lucene
 * @类名称：Article
 * @类描述：这是一个文章实体类
 * @创建人：YangChao
 * @创建时间：2016年8月30日 下午3:11:38
 * @version 1.0.0
 */
public class Article {
	private Integer id;
	private String title;
	private String url;
	private String channel;
	private String site;
	private String category;//分类
	private Date date;
	
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
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
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
	
}