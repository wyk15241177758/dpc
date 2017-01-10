package com.jt.test.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.jt.gateway.dao.JdbcDao;
import com.jt.gateway.dao.impl.JdbcDaoImpl;

public class JdbcTest {
	public static void main(String[] args) {
		String DB="qasys";
		String ip="127.0.0.1";
		String port="3306";
		String user="root";
		String pw="root";
		String table="searchHis";
		
		JdbcDao JdbcDao = new JdbcDaoImpl(DB, ip, port+ "", user,pw);
		Map<String, Object> rsMap = null;
		String sql="select ID,SEARCHCONTENT,SEARCHTIMES,CREATETIME,UPDATETIME from searchhis where ID>=1 and ID<1001";
		
		try {
			List<Map<String, Object>> rsList = JdbcDao.executeQueryForList(sql);
			for(Map<String,Object> map:rsList){
				System.out.println(map);
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
}
