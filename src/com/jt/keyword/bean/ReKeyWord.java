package com.jt.keyword.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class ReKeyWord  implements  Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1962515706482957468L;

	public   Integer   id;
	
	public   Integer  kid;
	public   String   kids;
	public   List<KeyWord> reKeywords;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getKid() {
		return kid;
	}
	public void setKid(Integer kid) {
		this.kid = kid;
	}
	public String getKids() {
		return kids;
	}
	public void setKids(String kids) {
		this.kids = kids;
	}
	public List<KeyWord> getReKeywords() {
		return reKeywords;
	}
	public void setReKeywords(List<KeyWord> reKeywords) {
		this.reKeywords = reKeywords;
	}
	
	
	
	
	
	

}
