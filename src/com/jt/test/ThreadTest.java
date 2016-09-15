package com.jt.test;

public class ThreadTest  extends Thread{
	
	@Override
	public void run() {
		try {
			System.out.println(this.getState());
			Thread.sleep(2000l);
			System.out.println("内部线程执行结束");

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void test(){
		this.start();
		try {
			this.wait();
			System.out.println("主线程结束111");

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
//class Thread2 extends Thread{
//	
//	@Override
//	public void run() {
//		try {
//			Thread.sleep(2000l);
//			System.out.println("内部线程执行结束");
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//	}
//	
//}
	public static void main(String[] args) {
		ThreadTest tt=new ThreadTest();
		tt.start();
		
		try {
			Thread.sleep(3000l);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(tt.getState());

	}
}
