package com.jt.bean.gateway;

import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.TextField;

public class DataField {
	private String name;
	private boolean isKey;
	private String type;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public FieldType getFieldType() {
		if(type.equalsIgnoreCase("STORE")){
			return TextField.TYPE_STORED;
		}else{
			return TextField.TYPE_NOT_STORED;
		}
	}
	
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public DataField(String name, boolean isKey, String type) {
		super();
		this.name = name;
		this.isKey = isKey;
		this.type = type;
	}
	public boolean isKey() {
		return isKey;
	}
	public void setKey(boolean isKey) {
		this.isKey = isKey;
	}
	public DataField(){
		
	}
	@Override
	public String toString() {
		return "DataField [name=" + name + ", isKey=" + isKey + ", type=" + type + "]";
	}

}
