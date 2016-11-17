/**
 * 语义分析相关服务
 */
package com.jt.nlp.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import org.apdplat.qa.questiontypeanalysis.patternbased.MainPartExtracter;
import org.apdplat.qa.questiontypeanalysis.patternbased.QuestionStructure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jt.nlp.util.NlpUtil;

import edu.stanford.nlp.ling.LabeledWord;

public class NlpService {
	private static final Logger LOG = LoggerFactory.getLogger(NlpService.class);

	private MainPartExtracter mainPartExtracter;
	private Set<String> remainPartOfSpeech;

	public NlpService() {
		mainPartExtracter = new MainPartExtracter();
		// 默认丢弃词组和地名类型
		remainPartOfSpeech = new HashSet<String>();
		remainPartOfSpeech.add("NR");
		remainPartOfSpeech.add("NN");
	}

	/**
	 * 获得标注词性的主谓宾
	 * 
	 * @param question
	 * @return
	 */
	public List<LabeledWord> getMainPartWords(String question) {
		QuestionStructure qs = mainPartExtracter.getMainPart(question);
		String mainPart = qs.getMainPart();
		if(mainPart==null){
			return null;
		}
		List<LabeledWord> list = mainPartExtracter.getPortOfSpeech(mainPart);
		LOG.info("question=[" + question + "] 标注词性的主谓宾=[" + list + "]");
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
			List<LabeledWord> mainPartWords = getMainPartWords(str);
			if(mainPartWords==null){
				set.add(str);
			}else{
				set.addAll(doFilterWord(mainPartWords));
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
	private LabeledWord baseFilterWord(LabeledWord word) {
		if(word==null)return null;
		// 词
		String text = word.value();
		// 过滤只有一个字的词，返回null
		if (text.length() < 2) {
			return null;
		} else {
			return word;
		}
	}

	// 处理过滤检索词
	private Set<String> doFilterWord(List<LabeledWord> mainPartWords) {
		Set<String> set = new HashSet<String>();
		Map<String,LabeledWord> map = new HashMap<String,LabeledWord>();

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
			}
		}
		
		for (Entry<String,LabeledWord> entry : map.entrySet()) {
			// 必须符合baseFilterWord
			if (baseFilterWord(entry.getValue()) != null
					&& remainPartOfSpeech.contains(entry.getValue().tag().value())) {
				set.add(entry.getValue().word());
			}
		}

		// 不属于remainPartOfSpeech，则将符合baseFilterWord的宾语放入set，如果宾语不符合baseFilterWord，则将
		// 符合baseFilterWord的主语放入set，总之谓语不太可能是搜索词。如果都不符合则返回长度为0的set
		if (set.size() == 0) {
			// 必须符合baseFilterWord
			if (baseFilterWord(map.get("O")) != null) {
				set.add(map.get("O").word());
			}else if (baseFilterWord(map.get("S")) != null){
				set.add(map.get("S").word());
			}
		}
		return set;
	}

	public static void main(String[] args) {
		NlpService service = new NlpService();
//		String[] arr = { "杨浦有什么地方好玩？", "孩子转学需要什么手续？", "我想自主创业，政府有什么政策？", "噪音扰民怎么办?", "上海怎么办理居住证 " };
		String[] arr = {"中国" };
		for (String str : arr) {
			System.out.println("问题:" + str + "----------begin");
			Set<String> set = service.getSearchWords(str);
			for (String s : set) {
				System.out.println(s);
			}
			System.out.println("问题:" + str + "----------end");
		}
	}
}
