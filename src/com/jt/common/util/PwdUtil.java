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
		ConfigManager  configManager=new ConfigManager("jdbc.properties");
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
	 		if(!saveFile.exists()){
	 			createFile(fl);
	 		}
			pwriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(saveFile), "UTF-8")));
			pwriter.println(pwdjm);	
			pwriter.close();
		} catch (IOException e) {
			e.printStackTrace();
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
	
	
	
	  public static boolean createFile(String destFileName) {  
	        File file = new File(destFileName);  
	        if(file.exists()) {  
	            System.out.println("创建单个文件" + destFileName + "失败，目标文件已存在！");  
	            return false;  
	        }  
	        if (destFileName.endsWith(File.separator)) {  
	            System.out.println("创建单个文件" + destFileName + "失败，目标文件不能为目录！");  
	            return false;  
	        }  
	        //判断目标文件所在的目录是否存在  
	        if(!file.getParentFile().exists()) {  
	            //如果目标文件所在的目录不存在，则创建父目录  
	            System.out.println("目标文件所在目录不存在，准备创建它！");  
	            if(!file.getParentFile().mkdirs()) {  
	                System.out.println("创建目标文件所在目录失败！");  
	                return false;  
	            }  
	        }  
	        //创建目标文件  
	        try {  
	            if (file.createNewFile()) {  
	                System.out.println("创建单个文件" + destFileName + "成功！");  
	                return true;  
	            } else {  
	                System.out.println("创建单个文件" + destFileName + "失败！");  
	                return false;  
	            }  
	        } catch (IOException e) {  
	            e.printStackTrace();  
	            System.out.println("创建单个文件" + destFileName + "失败！" + e.getMessage());  
	            return false;  
	        }  
	    }  
	     
	
	
   public static void main(String[] args) throws IOException {
	  createPwd();
	//   System.out.println(getPwd());
	   createFile("E:/jksd/asda/pwd.key");
		
}
}
