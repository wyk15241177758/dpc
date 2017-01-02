package com.jt.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

public class XmlPostTest {
	 public static void TestPost() throws IOException {  
         
	        URL url = new URL("http://localhost:8080/PostTest/servlet/star");  
	        URLConnection connection = url.openConnection();  
	        connection.setDoOutput(true);  
	        OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "8859_1");  
	        out.write("username=kevin&password=12345"); // 向页面传递数据。post的关键所在！  
	        out.flush();  
	        out.close();  
	        // 一旦发送成功，用以下方法就可以得到服务器的回应：  
	        String sCurrentLine;  
	        String sTotalString;  
	        sCurrentLine = "";  
	        sTotalString = "";  
	        InputStream l_urlStream;  
	        l_urlStream = connection.getInputStream();  
	        // 传说中的三层包装阿！  
	        BufferedReader l_reader = new BufferedReader(new InputStreamReader(  
	                l_urlStream));  
	        while ((sCurrentLine = l_reader.readLine()) != null) {  
	            sTotalString += sCurrentLine + "\r\n";  
	  
	        }  
	        System.out.println(sTotalString);  
	          
	    }  
	  
	    public static void main(String[] args) throws IOException {  
	        TestPost();  
	    }  
}
