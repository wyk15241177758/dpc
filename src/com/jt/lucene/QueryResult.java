package com.jt.lucene;
import java.util.List;

/**
 * @��Ŀ���ƣ�lucene
 * @�����ƣ�QueryResult
 * @�������������
 * @�����ˣ�YangChao
 * @����ʱ�䣺2016��8��31�� ����4:56:24
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