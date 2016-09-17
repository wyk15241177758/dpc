package com.jt.lucene;
import java.util.List;

/**
 * @项目名称：lucene
 * @类名称：QueryResult
 * @类描述：结果集
 * @创建人：YangChao
 * @创建时间：2016年8月31日 下午4:56:24
 * @version 1.0.0
 */
public class QueryResult {
	private int count;
	private List list;

	public QueryResult() {
		super();
	}

	public QueryResult(int count, List list) {
		super();
		this.count = count;
		this.list = list;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List getList() {
		return list;
	}

	public void setList(List list) {
		this.list = list;
	}
}