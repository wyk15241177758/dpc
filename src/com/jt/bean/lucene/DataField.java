package com.jt.bean.lucene;

import org.apache.lucene.document.FieldType;

public class DataField {
	private String name;
	@Override
	public String toString() {
		return "DataField [name=" + name + ", type=" + type + "]";
	}

	private FieldType type;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public FieldType getType() {
		return type;
	}
	public void setType(FieldType type) {
		this.type = type;
	}
	public DataField(String name, FieldType type) {
		super();
		this.name = name;
		this.type = type;
	}
	
	public DataField(){
		
	}
}
