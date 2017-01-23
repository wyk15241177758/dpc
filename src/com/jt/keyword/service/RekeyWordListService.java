package com.jt.keyword.service;

import java.util.List;
import java.util.Map;

import com.jt.keyword.bean.CountResult;
import com.jt.keyword.bean.QueryResutList;

public interface RekeyWordListService {
	/**
	 * MySql统计分类
	 * @param keyword
	 * @return
	 */
	public   List<CountResult>  flcountResults(String keyword);
    /**
     * MySql统计来源下分类
     * @param keyword
     * @param source
     * @return
     */
	public   List<CountResult> flcountResults(String keyword, String source);
    /**
     * MySql统计来源
     * @param keyword
     * @return
     */
	public  List<CountResult> sourcecountResults(String keyword);
    /**
     * MySql统计分类下来源
     * @param keyword
     * @param fl
     * @return
     */
	public  List<CountResult> sourcecountResults(String keyword, String fl);
    /**
     * MySql统计分类和来源
     * @param keyword
     * @param fl
     * @param source
     * @return
     */
	public  List<CountResult> countResults(String keyword, String fl, String source);
	
	
	
	/**
	 * LUCENE统计
	 * @param keyword
	 * @return
	 */
	public   Map<String, List<CountResult>>  countResultsLC(String keyword);
    /**
     * LUCENE统计来源下分类
     * @param keyword
     * @param source
     * @return
     */
	public   Map<String, List<CountResult>> flcountResultsLC(String keyword, String source);
   
    /**
     * LUCENE统计分类下来源
     * @param keyword
     * @param fl
     * @return
     */
	public  Map<String, List<CountResult>> sourcecountResultsLC(String keyword, String fl);
    /**
     * LUCENE统计分类和来源
     * @param keyword
     * @param fl
     * @param source
     * @return
     */
	public  Map<String, List<CountResult>> countResultsLC(String keyword, String fl, String source);
	QueryResutList queryByKeyWord(String keyword, int pageNum, int pageSize);
	QueryResutList queryByKS(String keyword, String source, int pageNum,
			int pageSize);
	QueryResutList queryByKF(String keyword, String fl, int pageNum,
			int pageSize);
	QueryResutList queryByKSF(String keyword, String source, String fl,
			int pageNum, int pageSize);
	
}
