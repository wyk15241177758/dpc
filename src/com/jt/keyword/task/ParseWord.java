package com.jt.keyword.task;

public class ParseWord {
	public  Long xqId;
	public  String words;
	
	
	
	public ParseWord(Long xqId, String words) {
		this.xqId = xqId;
		this.words = words;
	}
	public ParseWord() {
		// TODO Auto-generated constructor stub
	}
	public Long getXqId() {
		return xqId;
	}
	public void setXqId(Long xqId) {
		this.xqId = xqId;
	}
	public String getWords() {
		return words;
	}
	public void setWords(String words) {
		this.words = words;
	}
	

}
