package com.jt.keyword.task;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apdplat.qa.parser.WordParser;
import org.apdplat.word.segmentation.Word;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jt.nlp.service.NlpService;
/**
 * 热词分析统计
 * @author 邹许红
 * @date   2016-12-05
 */
public class HotTask   extends  Thread{
	private static final Logger LOG = LoggerFactory.getLogger(HotTask.class);
	private static final  String reg = "[\\u4e00-\\u9fa5A-Z]+";
   public HotTask(){
	   
   }
	
	
   public  void  parseWords(List<Word> words){
	   if(words==null||words.size()==0){
          return;		}
	   
	   for (int i = 0; i < words.size(); i++) {
		   Word word = words.get(i);
		   String wordtext = word.getText();
		   if(wordtext!=null&&wordtext.length()>=2&&wordtext.length()<=5){
			   if(StringUtils.isNotBlank(wordtext)&&wordtext.matches(reg)){
					ParamUtil.parseMapBykey(wordtext);
				}   
		   }
		
	}
	   
   }


	@Override
	public void run() {
		try {
			while(!ParamUtil.titleList.isEmpty()){
				String title=ParamUtil.titleList.poll();
				LOG.debug("title:"+title);
				 List<Word> words=null;
				if(StringUtils.isNotBlank(title)){
					words = WordParser.parse(title);
				 }else{
					 continue;
				 }
				if(words==null||words.size()==0){
					 continue;
				}
				
				parseWords(words);

			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ParamUtil.parselatch.countDown();
	}

	
	
	
	
	
	
}
