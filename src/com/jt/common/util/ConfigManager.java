package com.jt.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * <p>配置文件管理类,根据配置文件名和文件可读出配置文件信息
 * 支持多配置文件读取，每个配置文件ConfigManager实例在程序中保持唯一实例
 * 当配置文件被修改时，会重新去读取配置文件</p>
 */
public final class ConfigManager {
	private static final Logger LOGGER = LoggerFactory.getLogger(ConfigManager.class);
    //文件名
    private String fileName;
    //存储配置文件
    private File file = null;
    //记录文件的修改时间
    private long lastModifiedTime = 0;
    //存储配置值
    private Properties props = null;
    //存储ConfigManager对象实例
    private static HashMap<String,ConfigManager> hashMap = new HashMap<String,ConfigManager>();
    private static Object hashMapLock=new Object();
    /**
     * 私有的构造函数,保证外界无法直接实例化
     * @param v_File File 文件名也作为实例在HashMap中的KEY值
     * @throws IOException,Exception
     */
    public ConfigManager(File v_File) throws IOException {
        this.file = v_File;
        this.fileName = this.file.getName();
        lastModifiedTime = this.file.lastModified(); //得到文件的最后修改时间
        props = new Properties();
        props.load(new InputStreamReader(new FileInputStream(this.file),"UTF-8")); //把配置信息读到Properties中
    }

    public ConfigManager(String filename) throws IOException {
        props = new Properties();
        props.load(ConfigManager.class.getResourceAsStream("/" + filename)); //把配置信息读到Properties中
    }



    /**
     * 得到配置文件的值
     * @return Properties
     */
    public Properties getProperties() {
        return props;
    }

    /**
     * 根据文件名得到该配置类的一个实例
     * @param v_FileName String 不带扩展名的文件名
     * @throws IOException 抛出IO异常
     * @return ConfigManager
     */
    @SuppressWarnings("unused")
	public static ConfigManager getInstance(String v_FileName) throws
            IOException {
        if (v_FileName == null) { //判断文件名是否为空
            throw new IOException("文件名不能为空");
        }
        File v_TempFile = null;
        //得到文件的URL
        URL v_Url = ConfigManager.class.getResource("/" + v_FileName);

        LOGGER.debug(v_Url.toString());

        if (v_Url == null) {
            throw new IOException("找不到指定文件:" + v_FileName);
        }

        try {
            v_TempFile = new File(new URI(v_Url.toString())); //实例化一个文件对象
        } catch (Exception e) {
                return getInstance2(v_FileName); //调用另外一个静态方法
        }
        return getInstance(v_TempFile); //调用另外一个静态方法
    }

    /**
     * 得到该配置文件的一个实例
     * @param file File  配置文件
     * @throws IOException 抛出IO异常
     * @return ConfigManager
     */
    public static ConfigManager getInstance2(String filename) throws
            IOException {
        //判断文件是否为空
        String v_Key = filename; //把文件名作为配置类的实例的KEY
        ConfigManager v_Cfg = null;
        synchronized(hashMapLock){
        	 if (hashMap.containsKey(v_Key)) { //检查是否已经有该实例,保证每个配置文件对应一个唯一实例
                 v_Cfg = (ConfigManager) hashMap.get(v_Key); //返回已有的实例
             } else {
                 v_Cfg = new ConfigManager(filename); //生成一个新实例
                 hashMap.put(filename, v_Cfg); //保存到HashMap中
             }
        }
        return v_Cfg;
    }


    /**
     * 得到该配置文件的一个实例
     * @param v_File File  配置文件
     * @throws IOException 抛出IO异常
     * @return ConfigManager
     */
    public static ConfigManager getInstance(File v_File) throws
            IOException {
        //判断文件是否为空
        if (v_File == null) {
            throw new IOException("文件不能为空");
        }
        String v_Key = v_File.getName(); //把文件名作为配置类的实例的KEY
        ConfigManager v_Cfg = null;
        synchronized(hashMapLock){
        	if (hashMap.containsKey(v_Key)) { //检查是否已经有该实例,保证每个配置文件对应一个唯一实例
                v_Cfg = (ConfigManager) hashMap.get(v_Key); //返回已有的实例
            } else {
                v_Cfg = new ConfigManager(v_File); //生成一个新实例
                hashMap.put(v_Key, v_Cfg); //保存到HashMap中
            }
        }
        return v_Cfg;
    }

    /**
     * 得到指定KEY的值
     * @param name String 配置值中的KEY
     * @return Object
     */
    public Object getConfigItem(String v_Key) throws IOException {

    	if(this.file==null){
    		Object val = props.getProperty(v_Key); //取出值
    		//System.out.println(val.toString());
            return val;
    	}

        long v_NewTime = this.file.lastModified();
        //System.out.println(this.v_File.getName());
        if (v_NewTime == 0) {
            if (lastModifiedTime == 0) {
            	LOGGER.error("{} file does not exist!",fileName);
            } else {
            	LOGGER.error("{} file was deleted!",fileName);
            }
            return null;
        } else if (v_NewTime > lastModifiedTime) { //判断文件是否被修改过
            props.clear(); //清除原来的配置信息
            props.load(new InputStreamReader(new FileInputStream(this.file),"GB18030")); //把配置信息读到Properties中
        }
        lastModifiedTime = v_NewTime;
        return props.getProperty(v_Key); //取出值
    }

    /**
     * 得到指定KEY的值
     * @param name String 配置值中的KEY
     * @return Object
     */
    public Object getConfigItem2(String v_Key) throws IOException {
        return props.getProperty(v_Key); //取出值
    }
}
