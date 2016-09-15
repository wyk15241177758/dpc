package com.jt.gateway.dao;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class GwPropertyDao {
	private Properties pro;
	private OutputStream out;
	private InputStream in;
	private File file;
	public GwPropertyDao(String proName) throws IOException{
		pro=new Properties(); 
		file=new File(proName);
		if(!file.exists()){
			file.createNewFile();
		}
	}
	public void add(String key,String value) throws FileNotFoundException{
		out=new BufferedOutputStream(new FileOutputStream(file));
		pro.setProperty(key, value);
	}
	public void del(String key) throws FileNotFoundException{
		out=new BufferedOutputStream(new FileOutputStream(file));
		pro.remove(key);
	}
	public void update(String key ,String value) throws FileNotFoundException{
		out=new BufferedOutputStream(new FileOutputStream(file));
		pro.setProperty(key, value);
	}
	public String get(String key) throws IOException{
		in=new BufferedInputStream(new FileInputStream(file));
		pro.load(in);
		String value= pro.getProperty(key);
		if(in!=null){
			in.close();
		}
		return value;
	}
	public void save() throws IOException{
		pro.store(out, "");
		if(out!=null){
			out.close();
		}
	}
	
	public static void main(String[] args) {
		try {
			GwPropertyDao dao=new GwPropertyDao("test.pro");
			dao.add("name2", "zxb1");
			dao.save();
			
			System.out.println(dao.get("name1"));;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
