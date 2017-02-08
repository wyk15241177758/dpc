package com.jt.keyword.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @ClassName ConfigManager
 * @Description TODO(读取配置文件)
 * @author 邹许红
 * @date 2016-4-1 下午1:26:12
 */
public class ConfigManager {
	public static String   configName="jdbc.properties";
	private static final Logger LOGGER = LoggerFactory.getLogger(ConfigManager.class);

	 //存储配置值
    public static   Properties props = new Properties();
    

	/**
	 * 
	 * @Title getValueByKey
	 * @Description TODO(获取配置文件的信息)
	 * @param key
	 * @return
	 */
	public static String  getValueByKey(String  key){
		try {
			props.load(new InputStreamReader(ConfigManager.class.getResourceAsStream("/" + configName),"UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
			LOGGER.error("找不到配置文件{}",configName);
		}
		return props.getProperty(key);
		
	}
	
	
	
	
	
	

}
