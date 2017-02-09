package com.jt.test.nlp;

import com.jt.nlp.service.NlpService;

import edu.stanford.nlp.ling.Label;
import edu.stanford.nlp.ling.WordTag;

public class Test {
public static void main(String[] args) {
	NlpService service = new NlpService();
	System.out.println(service.getParticle("市长,公园,刘和生"));;
}
}
