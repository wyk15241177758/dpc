package com.jt.gateway.service.operation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import com.jt.bean.gateway.DataField;
import com.jt.bean.gateway.GwConfig;
import com.jt.bean.gateway.GwConfigs;
/**
 * //配置信息的增删改查
 * @author zhengxiaobin
 *
 */
public class GwConfigService {
	private File file ;
	private SAXBuilder builder;
	private Document doc;
	public GwConfigService() throws JDOMException, IOException{
		this.file=new File("gateway.conf");
		 builder = new SAXBuilder();
	}
	
	public GwConfigService(File file) throws JDOMException, IOException{
		this.file=file;
		if(!file.exists()){
			file.createNewFile();
		}
		 builder = new SAXBuilder();
	}
	
	public void addConfigs(GwConfigs configs) throws IOException, JDOMException{
		Element root = new Element("TASKS");
		
		for(GwConfig config:configs.getConfigs()){
			root.addContent(config2Element(config));
		}
		
		doc = new Document(root);
		FileWriter fw = new FileWriter(file);
		XMLOutputter out = new XMLOutputter();
		
		out.setEncoding("UTF-8");
		out.output(doc, fw);
		fw.close();
		
	}
	
	public void addConfig(GwConfig config) throws Exception{

		//检查此任务是否已存在
		if(getConfig(config.getTaskName())!=null){
			throw new Exception("任务已存在，无法新增");
		}
		//如果原先有config，则重新加入
		GwConfigs configs=getConfigs();
		if(configs==null){
			configs=new GwConfigs();
		}
		configs.addConfig(config);
		addConfigs(configs);
	}
	public Element config2Element(GwConfig config) throws IOException, JDOMException{
		
		Element task = new Element("TASK");
		Element name = new Element("TASKNAME");
		Element path = new Element("INDEXPATH");
		Element sqlIp = new Element("SQLIP");
		Element sqlUser = new Element("SQLUSER");
		Element sqlPw = new Element("SQLPW");
		Element sqlDb = new Element("SQLDB");
		Element sqlPort = new Element("SQLPORT");
		Element sqlTable = new Element("SQLTABLE");
		Element idName = new Element("IDNAME");
		Element fields = new Element("FIELDS");
		for(DataField df:config.getList()){
			Element field = new Element("FIELD");
			Element fieldName = new Element("FIELDNAME");
			Element fieldType = new Element("FIELDTYPE");
			Element isKey = new Element("ISKEY");
			fieldName.addContent(df.getName());
			fieldType.addContent(df.getType());
			isKey.addContent(df.isKey()+"");
			field.addContent(fieldName);
			field.addContent(fieldType);
			field.addContent(isKey);
			fields.addContent(field);
		}
		
		name.addContent(config.getTaskName());
		path.addContent(config.getIndexPath());
		sqlIp.addContent(config.getSqlIP());
		sqlUser.addContent(config.getSqlUser());
		sqlPw.addContent(config.getSqlPw());
		sqlDb.addContent(config.getSqlDB());
		sqlPort.addContent(config.getSqlPort());
		sqlTable.addContent(config.getSqlTable());
		idName.addContent(config.getIdName());
		
		task.addContent(name);
		task.addContent(path);
		task.addContent(sqlIp);
		task.addContent(sqlUser);
		task.addContent(sqlPw);
		task.addContent(sqlDb);
		task.addContent(sqlPort);
		task.addContent(sqlTable);
		task.addContent(idName);
		task.addContent(fields);
		
		return task;
		
	}
	public boolean delConfig(String taskName) throws IOException, JDOMException{
		GwConfigs configs=getConfigs();
		if(configs!=null){
			GwConfig config=configs.getConfigByName(taskName);
			if(config!=null){
				configs.removeConfig(config);
				addConfigs(configs);
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}
	public boolean updateConfig(GwConfig config) throws IOException, JDOMException{
		GwConfigs configs=getConfigs();
		if(configs!=null){
			GwConfig oldConfig=configs.getConfigByName(config.getTaskName());
			if(oldConfig!=null){
				configs.removeConfig(oldConfig);
				configs.addConfig(config);
				addConfigs(configs);
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}
	
	public  GwConfig getConfig(String taskName){
		GwConfig config=null;
		//获得配置信息
		try {
			doc = builder.build(file);
		} catch (JDOMException e1) {
			return null;
		}
		Element root = doc.getRootElement();
		List tasks = root.getChildren();
		for (Object task:tasks) {
			if(taskName.equalsIgnoreCase(((Element) task).getChild("TASKNAME").getText())){
				config=new GwConfig();
				config.setIndexPath(((Element) task).getChild("INDEXPATH").getText());
				config.setSqlDB(((Element) task).getChild("SQLDB").getText());
				config.setSqlIP(((Element) task).getChild("SQLIP").getText());
				config.setSqlPort(((Element) task).getChild("SQLPORT").getText());
				config.setSqlUser(((Element) task).getChild("SQLUSER").getText());
				config.setSqlPw(((Element) task).getChild("SQLPW").getText());
				config.setSqlTable(((Element) task).getChild("SQLTABLE").getText());
				config.setTaskName(((Element) task).getChild("TASKNAME").getText());
				Element fields=((Element) task).getChild("FIELDS");
				for(Object e:fields.getChildren()){
					DataField field=new DataField();
					field.setName(((Element)e).getChildText("FIELDNAME"));
					field.setType(((Element)e).getChildText("FIELDTYPE"));
					config.getList().add(field);
					if(field.isKey()){
						config.setIdName(((Element)e).getChildText("FIELDNAME"));
					}
				}
				break;
			}
		}
		return config;
	}
	
	public  GwConfigs getConfigs() {
		GwConfigs configs=new GwConfigs();
		GwConfig config=new GwConfig();
		//获得配置信息
		try {
			doc = builder.build(file);
		} catch (JDOMException e1) {
			return null;
		}
		Element root = doc.getRootElement();
		List tasks = root.getChildren();
		for (Object task:tasks) {
			config=new GwConfig();
			config.setIdName(((Element) task).getChild("IDNAME").getText());
			config.setIndexPath(((Element) task).getChild("INDEXPATH").getText());
			config.setSqlDB(((Element) task).getChild("SQLDB").getText());
			config.setSqlIP(((Element) task).getChild("SQLIP").getText());
			config.setSqlPort(((Element) task).getChild("SQLPORT").getText());
			config.setSqlUser(((Element) task).getChild("SQLUSER").getText());
			config.setSqlPw(((Element) task).getChild("SQLPW").getText());
			config.setSqlTable(((Element) task).getChild("SQLTABLE").getText());
			config.setTaskName(((Element) task).getChild("TASKNAME").getText());
			Element fields=((Element) task).getChild("FIELDS");
			for(Object e:fields.getChildren()){
				DataField field=new DataField();
				field.setName(((Element)e).getChildText("FIELDNAME"));
				field.setType(((Element)e).getChildText("FIELDTYPE"));
				config.getList().add(field);
			}
			configs.addConfig(config);
		}
		return configs;
	}
	
	
	
	//test
	public static void main(String[] args) {
			try {
				GwConfigService dao=new GwConfigService();
				GwConfigService dao2=new GwConfigService(new File("gateway3.conf"));
				GwConfig config=dao.getConfig("智能问答数据抽取1");
				dao2.addConfig(config);
			} catch(Exception e){
				e.printStackTrace();
			}

	}
}
