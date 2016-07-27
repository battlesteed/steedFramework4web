package steed.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import steed.util.base.BaseUtil;

public class TestReg {
	public static void main(String[] args) {  
		  
        StringBuilder text = new StringBuilder("<img src=\"/SMS/img/baidu_jgylogo3.gif?v=13442422.gif\">"+"cxz</img>");  
        Pattern p = Pattern.compile("\\=(\\'|\\\")(/SMS)\\S*(\\'|\\\")");  
        Matcher m = p.matcher(text);  
  
        int matchPoint = 0;  
        while (m.find(matchPoint)) {  
            matchPoint = m.end();  
//            BaseUtil.out(m.start(1),"m.start(1)");
//            BaseUtil.out(m.start(),"m.start(0)");
//            BaseUtil.out(m.start(2),"m.start(2)");
//            BaseUtil.out(m.start(),"m.start(0)");
//            BaseUtil.out(m.end(1),"m.end(1)");
//            BaseUtil.out(m.end(),"m.end()");
            int i = m.groupCount();
            for (int j = 0; j < i; j++) {
				BaseUtil.out(m.group(j),"j");
			}
            text.replace(m.start(2), m.end(2), "localhost"+m.group(2));
//            text.replace(m.start(), m.end(), String.format("%.2f美元", Double.valueOf(m.group(1)) / 6.3698));  
        }  
          
        System.out.println(text);  
    }  
}
