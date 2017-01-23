package com.jt.keyword.util;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import com.mchange.v2.c3p0.ComboPooledDataSource;
/**
 * 
 * @ClassName DataSourceUtil
 * @Description TODO(C3P0连接工具类)
 * @author 邹许红
 * @date 2017-1-3 上午9:38:51
 */
public class DataSourceUtil {
	private static final Logger LOGGER = Logger.getLogger(DataSourceUtil.class);

	static String url =ConfigManager.getValueByKey("jdbcUrl");
	static String user = ConfigManager.getValueByKey("username");
	static String password = ConfigManager.getValueByKey("passwd");
	static String dirver = ConfigManager.getValueByKey("driverClass");

	static int    acquireIncrement=5;
	static int    minPoolSize=15;
	static int    maxPoolSize=50;
	static int    initialPoolSize=10;
	static int    maxIdleTime=600;
	static int    acquireRetryAttempts=10;
	static int    idleConnectionTestPeriod=1800;
	public static ComboPooledDataSource  dataSource=new ComboPooledDataSource();

	static{
	try {
			dataSource.setDriverClass(dirver);
		} catch (PropertyVetoException e) {
			LOGGER.error(e);
		}
		dataSource.setJdbcUrl(url);
		dataSource.setUser(user);
		dataSource.setPassword(password);
		dataSource.setAcquireIncrement(acquireIncrement);
		dataSource.setMinPoolSize(minPoolSize);
		dataSource.setMaxPoolSize(maxPoolSize);
		dataSource.setInitialPoolSize(initialPoolSize);
		dataSource.setMaxIdleTime(maxIdleTime);
		dataSource.setAcquireRetryAttempts(acquireRetryAttempts);
		dataSource.setIdleConnectionTestPeriod(idleConnectionTestPeriod);
		dataSource.setPreferredTestQuery("SELECT 1");
		
	}
	/**
	 * 
	 * @Title getConnection
	 * @Description TODO(获取数据库连接)
	 * @return
	 */
	public  static   Connection  getConnection(){
		try {
			Connection cs = dataSource.getConnection();
			return cs;
		} catch (SQLException e) {
			LOGGER.error(e);
		}
		return null;
         
	}
	
	public static void main(String[] args) {
		try {
			Connection cs = dataSource.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
}
