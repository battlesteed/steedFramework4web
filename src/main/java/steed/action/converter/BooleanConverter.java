package steed.action.converter;

import java.util.Map;

import org.apache.struts2.util.StrutsTypeConverter;

import steed.util.base.ArrayUtil;
import steed.util.base.BaseUtil;
import steed.util.base.StringUtil;

/**
 * @author 战马
 */
@SuppressWarnings("rawtypes")
public class BooleanConverter extends StrutsTypeConverter {
	
	@Override
	public Object convertFromString(Map context, String[] values, Class toClass) {
		if (toClass == Boolean.class) {
			if (!ArrayUtil.isArrayEmpty(values)&&!StringUtil.isStringEmpty(values[0])) {
				return new Boolean(values[0]);
			}
		}
		return null;
	}

	@Override
	public String convertToString(Map context, Object obj) {
		if (!BaseUtil.isObjEmpty(obj)) {
			return obj.toString();
		}
		return null;
	}

}
