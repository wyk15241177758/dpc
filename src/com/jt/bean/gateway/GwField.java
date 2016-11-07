package com.jt.bean.gateway;

import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.TextField;

public class GwField {
	private String name;
	private boolean tableKey;
	private String type;
	private Long fieldId;
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
	public GwField(String name, boolean tableKey, String type) {
		super();
		this.name = name;
		this.tableKey = tableKey;
		this.type = type;
	}

	public Long getFieldId() {
		return fieldId;
	}
	public void setFieldId(Long fieldId) {
		this.fieldId = fieldId;
	}
	public Long getJobId() {
		return jobId;
	}
	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}

	
	public boolean isTableKey() {
		return tableKey;
	}
	public void setTableKey(boolean tableKey) {
		this.tableKey = tableKey;
	}
	public GwField(){
		
	}
	@Override
	public boolean equals(Object obj) {
		GwField paramField=(GwField)obj;
		if(paramField.getName().equals(this.name)&&paramField.getType().equals(this.type)&&
				(paramField.tableKey==this.tableKey)){
			return true;
		}else{
			return false;
		}
	}
	@Override
	public String toString() {
		return "GwField [name=" + name + ", tableKey=" + tableKey + ", type=" + type + ", fieldId=" + fieldId
				+ ", jobId=" + jobId + "]";
	}


}
