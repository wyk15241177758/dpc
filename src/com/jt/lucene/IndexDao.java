package com.jt.lucene;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.NumericDocValuesField;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.DocValuesType;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.Scorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;

import com.jt.gateway.util.FileUtil;

/**
 * @项目名称：lucene
 * @类名称：IndexDao
 * @类描述：
 * @创建人：YangChao
 * @创建时间：2016年8月31日 上午10:12:05
 * @version 1.0.0
 */
public class IndexDao {
	private static Logger logger = Logger.getLogger(IndexDao.class);
	private LuceneUtilsGw util;
	private String indexPath;
	
	public IndexDao(String indexPath) throws IOException{
		this.indexPath=indexPath;
		System.out.println("indexPath=["+indexPath+"]");
		util=new LuceneUtilsGw(indexPath);
	}
	
	public void save(Document doc) {
		
		IndexWriter indexWriter = null;
		try {
			IndexWriterConfig config = new IndexWriterConfig(util.getAnalyzer());
			indexWriter = new IndexWriter(util.getDirectory(), config);
			indexWriter.addDocument(doc);
		} catch (Exception e) {
			logger.error("IndexDao.save error", e);
		} finally {
			LuceneUtilsGw.closeIndexWriter(indexWriter);
		}
	}
	
	public void save(Article article) {
		Document doc = DocumentUtils.article2Document(article);
		IndexWriter indexWriter = null;
		try {
			IndexWriterConfig config = new IndexWriterConfig(util.getAnalyzer());
			indexWriter = new IndexWriter(util.getDirectory(), config);
			indexWriter.addDocument(doc);
		} catch (Exception e) {
			logger.error("IndexDao.save error", e);
		} finally {
			LuceneUtilsGw.closeIndexWriter(indexWriter);
		}
	}

	public void delete(String id) {
		IndexWriter indexWriter = null;
		try {
			Term term = new Term("id", id);
			IndexWriterConfig config = new IndexWriterConfig(util.getAnalyzer());
			indexWriter = new IndexWriter(util.getDirectory(), config);
			indexWriter.deleteDocuments(term);// 删除含有指定term的所有文档
		} catch (Exception e) {
			logger.error("IndexDao.save error", e);
		} finally {
			LuceneUtilsGw.closeIndexWriter(indexWriter);
		}
	}

	public void delAfterId(long beginId,String field){
		System.out.println("空实现");
	}
	public boolean deleteAll(){
		boolean status=false;
		File file=new File(indexPath);
		if(file.exists()){
			status=FileUtil.deleteDir(file);
		}
		return status;
	}
	
	public void update(Article article) {
		Document doc = DocumentUtils.article2Document(article);
		IndexWriter indexWriter = null;
		try {
			Term term = new Term("id", article.getId().toString());
			IndexWriterConfig config = new IndexWriterConfig(util.getAnalyzer());
			indexWriter = new IndexWriter(util.getDirectory(), config);
			indexWriter.updateDocument(term, doc);// 先删除，后创建。
		} catch (Exception e) {
			logger.error("IndexDao.save error", e);
		} finally {
			LuceneUtilsGw.closeIndexWriter(indexWriter);
		}
	}

	public QueryResult search(String queryString, int firstResult, int maxResult) {
		List<Article> list = new ArrayList<Article>();
		try {
			DirectoryReader ireader = DirectoryReader.open(util.getDirectory());
			// 2、第二步，创建搜索器
			IndexSearcher isearcher = new IndexSearcher(ireader);

			// 3、第三步，类似SQL，进行关键字查询
			String[] fields = { "title", "content" };
			QueryParser parser = new MultiFieldQueryParser(fields, util.getAnalyzer());
			Query query = parser.parse(queryString);

			TopDocs topDocs = isearcher.search(query, firstResult + maxResult);
			int count = topDocs.totalHits;// 总记录数
			System.out.println("总记录数为：" + topDocs.totalHits);// 总记录数
			ScoreDoc[] hits = topDocs.scoreDocs;// 第二个参数，指定最多返回前n条结果
			// 高亮
			Formatter formatter = new SimpleHTMLFormatter("<font color='red'>", "</font>");
			Scorer source = new QueryScorer(query);
			Highlighter highlighter = new Highlighter(formatter, source);

			// 摘要
//			Fragmenter fragmenter = new SimpleFragmenter(5);
//			highlighter.setTextFragmenter(fragmenter);

			// 处理结果
			int endIndex = Math.min(firstResult + maxResult, hits.length);
			for (int i = firstResult; i < endIndex; i++) {
				Document hitDoc = isearcher.doc(hits[i].doc);
				Article article = DocumentUtils.document2Ariticle(hitDoc);
				//
				String text = highlighter.getBestFragment(util.getAnalyzer(), "content", hitDoc.get("content"));
				if (text != null) {
					article.setContent(text);
				}
				
				String title = highlighter.getBestFragment(util.getAnalyzer(), "title", hitDoc.get("title"));
				if (title != null) {
					article.setTitle(title);
				}
				
				list.add(article);
			}
			ireader.close();
			return new QueryResult(count, list);
		} catch (Exception e) {
			logger.error("IndexDao.search error", e);
		}
		return null;
	}
	
	public List<Document> search(String queryString,String field,String sortField,SortField.Type sortFieldType,boolean reverse ,int firstResult, int maxResult) {
		List<Document> list = new ArrayList<Document>();
		try {
			Sort sort=new Sort(new SortField(sortField, sortFieldType,reverse));//生成排序类
			
			DirectoryReader ireader = DirectoryReader.open(util.getDirectory());
			// 2、第二步，创建搜索器
			IndexSearcher isearcher = new IndexSearcher(ireader);

			// 3、第三步，类似SQL，进行关键字查询
			QueryParser parser = new QueryParser(field, util.getAnalyzer());
			Query query = parser.parse(queryString);

			TopDocs topDocs = isearcher.search(query, firstResult + maxResult,sort);
			ScoreDoc[] hits = topDocs.scoreDocs;// 第二个参数，指定最多返回前n条结果
			// 高亮
//			Formatter formatter = new SimpleHTMLFormatter("<font color='red'>", "</font>");
//			Scorer source = new QueryScorer(query);
//			Highlighter highlighter = new Highlighter(formatter, source);
			
			// 处理结果
			int endIndex = Math.min(firstResult + maxResult, hits.length);
			for (int i = firstResult; i < endIndex; i++) {
				Document hitDoc = isearcher.doc(hits[i].doc);
				list.add(hitDoc);
			}
			ireader.close();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
//	public TopDocs search(String queryString,String field,String sortField,boolean reverse ,int firstResult, int maxResult) {
//		try {
//			DirectoryReader ireader = DirectoryReader.open(util.getDirectory());
//			// 2、第二步，创建搜索器
//			IndexSearcher isearcher = new IndexSearcher(ireader);
//
//			// 3、第三步，类似SQL，进行关键字查询
//			String[] fields = { "title", "content" };
//			QueryParser parser = new QueryParser(field, util.getAnalyzer());
//			Query query = parser.parse(queryString);
//			
//			Sort sort=new Sort(new SortField(sortField, SortField.Type.LONG,reverse));//生成排序类
//			
//			TopDocs topDocs = isearcher.search(query, firstResult + maxResult,sort);
//			return topDocs;
//		} catch (Exception e) {
//			logger.error("IndexDao.search error", e);
//		}
//		return null;
//	}
	
	public void addLongPoint(Document document, String name, long value) {
	    Field field = new LongPoint(name, value);
	    document.add(field);
	    //要排序，必须添加一个同名的NumericDocValuesField
	    field = new NumericDocValuesField(name, value);
	    document.add(field);
	    //要存储值，必须添加一个同名的StoredField
	    field = new StoredField(name, value);
	    document.add(field);
	}
	
}