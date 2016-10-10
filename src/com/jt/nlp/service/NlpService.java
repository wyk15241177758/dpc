/**
 * 语义分析相关服务
 */
package com.jt.nlp.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apdplat.qa.questiontypeanalysis.patternbased.MainPartExtracter;
import org.apdplat.qa.questiontypeanalysis.patternbased.QuestionStructure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jt.nlp.util.NlpUtil;

import edu.stanford.nlp.ling.LabeledWord;


public class NlpService {
    private static final Logger LOG = LoggerFactory.getLogger(NlpService.class);

	
	 private MainPartExtracter mainPartExtracter;
	 private Set<String> remainPartOfSpeech;
	 public NlpService(){
	        mainPartExtracter = new MainPartExtracter();
	        //默认丢弃词组和地名类型
	        remainPartOfSpeech=new HashSet<String>();
	        remainPartOfSpeech.add("NR");
	        remainPartOfSpeech.add("NN");
	 }
	 
	 /**
	  *获得标注词性的主谓宾 
	  * @param question
	  * @return
	  */
	 public List<LabeledWord> getMainPartWords(String question){
		QuestionStructure qs = mainPartExtracter.getMainPart(question);
		String mainPart=qs.getMainPart();
		List<LabeledWord> list=mainPartExtracter.getPortOfSpeech(mainPart);
		LOG.info("question=["+question+"] 标注词性的主谓宾=["+list+"]");
		return list;
	 }
	 /**
	  * 主方法，获得过滤之后的主谓宾
	  * @param question
	  * @return
	  */
	 public Set<String> getSearchWords(String question){
		 Set<String>set=new HashSet<String>();
		 //将问题根据标点符合切分为几个问题
		 List<String> splitedQ=NlpUtil.splitQuestion(question);
		 for(String str:splitedQ){
			 List<LabeledWord> mainPartWords=getMainPartWords(str);
			 for(LabeledWord word:mainPartWords){
				 LOG.info("111question=["+question+"] 标注词性的主谓宾=["+word+"]");
				 String temp=doFilterWord(word);
				 if(temp!=null){
					 set.add(temp);
				 }
			 }
		 }
		 return set;
	 }
	 
	 //处理过滤检索词
	 private String doFilterWord(LabeledWord word){
		 //词
		 String text=word.value();
		 //词性
		 String label=word.tag().value();
		 
		 String filteredWord=text;
		 //过滤只有一个字的词，返回null
		 if(text.length()<2){
			 return null;
		 }
		 //仅保留词性为remainPartOfSpeech的词
		 if(remainPartOfSpeech.contains(label)){
			 return filteredWord;
		 }else{
			 return null;
		 }
	 }
	 public static void main(String[] args) {
		 NlpService service=new NlpService();
		String [] arr={"杨浦有什么地方好玩？","孩子转学需要什么手续？","我想自主创业，政府有什么政策？","噪音扰民怎么办?","上海怎么办理居住证 "};
        for(String str:arr){
        	System.out.println("问题:"+str+"----------begin");
        	Set<String> set=service.getSearchWords(str);
        	for(String s:set){
        		System.out.println(s);
        	}
        	System.out.println("问题:"+str+"----------end");
        }
	}
}
