package com.jt.keyword.bean;

import java.io.Serializable;
import java.util.Date;
/**
 * 
 * @author 邹许红
 *
 */
public class ParseHotWord implements  Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7920281381157259405L;
	private  Integer  id;
	private  String   hotword;
	private Date createTime; 
	private Integer      num;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getHotword() {
		return hotword;
	}
	public void setHotword(String hotword) {
		this.hotword = hotword;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	
	
	

}
