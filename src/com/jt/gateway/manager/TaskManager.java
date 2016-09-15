package com.jt.gateway.manager;

import java.io.IOException;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.jdom.JDOMException;

import com.jt.gateway.service.IndexTask_bak;
import com.jt.gateway.service.TaskStatus;



public class TaskManager {
	private static Logger logger = Logger.getLogger(TaskManager.class);
	
	
	private int batchSize=5000;
	
	private TaskStatus taskStatus;
	
	
	public void doTask(String taskName) throws IOException, JDOMException, InterruptedException, ClassNotFoundException, SQLException{
		IndexTask_bak task=new IndexTask_bak(taskName);
		taskStatus=new TaskStatus(taskName);
		String lastStatus=taskStatus.getLastStatus();
		long lastId=taskStatus.getLastId();
		//第一次运行
		if(lastStatus==null){
			taskStatus.setLastId(0l);
			taskStatus.setLastStatus("suc");
			taskStatus.saveTask();
			lastStatus="suc";
			lastId=0l;
		}
		//上次执行失败
		if(!lastStatus.equalsIgnoreCase("suc")){
			System.out.println("111");
			if(lastId==0){
				task.delAllIndex();
			}else{
				task.delIndex(lastId);
			}
		}else{
			System.out.println("222");
			long maxId=task.getMaxId();
			long beginId=0;
			long endId=batchSize;
			do{
				task.setBeginId(beginId);
				task.setEndId(endId);
				task.createIndex();
				beginId=endId;
				endId+=batchSize;
			}while(endId<(maxId+batchSize));
			taskStatus.setLastId(endId);
			taskStatus.setLastStatus("suc");
			taskStatus.saveTask();
		}
	}
public static void main(String[] args) {
	TaskManager manager=new TaskManager();
	try {
		manager.doTask("智能问答数据抽取");
	} catch (ClassNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (JDOMException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
}
