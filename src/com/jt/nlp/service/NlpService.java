package com.jt.nlp.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apdplat.qa.parser.WordParser;
import org.apdplat.qa.questiontypeanalysis.patternbased.MainPartExtracter;
import org.apdplat.qa.questiontypeanalysis.patternbased.QuestionStructure;
import org.apdplat.word.segmentation.Word;


public class NlpService {
	 private MainPartExtracter mainPartExtracter;
	 private Set<String> dropPartOfSpeech;
	 public NlpService(){
	        mainPartExtracter = new MainPartExtracter();
	        //默认丢弃词组和地名类型
	        dropPartOfSpeech=new HashSet<String>();
	        dropPartOfSpeech.add("l");
	        dropPartOfSpeech.add("ns");
	 }
	 //获得标注词性的主谓宾
	 public List<Word> getMainPartWords(String question){
		QuestionStructure qs = mainPartExtracter.getMainPart(question);
		List<Word> list=WordParser.parse(qs.getMainPart());
		return list;
	 }
	 /**
	  * 主方法，获得过滤之后的主谓宾
	  * @param question
	  * @return
	  */
	 public List<String> getSearchWords(String question){
		 List<String>list=new ArrayList<String>();
		 List<Word> mainPartWords=getMainPartWords(question);
		 for(Word word:mainPartWords){
			 String temp=doFilterWord(word);
			 if(temp!=null){
				 list.add(temp);
			 }
		 }
		 return list;
	 }
	 //处理过滤检索词
	 private String doFilterWord(Word word){
		 //词
		 String text=word.getText();
		 //词性
		 String partOfSpeech=word.getPartOfSpeech().getPos();
		 String filteredWord=text;
		 //过滤只有一个字的词，返回null
		 if(text.length()<2){
			 return null;
		 }
		 //过滤掉词性为dropPartOfSpeech的词
		 if(dropPartOfSpeech.contains(partOfSpeech)){
			 return null;
		 }
		 return filteredWord;
	 }
	 public static void main(String[] args) {
		 NlpService service=new NlpService();
		String [] arr={"杨浦有什么地方好玩？","孩子转学需要什么手续？","我想自主创业，政府有什么政策？","噪音扰民怎么办?","上海怎么办理居住证 "};
        for(String str:arr){
        	System.out.println("问题:"+str+"----------begin");
        	List<String> list=service.getSearchWords(str);
        	for(String s:list){
        		System.out.println(s);
        	}
        	System.out.println("问题:"+str+"----------end");
        }
	}
}
