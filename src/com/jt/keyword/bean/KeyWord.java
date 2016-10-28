package com.jt.keyword.bean;

import java.io.Serializable;
import java.util.List;

public class KeyWord  implements  Serializable  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5915364456359252249L;
	public   Integer   id;
	public   KeyWord  parent;
	public   String    wordvalue;
	public   String    extend;
	public   Integer    floor;
	public   List<KeyWord>  childrens;
	public   Integer    idx;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public KeyWord getParent() {
		return parent;
	}
	public void setParent(KeyWord parent) {
		this.parent = parent;
	}
	public String getWordvalue() {
		return wordvalue;
	}
	public void setWordvalue(String wordvalue) {
		this.wordvalue = wordvalue;
	}
	public String getExtend() {
		return extend;
	}
	public void setExtend(String extend) {
		this.extend = extend;
	}
	public Integer getFloor() {
		return floor;
	}
	public void setFloor(Integer floor) {
		this.floor = floor;
	}
	public List<KeyWord> getChildrens() {
		return childrens;
	}
	public void setChildrens(List<KeyWord> childrens) {
		this.childrens = childrens;
	}
	public Integer getIdx() {
		return idx;
	}
	public void setIdx(Integer idx) {
		this.idx = idx;
	}

	
	
	
	
	
	

}
