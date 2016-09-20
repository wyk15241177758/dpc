package com.jt.gateway.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileUtil {
	/**
     * 递归删除目录下的所有文件及子目录下所有文件
     * @param dir 将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful.
     *                 If a deletion fails, the method stops attempting to
     *                 delete and returns "false".
     */
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }
    //判断文件是否为空
    public static boolean isContentNull(File file){
    	boolean isNull=true;
    	if(file.exists()&&file.isFile()){
    		FileInputStream fis=null;
    		try {
				fis=new FileInputStream(file);
				if(fis.read()==-1){
					return true;
				}else{
					return false;
				}
				
			} catch (FileNotFoundException e) {
				return true;
			}catch(IOException e){
				return true;
			}finally{
				try {
					if(fis!=null){
						fis.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
    		
    	}
    	return isNull;
    }
}
