package com.jt.test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Temp {
	private Integer test;

	public Integer getTest() {
		return test;
	}

	public void setTest(Integer test) {
		this.test = test;
	}
	
	
	public boolean isZs(int N){
		boolean isZhishu=true;
		for(int i=1;i<=N;i++){
			if(N%i==0){
				if(i!=1&&i!=N){
					isZhishu=false;
					break;					
				}
			}
		}
		return isZhishu;
	}
	public void getZs(int N){
		for(int i=1,j=1;i<=N;j++){
			
			if(isZs(j)){
				System.out.println(j);
				i++;
			}
		}
	}

	public static void main(String[] args) throws Exception {
		Temp t=new Temp();
		t.getZs(8);

	}
}
