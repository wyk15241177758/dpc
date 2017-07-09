package com.jt.test.nlp;

import java.util.List;

import org.apdplat.word.WordSegmenter;
import org.apdplat.word.recognition.StopWord;
import org.apdplat.word.segmentation.Word;

import com.jt.nlp.service.NlpService;

import edu.stanford.nlp.ling.Label;
import edu.stanford.nlp.ling.WordTag;

public class Test {
public static void main(String[] args) {
	String question="廉租房和低保户可以买车吗";
	//先排除停用词
	System.out.println(WordSegmenter.seg(question)+"\n------\n");
	System.out.println(WordSegmenter.segWithStopWords(question));
//	System.out.println(StopWord.is("我"));
}
}
