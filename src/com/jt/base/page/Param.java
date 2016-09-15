package com.jt.base.page;
/**********************************************
 * 
 * @time   2011-1-13
 **********************************************/
public class Param {

	private int type ; 
	private Object value;
	
	
	
	public Param() {
		
	}
	public Param(int type, Object value) {
		this.type = type;
		this.value = value;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	
	
	
}
