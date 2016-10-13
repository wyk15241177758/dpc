package com.jt.lucene;

import java.util.Date;
import java.util.HashMap;

/**
 * @项目名称：lucene
 * @类名称：Article
 * @类描述：这是一个文章实体类
 * @创建人：YangChao
 * @创建时间：2016年8月30日 下午3:11:38
 * @version 1.0.0
 */
public class Article {
	@Override
	public String toString() {
		return "Article [id=" + id + ", title=" + title + ", url=" + url + ", channel=" + channel + ", site=" + site
				+ ", category=" + category + ", date=" + date + "]";
	}
	private Integer id;
	private String title;
	private String url;
	private String channel;
	private String site;
	private String category;//分类
	private Date date;
	//返回与jtcrawler表的字段对应关系，必须小写
	public static String getMapedFieldName(String articleField) {
		if(articleField.equalsIgnoreCase("id")){
			return "xq_id";
		}
		if(articleField.equalsIgnoreCase("title")){
			return "xq_title";
		}
		if(articleField.equalsIgnoreCase("url")){
			return "xq_url";
		}
		if(articleField.equalsIgnoreCase("channel")){
			return "lm_name";
		}
		if(articleField.equalsIgnoreCase("site")){
			return "zd_name";
		}
		if(articleField.equalsIgnoreCase("category")){
			return "sjfl";
		}
		if(articleField.equalsIgnoreCase("date")){
			return "load_time";
		}
		return null;
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