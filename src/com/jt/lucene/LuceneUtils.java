package com.jt.lucene;
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
public class LuceneUtils {
	private static Logger logger = Logger.getLogger(LuceneUtils.class);
	private static Directory directory; 
	private static Analyzer analyzer;
	static {
		try {
			directory = FSDirectory.open(Paths.get("D:\\testindex"));
			// analyzer = new StandardAnalyzer();
			analyzer = new SmartChineseAnalyzer();
		} catch (Exception e) {
			logger.error("LuceneUtils error!", e);
		}
	}
	  public static String escapeQueryChars(String s) {
		    StringBuilder sb = new StringBuilder();
		    for (int i = 0; i < s.length(); i++) {
		      char c = s.charAt(i);
		      // These characters are part of the query syntax and must be escaped
		      if (c == '\\' || c == '+' || c == '-' || c == '!'  || c == '(' || c == ')' || c == ':'
		        || c == '^' || c == '[' || c == ']' || c == '\"' || c == '{' || c == '}' || c == '~'
		        || c == '*' || c == '?' || c == '|' || c == '&'  || c == ';' || c == '/'
		        || Character.isWhitespace(c)) {
		        sb.append('\\');
		      }
		      sb.append(c);
		    }
		    return sb.toString();
		  }

	public static Directory getDirectory() {
		return directory;
	}

	public static Analyzer getAnalyzer() {
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