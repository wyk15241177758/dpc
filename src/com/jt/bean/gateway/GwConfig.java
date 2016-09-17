package com.jt.bean.gateway;

import java.util.ArrayList;
import java.util.List;

public class GwConfig {
	private List<DataField> list;
	private String indexPath;
	private String idName;
	private String taskName;

	private String sqlIP;
	private String sqlUser;
	private String sqlPw;
	private String sqlDB;
	private String sqlPort;
	private String sqlTable;

	
	public String getSqlPort() {
		return sqlPort;
	}

	public void setSqlPort(String sqlPort) {
		this.sqlPort = sqlPort;
	}

	public String getTaskName() {
		return taskName;
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

	public String getSqlIP() {
		return sqlIP;
	}
	public void setSqlIP(String sqlIP) {
		this.sqlIP = sqlIP;
	}
	public String getSqlUser() {
		return sqlUser;
	}
	public void setSqlUser(String sqlUser) {
		this.sqlUser = sqlUser;
	}
	public String getSqlPw() {
		return sqlPw;
	}
	public void setSqlPw(String sqlPw) {
		this.sqlPw = sqlPw;
	}
	public String getSqlDB() {
		return sqlDB;
	}
	public void setSqlDB(String sqlDB) {
		this.sqlDB = sqlDB;
	}
	public String getSqlTable() {
		return sqlTable;
	}
	public void setSqlTable(String sqlTable) {
		this.sqlTable = sqlTable;
	}
	public GwConfig(){
		this.list=new ArrayList<DataField>();
	}

	@Override
	public String toString() {
		return "GwConfig [list=" + list + ", indexPath=" + indexPath + ", idName=" + idName
				+ ", taskName=" + taskName + ", sqlIP=" + sqlIP + ", sqlUser=" + sqlUser + ", sqlPw=" + sqlPw
				+ ", sqlDB=" + sqlDB + ", sqlTable=" + sqlTable + "]";
	}
	
}
