package com.jt.test.nlp;

import edu.stanford.nlp.ling.Label;
import edu.stanford.nlp.ling.WordTag;

public class Test {
public static void main(String[] args) {
	Label label=WordTag.factory().newLabel("test");
	System.out.println(label.value());
}
}
