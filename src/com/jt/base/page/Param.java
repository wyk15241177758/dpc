package com.jt.base.page;
/**********************************************
 * 
 * @time   2011-1-13
 **********************************************/
public class Param {

	private int type ; 
	private Object value;
	private String paramName;
	private boolean isLike;
	
	public Param() {
		
	}
	public Param(int type, Object value) {
		this.type = type;
		this.value = value;
	}
	
	public Param(int type, Object value, String paramName, boolean isLike) {
		super();
		this.type = type;
		this.value = value;
		this.paramName = paramName;
		this.isLike = isLike;
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
	
	
	public String getParamName() {
		return paramName;
	}
	public void setParamName(String paramName) {
		this.paramName = paramName;
	}
	public boolean isLike() {
		return isLike;
	}
	public void setLike(boolean isLike) {
		this.isLike = isLike;
	}
	@Override
	public String toString() {
		return "Param [type=" + type + ", value=" + value + ", paramName=" + paramName + ", isLike=" + isLike + "]";
	}
	
	
	
}
