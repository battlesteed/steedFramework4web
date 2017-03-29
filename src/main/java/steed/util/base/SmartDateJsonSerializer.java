package steed.util.base;

import java.lang.reflect.Type;
import java.util.Date;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * 聪明的日期json序列器,有些date只精确到了日,并没有小时和分,转成json的时候就会变成类似2017-03-12 00:00
 * 很不好,但是如果你设置了日期格式为'yyyy-MM-dd',那些精确到分或秒的date就会损失精度
 * 
 * 本序列器非常聪明,如果日期只精确到了日,则转化成SmartDateJsonSerializer.datePattern(默认"yyyy-MM-dd")
 * 精确到分的date同理
 * @author 战马
 *
 */
public class SmartDateJsonSerializer implements JsonSerializer<Date> {
	private String datePattern = "yyyy-MM-dd";
	private String timePattern = "yyyy-MM-dd HH:mm";
	
	public SmartDateJsonSerializer() {
		super();
	}

	public SmartDateJsonSerializer(String datePattern, String timePattern) {
		super();
		this.datePattern = datePattern;
		this.timePattern = timePattern;
	}

	@Override
	public JsonElement serialize(Date date, Type arg1, JsonSerializationContext arg2) {
		if (DateUtil.getToday(date).compareTo(date) == 0) {
			return new JsonPrimitive(DateUtil.getStringFormatDate(date, datePattern));
		}else {
			return new JsonPrimitive(DateUtil.getStringFormatDate(date, timePattern));
		}
	}
	
}
