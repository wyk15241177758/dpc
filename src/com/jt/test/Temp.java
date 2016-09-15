package com.jt.test;

import java.io.File;

import com.jt.gateway.util.FileUtil;


public class Temp {
public static void main(String[] args) {
	File file=new File("D:\\LUCENEINDEX_new");
	System.out.println(FileUtil.deleteDir(file));;
}
}
