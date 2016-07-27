package steed.util.other;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class Pinyin4J2 {
	public String getFirstPinyin(char cha){
		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();  
		// UPPERCASE：大写  (ZHONG)  
		// LOWERCASE：小写  (zhong)  
		format.setCaseType(HanyuPinyinCaseType.LOWERCASE);  
		// WITHOUT_TONE：无音标  (zhong)  
		// WITH_TONE_NUMBER：1-4数字表示英标  (zhong4)  
		// WITH_TONE_MARK：直接用音标符（必须WITH_U_UNICODE否则异常）  (zhòng)  
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);  
		// WITH_V：用v表示ü  (nv)  
		// WITH_U_AND_COLON：用"u:"表示ü  (nu:)  
		// WITH_U_UNICODE：直接用ü (nü)  
		format.setVCharType(HanyuPinyinVCharType.WITH_V);  
		try {
			String[] pinyin = PinyinHelper.toHanyuPinyinStringArray(cha, format);
			return pinyin[0];
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			e.printStackTrace();
			return null;
		}
	}
	public String getPinyin(String str){
		StringBuffer sb = new StringBuffer();
		String temp;
		char[] chars = str.toCharArray();
		for (int i = 0;i <chars.length;i++) {
			if ((temp = getFirstPinyin(chars[i]))!= null) {
				sb.append(temp);
			}
		}
		return sb.toString();
	}
}