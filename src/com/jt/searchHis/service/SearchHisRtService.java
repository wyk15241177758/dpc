package com.jt.searchHis.service;

import java.util.List;
import java.util.Map;

import com.jt.searchHis.bean.SearchHis;

/**
 * 内存保存检索历史，达到一定数量则存入数据库，避免每次检索都操作数据库
 */

public interface SearchHisRtService {
	public void add(String question);
	public Map<String ,List<SearchHis>> tempList();
	public boolean isLocked();
}
