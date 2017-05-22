package com.jt.keyword.task;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jt.keyword.bean.CountResult;
import com.jt.keyword.bean.KeyWord;
import com.jt.keyword.util.DataSourceUtil;
import com.jt.lucene.Article;

/**
 * 
 * @author 邹许红
 *
 */
public class KeyWordTaskUtil {
	private static final Logger LOG = LoggerFactory.getLogger(KeyWordTaskUtil.class);
public KeyWordTaskUtil(){
    	
    }
	public KeyWordTaskUtil(List<Article> articles){
    	
    }
    public static String  unionKey(Set<String> keywords){
    	if(keywords==null){
    		return "";
    	}
    	String  words="";
    	int i=0;
    	for (String string : keywords) {
			if(i>0){
				words=words+";";
			}
			words=words+string;
			
           i++;
           if(i==4){
				break;
			}
		}
		return words;
    	
    }
    /**
     * 判断查询语句标签
     * @param queryKey
     * @param keyWords
     * @return
     */
    public  static  String  parse(String queryKey,List<KeyWord>  keyWords){
    	if(queryKey!=null&&keyWords!=null&&keyWords.size()>0)
    	{
    		for (int i = 0; i < keyWords.size(); i++) {
				if(keyWords.get(i)!=null&&keyWords.get(i).getWordvalue()!=null){
					if(queryKey.indexOf(keyWords.get(i).getWordvalue())>=0){
						LOG.info(keyWords.get(i).getWordvalue());
						if (keyWords.get(i).floor==0) {
	                    	   return keyWords.get(i).wordvalue;
						}
                       if (keyWords.get(i).floor==1) {
                    		if(keyWords.get(i).getParent()!=null){
                    		  return keyWords.get(i).getParent().getWordvalue();
							}
						}
					}
					
				}
			}
    	}    	
		return "";
    }
    
    public   List<CountResult>  flcountResults(String keyword){
    	String sql="SELECT COUNT(*) AS NUM,SJFL FROM `crawler_xq` where KEY_WORD LIKE  ?  GROUP BY  SJFL  " ;
    	Connection conn = DataSourceUtil.getConnection();
		PreparedStatement stmt=null;
		ResultSet st=null;
    	
		return null;
    }
  
    
    /**
     * 处理
     * @param articles
     */
    public static void parse(List<Article> articles){
    	if(articles!=null&&articles.size()>0){
    		for(int j=0;j<articles.size();j++){
    		Set<String>  set=new HashSet<String>();
    		String   title=articles.get(j).getTitle();
    		Long     xqid=articles.get(j).getId();
    		LOG.debug("["+title+"|"+xqid+"]");
    		for (int i = 0; i < ParamUtil.keywords.size(); i++) {
				if(ParamUtil.keywords.get(i)!=null&&ParamUtil.keywords.get(i).getWordvalue()!=null){
					if(title.indexOf(ParamUtil.keywords.get(i).getWordvalue())!=-1){
						LOG.info(ParamUtil.keywords.get(i).getWordvalue());
						if (ParamUtil.keywords.get(i).floor==0) {
	                    	   set.add(ParamUtil.keywords.get(i).wordvalue);
						}
                       if (ParamUtil.keywords.get(i).floor==1) {
                    		if(ParamUtil.keywords.get(i).getParent()!=null){
								set.add(ParamUtil.keywords.get(i).getParent().getWordvalue());
							}
						}
					}
					
				}
			}
    		String  word=unionKey(set);
    		LOG.debug(word);
    		if(StringUtils.isNotBlank(word)){
    			ParamUtil.parseWords.add(new ParseWord(xqid, word));
    		}
    		
    		}
    		
    	}
    	
    }
    
	
	public static  List<String>  parseAr(List<KeyWord> keywords){
		 List<String> reKeyWords=new ArrayList<String>();
		 if(keywords==null)
			 return reKeyWords;
		 for (int i = 0; i < keywords.size(); i++) {
			 reKeyWords.add(keywords.get(i).wordvalue);
		}
		 
		 
		return reKeyWords;
		
	}
	
	
	
	
}
