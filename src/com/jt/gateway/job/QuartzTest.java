/**
 * @Description: 
 *
 * @Title: QuartzTest.java
 * @Package com.joyce.quartz.main
 * @Copyright: Copyright (c) 2014
 *
 * @author Comsys-LZP
 * @date 2014-6-26 ����03:35:05
 * @version V2.0
 */
package com.jt.gateway.job;



/**
 * @Description: ������
 *
 * @ClassName: QuartzTest
 * @Copyright: Copyright (c) 2014
 *
 * @author Comsys-LZP
 * @date 2014-6-26 ����03:35:05
 * @version V2.0
 */
public class QuartzTest {
	public static void main(String[] args) {
		try {
			String job_name = "��̬�������";
			System.out.println("��ϵͳ��������ʼ(ÿ1�����һ��)...");  
			QuartzManager.addJob(job_name, QuartzJob.class, "0/1 * * * * ?");  
			
//			Thread.sleep(5000);  
//			System.out.println("���޸�ʱ�䡿��ʼ(ÿ2�����һ��)...");  
//			QuartzManager.modifyJobTime(job_name, "10/2 * * * * ?");  
//			Thread.sleep(6000);  
//			System.out.println("���Ƴ���ʱ����ʼ...");  
//			QuartzManager.removeJob(job_name);  
//			System.out.println("���Ƴ���ʱ���ɹ�");  
//			
//			System.out.println("���ٴ���Ӷ�ʱ���񡿿�ʼ(ÿ10�����һ��)...");  
//			QuartzManager.addJob(job_name, QuartzJob.class, "*/10 * * * * ?");  
//			Thread.sleep(60000);  
//			System.out.println("���Ƴ���ʱ����ʼ...");  
//			QuartzManager.removeJob(job_name);  
//			System.out.println("���Ƴ���ʱ���ɹ�");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
