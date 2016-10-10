/**
 * 对外提供QA功能的主服务，由此服务调用nlp和Lucene功能
 */
package com.jt.nlp.service;

public class QAService {

	 /**
	  * 预设场景，留空
	  * @param word
	  * @return
	  * 查询问题是否包含预设场景的词汇，此方法应该在lucene检索和NLP分析之前
	  */
	 private boolean presentScene(){
		 boolean presented=false;
		 return presented;
	 }
	 
}
