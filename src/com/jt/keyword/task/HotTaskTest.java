package com.jt.keyword.task;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.lang.StringUtils;
import org.apdplat.qa.parser.WordParser;
import org.apdplat.word.segmentation.Word;

import com.jt.nlp.service.NlpService;

import sun.net.www.content.audio.wav;



public class HotTaskTest {
	 /**
     * 默认参数
     */
	static String  url = "jdbc:mysql://192.168.103.62:3306/om_data";
	static String user = "root";
	static String password = "trsadmin";
	@SuppressWarnings("rawtypes")
	public static void main(String[] args) {
		Connection conn = createConnection();
		int i=1;
		while (i<10) {
			getdata(conn, i);
            i++;			
		}
		ParamUtil.totalpools=4;
		ParamUtil.parselatch=new CountDownLatch(ParamUtil.totalpools);
		int j = 0;
		long  begin=System.currentTimeMillis();
		for (; j < ParamUtil.totalpools; j++) {
			HotTask  sd=new HotTask();
			sd.start();
		}
		System.out.println(j);

		try {
			ParamUtil.parselatch.await();
			long  end=System.currentTimeMillis();
           System.out.println(end-begin);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		System.out.println("成功分析完毕");
		ConcurrentHashMap<String, Integer> map = ParamUtil.map;
		   StringBuffer t = new  StringBuffer();
		  for(Map.Entry me : map.entrySet()) {
	            t.append(me.getKey() + ": " + me.getValue() + "\r\n");
	        }
		    PrintWriter pwriter = null;	
	 		File saveFile =new File("D:\\csv.csv");
			try {
				pwriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(saveFile), "UTF-8")));
				pwriter.println(t.toString());	
				pwriter.close();
			} catch (UnsupportedEncodingException | FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public  static   void   getdata(Connection conn ,int page){
		
		PreparedStatement stmt=null;
		ResultSet st=null;	
		String  sql="select IR_URLTITLE  from  urlcontent  LIMIT  ?,?";
		try {
			stmt=conn.prepareStatement(sql);
			int beagin=(page-1)*1000;
			stmt.setInt(1, beagin);
			stmt.setInt(2, 1000);
			st=stmt.executeQuery();
			while (st.next()) {
				
				String title=st.getString("IR_URLTITLE");
				//System.out.println("查："+title);
				if(!ParamUtil.titleList.contains(title)){
					ParamUtil.titleList.offer(title);
				}
				
				
			}
			
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			closeAll(null, stmt, st);
		}
		
		
		
		
	}
	
	/**
	 * 建立连接
	 * 
	 * @return
	 */
	public static Connection createConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		try {
			return DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}
	
	   /**
	    * @释放资源
	    * @param connection
	    * @param pStatement
	    * @param rSet
	    */
	   	public static void closeAll(Connection connection, PreparedStatement pStatement,
	   			ResultSet rSet) {
	   		

	   		try {

	   			if (rSet != null) {
	   				rSet.close();

	   			}
	   			if (pStatement!=null) {
	   				pStatement.close();
	   			}
	   			if (connection != null) {
	   				connection.close();
	   			}

	   		} catch (SQLException e) {
	   			e.printStackTrace();
	   		}
	   	}

}
