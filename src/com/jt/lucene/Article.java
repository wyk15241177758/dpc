package com.jt.lucene;
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
	private String content;
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
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
}