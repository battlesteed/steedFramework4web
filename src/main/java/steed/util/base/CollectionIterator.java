package steed.util.base;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;


/**
 * 类似collection的iterator,统一Collections的操作
 * @author 战马
 *
 */
public class CollectionIterator{
	/**
	 * 要获取iterator的集合
	 */
	public Object collection;
	private int count;
	private int current=-1;
	private Iterator<?> i;
	private Iterator<?> key;
	private int type;
	public CollectionIterator(Object collection) {
		this.collection = collection;
		count = CollectionsUtil.getCollectionLength(collection);
		type = CollectionsUtil.getCollectionType(collection);
		if (type == CollectionsUtil.collection) {
			i = ((Collection<?>)collection).iterator();
		}else if(type == CollectionsUtil.map){
			key = ((Map<?,?>)collection).keySet().iterator();
		}
	}

	public boolean hasItem(){
		if (i != null) {
			return i.hasNext();
		}else if (key != null) {
			return key.hasNext();
		}else {
			return current <= count;
		}
	}
	
	public Object nextItem(){
		if (i != null) {
			return i.next();
		}else if (type == CollectionsUtil.array) {
			current++;
			return Array.get(collection, current);
		}else {
			return ((Map<?,?>)collection).get(key.next());
		}
	}
}
