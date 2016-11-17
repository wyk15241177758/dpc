package com.jt.bean.gateway;

public class PageMsg {
	private boolean sig;
	private Object msg;

	public PageMsg(){
		
	}
	
	public PageMsg(boolean sig, String msg) {
		super();
		this.sig = sig;
		this.msg = msg;
	}
	public boolean isSig() {
		return sig;
	}
	public void setSig(boolean sig) {
		this.sig = sig;
	}
	public String getMsg() {
		return msg.toString();
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public void setMsg(Object msg) {
		this.msg = msg;
	}
	@Override
	public String toString() {
		return "PageMsg [sig=" + sig + ", msg=" + msg + "]";
	}
	
}
