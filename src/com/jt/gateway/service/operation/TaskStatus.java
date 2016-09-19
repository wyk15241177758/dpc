package com.jt.gateway.service.operation;

import java.io.IOException;

import com.jt.gateway.dao.impl.GwPropertyDaoImpl;

public class TaskStatus {
	private GwPropertyDaoImpl proDao;
	private long lastId;
	private String lastStatus;
	
	public TaskStatus(String taskName) throws IOException {
		proDao = new GwPropertyDaoImpl(taskName);
	}

	public void saveTask() throws IOException {
		proDao.add("lastId", lastId + "");
		proDao.add("lastStatus", lastStatus);
		proDao.save();
	}


	public void setLastId(long lastId) {
		this.lastId = lastId;
	}

	public void setLastStatus(String lastStatus) {
		this.lastStatus = lastStatus;
	}
	
	
	public long getLastId() throws IOException{
		long lastId=0l;
		try {
			lastId=Long.parseLong(proDao.get("lastId"));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return lastId;
	}
	
	public String getLastStatus() throws IOException{
		return proDao.get("lastStatus");
	}
	public static void main(String[] args) {
		String a="智能问答数据抽取";
		try {
			TaskStatus s=new TaskStatus(a);
			System.out.println(s.getLastId());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
