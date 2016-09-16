package com.jt.test.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jt.test.dao.TestDao;
@Service
public class TestService {
	@Autowired
	private  TestDao dao; 
	public  void  test(){
		dao.test();
		System.out.println("Œ“ «service≤„");
	}

}
