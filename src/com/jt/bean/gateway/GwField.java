package com.jt.bean.gateway;

import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.TextField;

public class GwField {
	private String name;
	private boolean key;
	private String type;
	private Integer fieldId;
	private Long jobId;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public FieldType getFieldType() {
		if(type.equalsIgnoreCase("存储")){
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
	public GwField(String name, boolean key, String type) {
		super();
		this.name = name;
		this.key = key;
		this.type = type;
	}
	public Integer getFieldId() {
		return fieldId;
	}
	public void setFieldId(Integer fieldId) {
		this.fieldId = fieldId;
	}
	public Long getJobId() {
		return jobId;
	}
	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}
	public boolean isKey() {
		return key;
	}
	public void setKey(boolean key) {
		this.key = key;
	}
	public GwField(){
		
	}
	@Override
	public boolean equals(Object obj) {
		GwField paramField=(GwField)obj;
		if(paramField.getName().equals(this.name)&&paramField.getType().equals(this.type)&&
				(paramField.key==this.key)){
			return true;
		}else{
			return false;
		}
	}
	@Override
	public String toString() {
		return "DataField [name=" + name + ", key=" + key + ", type=" + type + "]";
	}

}
