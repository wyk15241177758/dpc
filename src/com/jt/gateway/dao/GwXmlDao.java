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

import com.jt.bean.lucene.DataField;
import com.jt.bean.lucene.GwConfig;
import com.jt.bean.lucene.GwConfigs;
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
				config.setTaskName(((Element) task).getChild("TASKNAME").getText());
				config.setIndexPath(((Element) task).getChild("INDEXPATH").getText());
				config.setDbName(((Element) task).getChild("DBNAME").getText());
				config.setIdName(((Element) task).getChild("IDNAME").getText());
				Element fields=((Element) task).getChild("FIELDS");
				for(Object e:fields.getChildren()){
					DataField field=new DataField();
					field.setName(((Element)e).getChildText("FIELDNAME"));
					if("store".equalsIgnoreCase(((Element)e).getChildText("FIELDTYPE"))){
						field.setType(TextField.TYPE_STORED);
					}else{
						field.setType(TextField.TYPE_NOT_STORED);
					}
					config.getList().add(field);
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
			config.setTaskName(((Element) task).getChild("TASKNAME").getText());
			config.setIndexPath(((Element) task).getChild("INDEXPATH").getText());
			config.setDbName(((Element) task).getChild("DBNAME").getText());
			config.setIdName(((Element) task).getChild("IDNAME").getText());
			Element fields=((Element) task).getChild("FIELDS");
			for(Object e:fields.getChildren()){
				DataField field=new DataField();
				field.setName(((Element)e).getChildText("FIELDNAME"));
				if("store".equalsIgnoreCase(((Element)e).getChildText("FIELDTYPE"))){
					field.setType(TextField.TYPE_STORED);
				}else{
					field.setType(TextField.TYPE_NOT_STORED);
				}
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
