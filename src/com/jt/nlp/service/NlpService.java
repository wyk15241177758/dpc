/**
 * 语义分析相关服务
 */
package com.jt.nlp.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apdplat.qa.parser.WordParser;
import org.apdplat.qa.questiontypeanalysis.patternbased.MainPartExtracter;
import org.apdplat.qa.questiontypeanalysis.patternbased.QuestionStructure;
import org.apdplat.word.WordSegmenter;
import org.apdplat.word.recognition.StopWord;
import org.apdplat.word.segmentation.Word;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.jt.nlp.util.NlpUtil;


public class NlpService {
	private static final Logger LOG = LoggerFactory.getLogger(NlpService.class);

	private MainPartExtracter mainPartExtracter;
	private Set<String> remainPartOfSpeech;
	//不分词词典
	private List<String> noParticipleWordList;
	public NlpService() {
		mainPartExtracter = new MainPartExtracter();
		// 默认丢弃词组和地名类型
		remainPartOfSpeech = new HashSet<String>();
		remainPartOfSpeech.add("NR");
		remainPartOfSpeech.add("NN");
	}

	/**
	 * 获得主谓宾，不再标注词性。
	 * 增加stopword的过滤
	 * 
	 * @param question
	 * @return
	 */
	public List<Word> getMainPartWords(String question) {
		//先排除停用词
		List<Word> list=WordSegmenter.seg(question);
		question="";
		for(Word word:list){
			question+=word.getText();
		}
		LOG.info("排除停用词的检索词=["+question+"]");
		QuestionStructure qs = mainPartExtracter.getMainPart(question);
		String mainPart = qs.getMainPart();
		if(mainPart==null){
			LOG.info("未能识别主谓宾 返回检索词=[" + list + "]");
			return list;
		}else{
			list=WordSegmenter.seg(mainPart);
		}
		
		LOG.info("主谓宾=[" + list + "]");
		return list;
	}

	/**
	 * 主方法，获得过滤之后的主谓宾
	 * 
	 * @param question
	 * @return
	 */
	public Set<String> getSearchWords(String question) {
		Set<String> set = new HashSet<String>();
		// 将问题根据标点符合切分为几个问题
		List<String> splitedQ = NlpUtil.splitQuestion(question);
		for (String str : splitedQ) {
			List<Word> mainPartWords = getMainPartWords(str);
			if(mainPartWords==null){
				set.add(str);
			}else{
				set.addAll(doFilterWord(mainPartWords));
			}
			if(noParticipleWordList==null){
				WebApplicationContext webContext = ContextLoader.getCurrentWebApplicationContext();
				if(webContext!=null){
					QAService qaService = (QAService) webContext.getBean("qaService");
					this.noParticipleWordList=qaService.getNoParticipleWordList();
				}
			}
			//如果属于不分词词典，直接加入set
			if(noParticipleWordList!=null){
					for(String noParticipleWord:noParticipleWordList){
						if(str.indexOf(noParticipleWord)!=-1){
							//问题中有不分词词汇，hashset自动 保证不重复
							set.add(noParticipleWord);
						}
					}
				}
			}
		return set;
	}

	/**
	 * 必须符合此规则
	 * 
	 * @param word
	 * @return
	 */
	private Word baseFilterWord(Word word) {
		if(word==null)return null;
		// 词
		String text = word.getText();
		// 过滤只有一个字的词，返回null
		if (text.length() < 2) {
			return null;
		} else {
			return word;
		}
	}

	// 处理过滤检索词
	private Set<String> doFilterWord(List<Word> mainPartWords) {
		

		Set<String> set = new HashSet<String>();
		Map<String,Word> map = new HashMap<String,Word>();

		for (int i = 0; i < mainPartWords.size(); i++) {
			switch (i) {
			case 0:
				map.put("S",mainPartWords.get(i));
				break;
			case 1:
				map.put("P",mainPartWords.get(i));
				break;
			case 2:
				map.put("O",mainPartWords.get(i));
				break;
			default:
				map.put("other",mainPartWords.get(i));
				break;
			}
		}
		
		for (Entry<String,Word> entry : map.entrySet()) {
			// 必须符合baseFilterWord
			//&& remainPartOfSpeech.contains(entry.getValue().tag().value())
			//不再做词性过滤，主谓宾都作为搜索词提交
			if (baseFilterWord(entry.getValue()) != null) {
				set.add(entry.getValue().getText());
			}
		}

//		// 不属于remainPartOfSpeech，则将符合baseFilterWord的宾语放入set，如果宾语不符合baseFilterWord，则将
//		// 符合baseFilterWord的主语放入set，总之谓语不太可能是搜索词。如果都不符合则返回长度为0的set
//		if (set.size() == 0) {
//			// 必须符合baseFilterWord
//			if (baseFilterWord(map.get("O")) != null) {
//				set.add(map.get("O").word());
//			}else if (baseFilterWord(map.get("S")) != null){
//				set.add(map.get("S").word());
//			}
//		}
		return set;
	}
	/**
	 * 对传入的句子分词
	 * @return
	 */
	public List<String> getParticle(String question){
		List<Word> list=WordParser.parse(question);
		List<String> particleList=new ArrayList<String>();
		for(Word word:list){
			particleList.add(word.getText());
		}
		return particleList;
	}
	public static void main(String[] args) {
		Word word=new Word("请问");
		List<Word> list=new ArrayList<Word>();
		list.add(word);
		word=new Word("社保");
		list.add(word);
		word=new Word("怎么");
		list.add(word);
		word=new Word("办理");
		list.add(word);
		StopWord.filterStopWords(list);
		System.out.println(list);;
//		NlpService service = new NlpService();
//		String[] arr = { "杨浦有什么地方好玩？", "孩子转学需要什么手续？", "我想自主创业，政府有什么政策？", "噪音扰民怎么办?", "上海怎么办理居住证 " };
//		String[] arr = {"中国" };
//		for (String str : arr) {
//			System.out.println("问题:" + str + "----------begin");
//			Set<String> set = service.getSearchWords(str);
//			for (String s : set) {
//				System.out.println(s);
//			}
//			System.out.println("问题:" + str + "----------end");
//		}
	}
}
