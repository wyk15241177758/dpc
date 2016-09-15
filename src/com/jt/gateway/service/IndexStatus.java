package com.jt.gateway.service;
/**
 * ����ģʽ���������״̬���Ƿ����
 * @author zxb
 *
 * 2016��9��15��
 */
public class IndexStatus {
	private int searchThread;
	private boolean searchEnable;
	private IndexStatus(){
		this.searchThread=0;
		this.searchEnable=true;
	}
	
	private static IndexStatus status=new IndexStatus();
	public static IndexStatus getStatus(){
		return status;
	}
	
	public void beginSearch(){
		this.searchThread++;
	}
	
	public void EndSearch(){
		this.searchThread--;
	}
	
	public int getSearchThread() {
		return searchThread;
	}

	public boolean IsSearchEnable(){
		return this.searchEnable;
	}

	public void setSearchEnable(boolean searchEnable) {
		this.searchEnable = searchEnable;
	}
	
	
}
