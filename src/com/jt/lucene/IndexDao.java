package com.jt.lucene;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.NumericDocValuesField;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.index.DirectoryReader;
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
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.Scorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.springframework.stereotype.Repository;

import com.jt.gateway.util.FileUtil;

/**
 * @项目名称：lucene
 * @类名称：IndexDao
 * @类描述：
 * @创建人：YangChao
 * @创建时间：2016年8月31日 上午10:12:05
 * @version 1.0.0
 */
@Repository
public class IndexDao {
	private static Logger logger = Logger.getLogger(IndexDao.class);
	private LuceneUtilsGw util;
	private String indexPath;
	
	public String getIndexPath() {
		return indexPath;
	}

	public void setIndexPath(String indexPath) {
		this.indexPath = indexPath;
	}

	public IndexDao(String indexPath) throws IOException{
		this.indexPath=indexPath;
		util=new LuceneUtilsGw(indexPath);
	}
	
	public IndexDao(){
		
	}
	public void initInSpring(){
		try {
			util=new LuceneUtilsGw(indexPath);
			logger.info("初始化indexdao");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public int batchSave(List<Document> docList){
		int jobSize=0;
		IndexWriter indexWriter = null;
		try {
			IndexWriterConfig config = new IndexWriterConfig(util.getAnalyzer());
			indexWriter = new IndexWriter(util.getDirectory(), config);
			for(Document doc:docList){
				try {
					indexWriter.addDocument(doc);
					jobSize++;
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("docid=["+doc.get("xq_id")+"]存储错误");
				}
			}
		} catch (Exception e) {
			logger.error("IndexDao.save error", e);
		} finally {
			LuceneUtilsGw.closeIndexWriter(indexWriter);
		}
		return jobSize;
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
	
//	public void save(Article article) {
//		Document doc = DocumentUtils.article2Document(article);
//		IndexWriter indexWriter = null;
//		try {
//			IndexWriterConfig config = new IndexWriterConfig(util.getAnalyzer());
//			indexWriter = new IndexWriter(util.getDirectory(), config);
//			indexWriter.addDocument(doc);
//		} catch (Exception e) {
//			logger.error("IndexDao.save error", e);
//		} finally {
//			LuceneUtilsGw.closeIndexWriter(indexWriter);
//		}
//	}

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


	public boolean deleteAll(){
		boolean status=false;
		File file=new File(indexPath);
		if(file.exists()){
			status=FileUtil.deleteDir(file);
		}
		return status;
	}
	
	//不再提供update方法，直接删除全部重建
//	public void update(Article article) {
//		Document doc = DocumentUtils.article2Document(article);
//		IndexWriter indexWriter = null;
//		try {
//			Term term = new Term("id", article.getId().toString());
//			IndexWriterConfig config = new IndexWriterConfig(util.getAnalyzer());
//			indexWriter = new IndexWriter(util.getDirectory(), config);
//			indexWriter.updateDocument(term, doc);// 先删除，后创建。
//		} catch (Exception e) {
//			logger.error("IndexDao.save error", e);
//		} finally {
//			LuceneUtilsGw.closeIndexWriter(indexWriter);
//		}
//	}
/**
 * 仅用于测试
 * @param queryString
 * @param firstResult
 * @param maxResult
 * @return
 */
	public QueryResult search(String[] queryString, int firstResult, int maxResult) {
		List<Article> list = new ArrayList<Article>();
		try {
			DirectoryReader ireader = DirectoryReader.open(util.getDirectory());
			// 2、第二步，创建搜索器
			IndexSearcher isearcher = new IndexSearcher(ireader);

			// 3、第三步，类似SQL，进行关键字查询
			String[] fields = {"title" ,"title"};
			Occur[] occ={Occur.SHOULD,Occur.SHOULD};
		//	QueryParser parser = new MultiFieldQueryParser(fields, util.getAnalyzer());
			Query query =MultiFieldQueryParser.parse(queryString,fields,occ,util.getAnalyzer());

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
				System.out.println("hits["+i+"].doc=["+hits[i].doc+"]");
				Document hitDoc = isearcher.doc(hits[i].doc);
				Article article = DocumentUtils.document2Ariticle(hitDoc);
				//
//				String text = null;
//				//highlighter.getBestFragment(util.getAnalyzer(), "content", hitDoc.get("content"));
//				if (text != null) {
//					article.setContent(text);
//				}
				
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
	
	/**
	 * 测试
	 * @param queryString
	 * 
	 * @param field
	 * @param sortField
	 * @param sortFieldType
	 * @param reverse
	 * @param firstResult
	 * @param maxResult
	 * @return
	 */
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
	
	
/**
 * 检索词的与、或关系统一定义
 * @param queryString
 * 支持传入多个检索词
 * @param occur
 * 多个检索词之间的关系，Occur.MUST(结果“与”)， Occur.MUST_NOT(结果“不包含”)，Occur.SHOULD(结果“或”)
 * @param field
 * 当前应用使用title
 * @param sortField
 * 排序字段，传入null则按照相关度排序
 * @param sortFieldType
 * 排序字段的类型，如long、int等
 * @param reverse
 * 排序方式，顺序(false)还是倒序(true)
 * @param firstResult
 * 结果从第几个开始
 * @param maxResult
 * 结果到第几个结束，可以传入-1，则限制为1000条
 * @return
 */
	public List<Document> search(String[] queryString,Occur occur,String field,String sortField,SortField.Type sortFieldType,boolean reverse ,int firstResult, int maxResult) {
		List<Document> list = new ArrayList<Document>();
		TopDocs topDocs =null;
		Sort sort=null;
		DirectoryReader ireader=null;
		IndexSearcher isearcher=null;
		Occur[] occ=new Occur[queryString.length];
		String[]fields=new String[queryString.length];
		if(maxResult==-1){
			maxResult=1000;
		}
		try {
			ireader = DirectoryReader.open(util.getDirectory());
			// 2、第二步，创建搜索器
			isearcher = new IndexSearcher(ireader);
			// 3、第三步，类似SQL，进行关键字查询
//			parser = new QueryParser(field, util.getAnalyzer());
			for(int i=0;i<queryString.length;i++){
				occ[i]=occur;
				fields[i]=field;
			}
			Query query =MultiFieldQueryParser.parse(queryString,fields,occ,util.getAnalyzer());

			
			if(sortField!=null&&sortFieldType!=null){
				sort=new Sort(new SortField(sortField, sortFieldType,reverse));//生成排序类
				topDocs = isearcher.search(query, firstResult + maxResult,sort);
			}else{
				topDocs = isearcher.search(query, firstResult + maxResult);
			}
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
	
	
/**
 * 支持定义每个检索词的与、或关系
 * @param queryString
 * 支持传入多个检索词
 * @param occurs
 * 多个检索词之间的关系，Occur.MUST(结果“与”)， Occur.MUST_NOT(结果“不包含”)，Occur.SHOULD(结果“或”)
 * @param field
 * 当前应用使用title
 * @param sortField
 * 排序字段，传入null则按照相关度排序
 * @param sortFieldType
 * 排序字段的类型，如long、int等
 * @param reverse
 * 排序方式，顺序(false)还是倒序(true)
 * @param firstResult
 * 结果从第几个开始
 * @param maxResult
 * 结果到第几个结束，可以传入-1，则限制为1000条
 * @return
 */
	public List<Document> search(String[] queryString,Occur[] occurs,String field,String sortField,SortField.Type sortFieldType,boolean reverse ,int firstResult, int maxResult) {
		List<Document> list = new ArrayList<Document>();
		TopDocs topDocs =null;
		Sort sort=null;
		DirectoryReader ireader=null;
		IndexSearcher isearcher=null;
		String[]fields=new String[queryString.length];
		if(maxResult==-1){
			maxResult=1000;
		}
		try {
			ireader = DirectoryReader.open(util.getDirectory());
			// 2、第二步，创建搜索器
			isearcher = new IndexSearcher(ireader);
			// 3、第三步，类似SQL，进行关键字查询
//			parser = new QueryParser(field, util.getAnalyzer());
			for(int i=0;i<queryString.length;i++){
				fields[i]=field;
			}
			Query query =MultiFieldQueryParser.parse(queryString,fields,occurs,util.getAnalyzer());

			
			if(sortField!=null&&sortFieldType!=null){
				sort=new Sort(new SortField(sortField, sortFieldType,reverse));//生成排序类
				topDocs = isearcher.search(query, firstResult + maxResult,sort);
			}else{
				topDocs = isearcher.search(query, firstResult + maxResult);
			}
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
	
	/**
	 * 简易检索，各个字段之间是or关系，默认按照相关性排序
	 * @param queryString
	 * @param field
	 * @param firstResult
	 * @param maxResult
	 * @return
	 */
	public List<Article> searchArticle(String[] queryString,String field,int firstResult, int maxResult) {
		Occur occur=Occur.SHOULD;
		return searchArticle(queryString, occur, field, null, null,false, firstResult, maxResult);
	}
		
	
/**
 * 检索词的与、或关系统一定义
 * @param queryString
 * 支持传入多个检索词
 * @param occur
 * 多个检索词之间的关系，Occur.MUST(结果“与”)， Occur.MUST_NOT(结果“不包含”)，Occur.SHOULD(结果“或”)
 * @param field
 * 当前应用使用title
 * @param sortField
 * 排序字段，传入null则按照相关度排序
 * @param sortFieldType
 * 排序字段的类型，如long、int等
 * @param reverse
 * 排序方式，顺序(false)还是倒序(true)
 * @param firstResult
 * 结果从第几个开始
 * @param maxResult
 * 结果到第几个结束，可以传入-1，则限制为1000条
 * @return
 */
	public List<Article> searchArticle(String[] queryString,Occur occur,String field,String sortField,SortField.Type sortFieldType,boolean reverse ,int firstResult, int maxResult) {
		Occur[] occurs=new Occur[queryString.length];
		for(int i=0;i<occurs.length;i++){
			occurs[i]=occur;
		}
		return searchArticle(queryString, occurs, field, sortField, sortFieldType, reverse, firstResult, maxResult);
	}
	
	
/**
 * 支持定义每个检索词的与、或关系，只检索一个字段
 * @param queryString
 * 支持传入多个检索词
 * @param occurs
 * 多个检索词之间的关系，Occur.MUST(结果“与”)， Occur.MUST_NOT(结果“不包含”)，Occur.SHOULD(结果“或”)
 * @param field
 * 当前应用使用title
 * @param sortField
 * 排序字段，传入null则按照相关度排序
 * @param sortFieldType
 * 排序字段的类型，如long、int等
 * @param reverse
 * 排序方式，顺序(false)还是倒序(true)
 * @param firstResult
 * 结果从第几个开始
 * @param maxResult
 * 结果到第几个结束，可以传入-1，则限制为1000条
 * @return
 */
	public List<Article> searchArticle(String[] queryString,Occur[] occurs,String field,String sortField,SortField.Type sortFieldType,boolean reverse ,int firstResult, int maxResult) {
		String fields[]=new String[queryString.length];
		for(int i=0;i<fields.length;i++){
			fields[i]=field;
		}
		return searchArticle(queryString,occurs,fields, sortField, sortFieldType ,reverse , firstResult, maxResult);
	}
	
	
	/**
	 * 支持定义每个检索词的与、或关系，支持检索指定字段
	 * @param queryString
	 * 支持传入多个检索词
	 * @param occurs
	 * 多个检索词之间的关系，Occur.MUST(结果“与”)， Occur.MUST_NOT(结果“不包含”)，Occur.SHOULD(结果“或”)
	 * @param field
	 * 当前应用使用title
	 * @param sortField
	 * 排序字段，传入null则按照相关度排序
	 * @param sortFieldType
	 * 排序字段的类型，如long、int等
	 * @param reverse
	 * 排序方式，顺序(false)还是倒序(true)
	 * @param firstResult
	 * 结果从第几个开始
	 * @param maxResult
	 * 结果到第几个结束，可以传入-1，则限制为1000条
	 * @return
	 */
		public List<Article> searchArticle(String[] queryString,Occur[] occurs,String[] fields,String sortField,SortField.Type sortFieldType,boolean reverse ,int firstResult, int maxResult) {
			List<Article> list = new ArrayList<Article>();
			TopDocs topDocs =null;
			Sort sort=null;
			DirectoryReader ireader=null;
			IndexSearcher isearcher=null;
			if(maxResult==-1){
				maxResult=1000;
			}
			try {
				ireader = DirectoryReader.open(util.getDirectory());
				// 2、第二步，创建搜索器
				isearcher = new IndexSearcher(ireader);
				// 3、第三步，类似SQL，进行关键字查询
//				parser = new QueryParser(field, util.getAnalyzer());
//				for(int i=0;i<queryString.length;i++){
//					fields[i]=field;
//				}
				Query query =MultiFieldQueryParser.parse(queryString,fields,occurs,util.getAnalyzer());
				logger.info("Lucene检索条件为["+query+"]");
				
				if(sortField!=null&&sortFieldType!=null){
					sort=new Sort(new SortField(sortField, sortFieldType,reverse));//生成排序类
					topDocs = isearcher.search(query, firstResult + maxResult,sort);
				}else{
					topDocs = isearcher.search(query, firstResult + maxResult);
				}
				ScoreDoc[] hits = topDocs.scoreDocs;// 第二个参数，指定最多返回前n条结果
				// 高亮
//				Formatter formatter = new SimpleHTMLFormatter("<font color='red'>", "</font>");
//				Scorer source = new QueryScorer(query);
//				Highlighter highlighter = new Highlighter(formatter, source);
				
				// 处理结果
				int endIndex = Math.min(firstResult + maxResult, hits.length);
				
				for (int i = firstResult; i < endIndex; i++) {
					Document hitDoc = isearcher.doc(hits[i].doc);
					list.add(DocumentUtils.document2Ariticle(hitDoc));
				}
				ireader.close();
				return list;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		
	
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