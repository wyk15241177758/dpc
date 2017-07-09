package com.jt.keyword.bean;

import java.util.List;

/**
 * 查询结果
 * @作者 邹许红
 * @日期 2017-01-15
 */
public class QueryResutList {
	/**
	 * 当前页码
	 */
	public  int  pageNum;
	/**
	 * 每页数据
	 */
	public  int  pageSize;
	/**
	 * 总页码数
	 */
	public  int  page;
	/**
	 * 总数据量
	 */
	public  int  totalNUm;
	/**
	 * 结果
	 */
	public  List<XqResult> xqResults;
	/**
	 * 分类统计结果
	 */
	public  List<CountResult> flcountResults;
	/**
	 * 数据源分类结果
	 */
	public  List<CountResult> sourcecountResults;
	public int getPageNum() {
		return pageNum;
	}
	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getTotalNUm() {
		return totalNUm;
	}
	public void setTotalNUm(int totalNUm) {
		this.totalNUm = totalNUm;
	}
	public List<XqResult> getXqResults() {
		return xqResults;
	}
	public void setXqResults(List<XqResult> xqResults) {
		this.xqResults = xqResults;
	}
	public List<CountResult> getFlcountResults() {
		return flcountResults;
	}
	public void setFlcountResults(List<CountResult> flcountResults) {
		this.flcountResults = flcountResults;
	}
	public List<CountResult> getSourcecountResults() {
		return sourcecountResults;
	}
	public void setSourcecountResults(List<CountResult> sourcecountResults) {
		this.sourcecountResults = sourcecountResults;
	}
	

	

}
