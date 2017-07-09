package com.jt.keyword.test;

import java.io.File;
import java.util.List;

import org.apdplat.qa.parser.WordParser;
import org.apdplat.qa.util.Tools;
import org.apdplat.word.WordSegmenter;
import org.apdplat.word.segmentation.SegmentationAlgorithm;
import org.apdplat.word.segmentation.Word;
import org.apdplat.word.util.WordConfTools;

public class Test {
public static void main(String[] args) {
	  String appPath = Tools.getAppPath(WordParser.class);
      String confFile = appPath + "/web/dic/word_v_1_3/word.local.conf";
      if(!new File(confFile).exists()){
          confFile = appPath + "/jar/dic/word_v_1_3/word.local.conf";
      }
      if(new File(confFile).exists()){
          WordConfTools.forceOverride(confFile);}



	List<Word> words =
	        WordSegmenter.seg("区体育局进一步推动群众体育、竞技体育和体育产业协调发展 ",SegmentationAlgorithm.MaximumMatching);
	System.out.println(words);
	words = WordSegmenter.seg("区体育局进一步推动群众体育、竞技体育和体育产业协调发展 ",SegmentationAlgorithm.ReverseMaximumMatching);
	System.out.println(words);
	words = WordSegmenter.seg("区体育局进一步推动群众体育、竞技体育和体育产业协调发展 ",SegmentationAlgorithm.MinimumMatching);
	System.out.println(words);
	words = WordSegmenter.seg("区体育局进一步推动群众体育、竞技体育和体育产业协调发展 ",SegmentationAlgorithm.ReverseMinimumMatching);
	System.out.println(words);
	words = WordSegmenter.seg("区体育局进一步推动群众体育、竞技体育和体育产业协调发展 ",SegmentationAlgorithm.BidirectionalMaximumMatching);
	System.out.println(words);
	words = WordSegmenter.seg("区体育局进一步推动群众体育、竞技体育和体育产业协调发展 ",SegmentationAlgorithm.BidirectionalMinimumMatching);
	System.out.println(words);
	words = WordSegmenter.seg("区体育局进一步推动群众体育、竞技体育和体育产业协调发展 ",SegmentationAlgorithm.BidirectionalMaximumMinimumMatching);
	System.out.println(words);
	
	
	
	
	
	

}
}
