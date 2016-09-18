package com.jt.gateway.dao;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import org.apache.lucene.document.TextField;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.jt.bean.gateway.DataField;
import com.jt.bean.gateway.GwConfig;
import com.jt.bean.gateway.GwConfigs;
/**
 * //获取配置信息，并写入config返回
 * @author zhengxiaobin
 *
 */
public class GwXmlDao {
	 
	public static GwConfig getConfig(String taskName) throws JDOMException{
		GwConfig config=null;
		//获得配置信息
		SAXBuilder builder = new SAXBuilder();
		Document doc = builder.build(new File("gateway.conf"));
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
	
	public static GwConfigs getConfigs() throws JDOMException{
		GwConfigs configs=new GwConfigs();
		GwConfig config=new GwConfig();
		//获得配置信息
		SAXBuilder builder = new SAXBuilder();
		Document doc = builder.build(new File("gateway.conf"));
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
				GwConfig config=GwXmlDao.getConfig("智能问答数据抽取");
				List<DataField> list=config.getList();
				String indexPath=config.getIndexPath();
				System.out.println("indexPath=["+indexPath+"]");
				for(DataField df :list){
					System.out.println("name=["+df.getName()+"] type=["+df.getType()+"]");
				}
			} catch (JDOMException e) {
				e.printStackTrace();
			}

	}
}
