package com.jt.common.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import org.apache.commons.lang.StringUtils;
/**
 * 密码
 * @author zxh
 * @date   2016/10/08
 */
public class PwdUtil {
	public  static   String   getPwdFilePath() throws IOException{
		ConfigManager  configManager=new ConfigManager("conf.properties");
		return configManager.getConfigItem2("PWD_FILE_PATH").toString();
	}
	public  static  void   createPwd(){
		createPwd(null);
	}
	public  static  void   createPwd(String  pwd){
		if(StringUtils.isBlank(pwd))
			pwd="123456";
	   String   pwdjm=	EncryptUtils.encodeBase64(pwd);
	   try {
		String fl=getPwdFilePath();
		    PrintWriter pwriter = null;	
	 		File saveFile =new File(fl);
			pwriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(saveFile), "UTF-8")));
			pwriter.println(pwdjm);	
			pwriter.close();
		} catch (IOException e) {
		}  
	}
	
	
	public  static  String   getPwd(){
		InputStreamReader read=null;
		BufferedReader reader=null;
		try {
			String fl=getPwdFilePath();
			read = new InputStreamReader(new FileInputStream(new File(fl)),"UTF-8");
			reader = new BufferedReader(read);
			String line="";
			while ((line = reader.readLine()) != null) {
				return  EncryptUtils.decodeBase64(line);
				  }			
		} catch (IOException e) {
			
		}finally{
				try {
					
					if(reader!=null)
					reader.close();
					if(read!=null)
					read.close();
				} catch (IOException e) {
				
				}
		}

		return "123456";
	}
	
	
   public static void main(String[] args) throws IOException {
	   createPwd();
	   System.out.println(getPwd());
		
}
}
