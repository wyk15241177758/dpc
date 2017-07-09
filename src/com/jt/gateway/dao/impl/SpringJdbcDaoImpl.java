package com.jt.gateway.dao.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.jt.gateway.dao.Dao;

@SuppressWarnings("deprecation")
public class SpringJdbcDaoImpl extends JdbcDaoSupport            implements Dao {
	private Log log = LogFactory.getLog(this.getClass());
 
	@Override
	public int executeQueryForCount(String sql) throws ClassNotFoundException,SQLException {
		log.info("查询总数："+sql);
		try{
			return super.getJdbcTemplate().queryForObject(sql,Integer.class);
		}catch( EmptyResultDataAccessException e ){
			return 0;
		}
	}

	@Override
	public int executeQueryForCount(String sql, int[] types, Object[] values)throws ClassNotFoundException, SQLException {
		log.info("查询总数："+sql);
		try{
			return super.getJdbcTemplate().queryForObject(sql, values, types,Integer.class);
		}catch( EmptyResultDataAccessException e ){
			return 0;
		}
	}

	@Override
	public List<Map<String, Object>> executeQueryForList(String sql)throws ClassNotFoundException, SQLException {
		log.info("查询多条："+sql);
		try{
			return super.getJdbcTemplate().queryForList(sql);
		}catch( EmptyResultDataAccessException e ){
			return new ArrayList<Map<String, Object>>();
		}
	}

	@Override
	public List<Map<String, Object>> executeQueryForList(String sql,int[] types, Object[] values) throws ClassNotFoundException,SQLException {
		log.info("查询多条："+sql);
		try{
			return  super.getJdbcTemplate().queryForList(sql, values, types);
		}catch( EmptyResultDataAccessException e ){
			return new ArrayList<Map<String, Object>>();
		}
	}

	@Override
	public Map<String, Object> executeQueryForMap(String sql)throws ClassNotFoundException, SQLException {
		log.info("查询一条："+sql);
		try{
			return super.getJdbcTemplate().queryForMap(sql);
		}catch( EmptyResultDataAccessException e ){
			return null;
		}
	}

	@Override
	public Map<String, Object> executeQueryForMap(String sql, int[] types,Object[] values) throws ClassNotFoundException, SQLException {
		log.info("查询一条："+sql);
		try{
			return super.getJdbcTemplate().queryForMap(sql, values, types);
		}catch( EmptyResultDataAccessException e ){
			return null;
		}
	}

	@Override
	public int executeUpdate(String sql) throws ClassNotFoundException,SQLException {
		log.info("更新操作："+sql);
		return super.getJdbcTemplate().update(sql);
	}

	@Override
	public int executeUpdate(String sql, int[] types, Object[] values)throws ClassNotFoundException, SQLException, FileNotFoundException,IOException {
		log.info("更新操作："+sql);
		return super.getJdbcTemplate().update(sql, values, types);
	}

	@Override
	public List<?> executeQueryForList(String sql, Class<?> cla)throws ClassNotFoundException, SQLException {
		log.info("查询多条："+sql);
		try{
			
			return super.getJdbcTemplate().queryForList(sql, ParameterizedBeanPropertyRowMapper.newInstance( cla ) );
		}catch( EmptyResultDataAccessException e ){
			return new ArrayList<Map<String, Object>>();
		}
	}
	@Override
	public List<?> executeQueryForList(String sql, int[] types,Object[] values, Class<?> cla) throws ClassNotFoundException,SQLException {
		log.info("查询多条："+sql);
		try{
			return super.getJdbcTemplate().queryForList(sql, values, types, ParameterizedBeanPropertyRowMapper.newInstance( cla ) );
		}catch( EmptyResultDataAccessException e ){
			return new ArrayList<Map<String, Object>>();
		}
	}
	@Override
	public Object executeQueryForObject(String sql, Class<?> cla)throws ClassNotFoundException, SQLException {
		log.info("查询一条："+sql);
		try{
			return super.getJdbcTemplate().queryForObject(sql, ParameterizedBeanPropertyRowMapper.newInstance( cla ) );
		}catch( EmptyResultDataAccessException e ){
			return null;
		}
	}

	@Override
	public Object executeQueryForObject(String sql, int[] types,Object[] values, Class<?> cla) throws ClassNotFoundException,SQLException {
		log.info("查询一条："+sql);
		try{
			return super.getJdbcTemplate().queryForObject(sql, values, types, ParameterizedBeanPropertyRowMapper.newInstance( cla ) );
		}catch( EmptyResultDataAccessException e ){
			return null;
		}
	}



}
