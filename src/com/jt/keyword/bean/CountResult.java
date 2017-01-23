package com.jt.keyword.bean;
/**
 * 统计结果
 * @作者 邹许红
 * @日期 2017-01-15
 */
public class CountResult {
	
	public  String  wordValue;
	public  int     num;
	public CountResult(){
		
	}
	
	public CountResult(String wordValue, int num) {
		this.wordValue = wordValue;
		this.num = num;
	}

	public String getWordValue() {
		return wordValue;
	}
	public void setWordValue(String wordValue) {
		this.wordValue = wordValue;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	
	public int CompareTo(CountResult other)
    {
        if (other == null)
            return 1;
        int value = this.num - other.num;
        if (value == 0)
            value =1;
        return value;
    }

	@Override
	public String toString() {
		return "CountResult [wordValue=" + wordValue + ", num=" + num + "]";
	}
	

}
