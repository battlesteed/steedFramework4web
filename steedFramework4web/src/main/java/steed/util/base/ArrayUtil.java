package steed.util.base;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
/**
 * 数组,Collection,map工具类
 * @author 战马
 *
 */
public class ArrayUtil {
	public static boolean isArrayEmpty(Object[] array){
		return array == null || array.length < 0;
	}
	
}
