package com.jt.lucene;
import java.io.IOException;
import java.nio.file.Paths;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 * @项目名称：lucene
 * @类名称：LuceneUtils
 * @类描述：获取分词器和索引位置
 * @创建人：YangChao
 * @创建时间：2016年8月31日 上午9:48:06
 * @version 1.0.0
 */
public class LuceneUtilsGw {
	private static Logger logger = Logger.getLogger(LuceneUtilsGw.class);
	private  Directory directory; 
	private  Analyzer analyzer;
	
	public LuceneUtilsGw(String path) throws IOException{
		directory = FSDirectory.open(Paths.get(path));
		// analyzer = new StandardAnalyzer();
		analyzer = new SmartChineseAnalyzer();
	}
	public  Directory getDirectory() {
		return directory;
	}

	public  Analyzer getAnalyzer() {
		return analyzer;
	}

	public static void closeIndexWriter(IndexWriter indexWriter) {
		if (indexWriter != null) {
			try {
				indexWriter.close();
			} catch (Exception e2) {
				logger.error("indexWriter.close error", e2);
			}
		}
	}

}