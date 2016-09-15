package com.jt.gateway.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * ���ݿ����Ӷ���
 * @author 	�����
 * @time	2011-05-11
 */
public class JdbcDaoImpl implements JdbcDao{
	
	private Log log = LogFactory.getLog(this.getClass());

	private String sdbdriver = "org.gjt.mm.mysql.Driver";
	private String sconnStr = "jdbc:mysql://localhost:3306/jtcrawler?useUnicode=true&characterEncoding=utf-8";//�������ݿ�����Ϊ��pubs
    private String user = "root";  //��¼���ݿ��û���
    private String passwd = "root";   //��¼���ݿ�����

    
    /**
     * ��������
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public Connection getConnection() throws ClassNotFoundException, SQLException{
		 Class.forName(sdbdriver); //ָ��JDBC���ݿ���������
		 return DriverManager.getConnection(sconnStr,user,passwd);
    }
	/**
     * ����sql��ѯ�б�����(��ѯһ��)����֧��Ԥ����ķ�ʽ
     * @param sql
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
	public Map<String, Object> executeQueryForMap(String sql)throws ClassNotFoundException, SQLException {
    	Connection connect =this.getConnection();
		Statement stmt = connect.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		List<Map<String, Object>> list = this.rsToList(rs);
		if( !list.isEmpty() ){
			return list.get(0);
		}
		this.releaseConnection(rs, stmt, connect);//�ر�����
		return null;
	}

	/**
	 * ����sql��ѯ�б�����(��ѯһ��)��֧��Ԥ����ķ�ʽ
	 * @param sql
	 * @param types
	 * @param ObjectValues
	 * @return
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public Map<String, Object> executeQueryForMap(String sql, int[] types,Object[] ObjectValues) throws ClassNotFoundException, SQLException {
    	Connection connect = this.getConnection();
		PreparedStatement pst =  connect.prepareStatement(sql);
		
		if( types != null ){
			for(int i=0;i<types.length;i++){
				switch( types[i] ){
				case Types.VARCHAR:
					pst.setString(i+1, String.valueOf( ObjectValues[i] ) );
					break;
				case Types.INTEGER:
					pst.setInt(i+1, Integer.parseInt( String.valueOf( ObjectValues[i] ) ));
					break;
				case Types.BIGINT:
			    	pst.setLong(i+1, Long.valueOf(String.valueOf( ObjectValues[i] )));
			    	break;
				case Types.DOUBLE:
					pst.setDouble(i+1, Double.valueOf(String.valueOf( ObjectValues[i] )));
					break;
				case Types.DATE:
					pst.setDate(i+1, Date.valueOf(String.valueOf( ObjectValues[i] )));
					break;
				case Types.TIMESTAMP:
					pst.setTimestamp(i+1, Timestamp.valueOf(String.valueOf( ObjectValues[i] )));
					break;
				}
				
			}
		}

		ResultSet rs = pst.executeQuery();
		List<Map<String, Object>> list = this.rsToList(rs);
		if( !list.isEmpty() ){
			return list.get(0);
		}
		this.releaseConnection(rs, pst, connect);
		return null;
	}


	
    /**
     * ����sql��ѯ�б����ݣ���֧��Ԥ����ķ�ʽ
     * @param sql
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public List<Map<String, Object>> executeQueryForList(String sql) throws ClassNotFoundException, SQLException{
		
    	Connection connect =this.getConnection();
		Statement stmt = connect.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		List<Map<String, Object>> list = this.rsToList(rs);
		this.releaseConnection(rs, stmt, connect);//�ر�����
		return list;
	}

    /**
     * ִ�� ����ɾ���ġ��ȵĲ�������֧��Ԥ����ķ�ʽ
     * @param sql
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
	public int executeUpdate(String sql) throws ClassNotFoundException, SQLException {
		
		Connection connect=this.getConnection();
		Statement stmt=connect.createStatement();
		int count=stmt.executeUpdate(sql);

		this.releaseConnection(stmt, connect);//�ر�����
	
		return count;
	}
	
	
	/**
	 * ����sql��ѯ�б����ݣ�֧��Ԥ����ķ�ʽ
	 * @param sql
	 * @param types
	 * @param ObjectValues
	 * @return
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public List<Map<String, Object>> executeQueryForList(String sql , int[] types,Object[] ObjectValues) throws ClassNotFoundException, SQLException{
	    	Connection connect = this.getConnection();
			PreparedStatement pst =  connect.prepareStatement(sql);
			
			if( types != null ){
				for(int i=0;i<types.length;i++){
					switch( types[i] ){
					case Types.VARCHAR:
						pst.setString(i+1, String.valueOf( ObjectValues[i] ) );
						break;
					case Types.INTEGER:
						pst.setInt(i+1, Integer.parseInt( String.valueOf( ObjectValues[i] ) ));
						break;
					case Types.BIGINT:
				    	pst.setLong(i+1, Long.valueOf(String.valueOf( ObjectValues[i] )));
				    	break;
					case Types.DOUBLE:
						pst.setDouble(i+1, Double.valueOf(String.valueOf( ObjectValues[i] )));
						break;
					case Types.DATE:
						pst.setDate(i+1, Date.valueOf(String.valueOf( ObjectValues[i] )));
						break;
					case Types.TIMESTAMP:
						pst.setTimestamp(i+1, Timestamp.valueOf(String.valueOf( ObjectValues[i] )));
						break;
					}
					
				}
			}

			ResultSet rs = pst.executeQuery();
			List<Map<String, Object>> list = this.rsToList(rs);
			this.releaseConnection(rs, pst, connect);
			return list;
	}


	/**
	 * Ԥ����sql������   ֧��insert �� update  �� delete  ���
	 * @param sql
	 * @param types
	 * @param ObjectValues
	 * @return
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 * @throws IOException 
	 */
	public int executeUpdate(String sql , int[] types,Object[] ObjectValues) throws ClassNotFoundException, SQLException, IOException{

		Connection connect = this.getConnection();
		PreparedStatement pst =  connect.prepareStatement(sql);
		
		if( types != null ){
			for(int i=0;i<types.length;i++){
				switch( types[i] ){
				case Types.VARCHAR:
					pst.setString(i+1, String.valueOf( ObjectValues[i] ) );
					break;
				case Types.INTEGER:
					pst.setInt(i+1, Integer.parseInt( String.valueOf( ObjectValues[i] ) ));
					break;
				case Types.BLOB:
					InputStream in = new FileInputStream( (File)ObjectValues[i] );
					pst.setBinaryStream(i+1, in , in.available()  );
					break;
				case Types.BIGINT:
			    	pst.setLong(i+1, Long.valueOf(String.valueOf( ObjectValues[i] )));
			    	break;
				case Types.DOUBLE:
					pst.setDouble(i+1, Double.valueOf(String.valueOf( ObjectValues[i] )));
					break;
				case Types.DATE:
					pst.setDate(i+1, Date.valueOf(String.valueOf( ObjectValues[i] )));
					break;
				case Types.TIMESTAMP:
					pst.setTimestamp(i+1, Timestamp.valueOf(String.valueOf( ObjectValues[i] )));
					break;
				}
			}
		}

		int count = pst.executeUpdate();
		this.releaseConnection(pst, connect);
		return count;  
	}
	
	/**
	 * ��ѯһ�������������¼��������֧��Ԥ���룩
	 * @param sql
	 * @param types
	 * @param ObjectValues
	 * @return
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public int executeQueryForCount(String sql) throws ClassNotFoundException, SQLException{

    	Connection connect =this.getConnection();
		Statement stmt = connect.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		if(rs.next()){
			return rs.getInt(1);
		}
		this.releaseConnection(rs, stmt, connect);
		return 0; 
	}
	/**
	 * ��ѯһ�������������¼������֧��Ԥ���룩
	 * @param sql
	 * @param types
	 * @param ObjectValues
	 * @return
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public int executeQueryForCount(String sql , int[] types,Object[] ObjectValues) throws ClassNotFoundException, SQLException{

		Connection connect = this.getConnection();
		PreparedStatement pst =  connect.prepareStatement(sql);
		
		if( types != null ){
			for(int i=0;i<types.length;i++){
				switch( types[i] ){
				case Types.VARCHAR:
					pst.setString(i+1, String.valueOf( ObjectValues[i] ) );
					break;
				case Types.INTEGER:
					pst.setInt(i+1, Integer.parseInt( String.valueOf( ObjectValues[i] ) ));
					break;
				case Types.BIGINT:
			    	pst.setLong(i+1, Long.valueOf(String.valueOf( ObjectValues[i] )));
			    	break;
				case Types.DOUBLE:
					pst.setDouble(i+1, Double.valueOf(String.valueOf( ObjectValues[i] )));
					break;
				case Types.DATE:
					pst.setDate(i+1, Date.valueOf(String.valueOf( ObjectValues[i] )));
					break;
				case Types.TIMESTAMP:
					pst.setTimestamp(i+1, Timestamp.valueOf(String.valueOf( ObjectValues[i] )));
					break;
				}
				
			}
		}

		ResultSet rs = pst.executeQuery();
		if(rs.next()){
			return rs.getInt(1);
		}
		this.releaseConnection(rs, pst, connect);
		return 0; 
	}
	/**
	 * ��ResultSet�еĽ����װ��list��װMap�Ľṹ
	 * @author 		������
	 * @time		2011-05-11
	 * @param		 rs
	 * @return
	 * @throws SQLException
	 */
	private List<Map<String, Object>> rsToList( ResultSet rs ) throws SQLException{
		List<Map<String, Object>> row = new ArrayList<Map<String, Object>>();
		 while (rs.next()) {
			 Map<String, Object> col = new HashMap<String, Object>();
			 for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
				 //System.out.println(  rs.getMetaData().getColumnType(i)  );
				 switch( rs.getMetaData().getColumnType(i) ){
			 	 case Types.VARCHAR:
			 		col.put(rs.getMetaData().getColumnName(i), rs.getString(i));
			 		break;
			 	 case Types.INTEGER:
				 	col.put(rs.getMetaData().getColumnName(i), rs.getInt(i));
				 	break;	
			 	 case Types.BLOB:
			 		InputStream in = rs.getBinaryStream(i);
				 	col.put(rs.getMetaData().getColumnName(i), in );
				 	break;
			 	case Types.BIGINT:
			 		col.put(rs.getMetaData().getColumnName(i), rs.getLong(i));
			    	break;
				case Types.DOUBLE:
					col.put(rs.getMetaData().getColumnName(i),  rs.getDouble(i));
					break;
				case Types.DATE:
					col.put(rs.getMetaData().getColumnName(i), rs.getDate(i));
					break;
				case Types.TIMESTAMP:
					col.put(rs.getMetaData().getColumnName(i), rs.getTimestamp(i));
					break;
				 default:
					 col.put(rs.getMetaData().getColumnName(i), rs.getString(i));
				 	break;	
				 }
				 
			 }
			row.add(col);
		}
		 return row;
	}
	
	@SuppressWarnings("unused")
	private void releaseConnection(Connection connect) throws SQLException{
	    try {
	        if (connect != null && !connect.isClosed()){
	        	connect.close();
	        }
	    } catch (SQLException se){
	    	log.info("Close the connection encounter error!\n" + se.getMessage());
	        throw new SQLException("�ر������쳣��");
	    }
	}
	
	private void releaseConnection(Statement stmt, Connection connect) throws SQLException{
	    try {
	        if (stmt != null){
	        	stmt.close();
	        }
	        if (connect != null && !connect.isClosed()){
	        	connect.close();
	        }
	    } catch (SQLException se){
	    	log.info("Close the connection encounter error!\n" + se.getMessage());
	        throw new SQLException("�ر������쳣��");
	    }
	}
	private void releaseConnection(PreparedStatement pst, Connection connect) throws SQLException{
	    try {
	        if (pst != null){
	            pst.close();
	        }
	        if (connect != null && !connect.isClosed()){
	        	connect.close();
	        }
	    } catch (SQLException se){
	    	log.info("Close the connection encounter error!\n" + se.getMessage());
	        throw new SQLException("�ر������쳣��");
	    }
	}
	
	private void releaseConnection(ResultSet rs, Statement stmt, Connection connect) throws SQLException{
	    try {
	        if (rs != null){
	            rs.close();
	        }
	        if (stmt != null){
	        	stmt.close();
	        }
	        if (connect != null && !connect.isClosed()){
	        	connect.close();
	        }
	    } catch (SQLException se){
	    	log.info("Close the connection encounter error!\n" + se.getMessage());
	        throw new SQLException("�ر������쳣��");
	    }
	}
	private void releaseConnection(ResultSet rs, PreparedStatement pst, Connection connect) throws SQLException{
	    try {
	        if (rs != null){
	            rs.close();
	        }
	        if (pst != null){
	            pst.close();
	        }
	        if (connect != null && !connect.isClosed()){
	        	connect.close();
	        }
	    } catch (SQLException se){
	    	log.info("Close the connection encounter error!\n" + se.getMessage());
	        throw new SQLException("�ر������쳣��");
	    }
	}
	


		
		



	

	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
		
		JdbcDaoImpl dao = new JdbcDaoImpl();
		int[] types1 = {Types.VARCHAR};
		String[] objValues1 = {"%"};
		
		
		List<Map<String, Object>> list = dao.executeQueryForList("select * from  crawler_fl ", types1, objValues1);
		
		System.out.println("list.size()==="+list.size());
		for (int i = 0; i < list.size(); i++) {
			System.out.println(  list.get(i) );
		}

	}
	
	
//--------------------------------SET/GET----------------------------------------------
	public String getSdbdriver() {
		return sdbdriver;
	}
	public void setSdbdriver(String sdbdriver) {
		this.sdbdriver = sdbdriver;
	}
	public String getSconnStr() {
		return sconnStr;
	}
	public void setSconnStr(String sconnStr) {
		this.sconnStr = sconnStr;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	
}
