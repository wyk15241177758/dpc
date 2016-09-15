package com.jt.lucene.bean;

import java.util.ArrayList;
import java.util.List;

public class GwConfig {
	private List<DataField> list;
	private String indexPath;
	private String dbName;
	private String idName;
	private String taskName;
	
	public String getTaskName() {
		return taskName;
	}
	@Override
	public String toString() {
		return "GwConfig [list=" + list + ", indexPath=" + indexPath + ", dbName=" + dbName + ", idName=" + idName
				+ ", taskName=" + taskName + "]";
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public String getIdName() {
		return idName;
	}
	public void setIdName(String idName) {
		this.idName = idName;
	}
	public List<DataField> getList() {
		return list;
	}
	public void setList(List<DataField> list) {
		this.list = list;
	}
	public String getIndexPath() {
		return indexPath;
	}
	public void setIndexPath(String indexPath) {
		this.indexPath = indexPath;
	}
	public GwConfig(List<DataField> list, String indexPath,String dbName,String idName,String taskName) {
		super();
		this.list = list;
		this.indexPath = indexPath;
		this.dbName=dbName;
		this.idName=idName;
		this.taskName=taskName;
	}
	public String getDbName() {
		return dbName;
	}
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
	public GwConfig(){
		this.list=new ArrayList<DataField>();
	}
}
