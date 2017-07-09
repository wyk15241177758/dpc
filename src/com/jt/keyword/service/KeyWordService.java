package com.jt.keyword.service;

import java.util.List;

import com.jt.keyword.bean.KeyWord;

public interface KeyWordService {
	void addKeyword(String keyvalue, Integer pId);

	void updateKeyword(String keyvalue, Integer id);

	boolean isExist(String keyvalue);

	String queryPage(String keyvalue, Integer pageNum, Integer pageSize,
			Integer pid);

	String queryPage(String keyvalue);

	String queryPage(String keyvalue, Integer pageNum, Integer pageSize);

	List<KeyWord> queryAllFirst();

	List<KeyWord> queryAll();

	void delete(Class cla, String ids);

	KeyWord queryById(Class cla, Integer id);

}
