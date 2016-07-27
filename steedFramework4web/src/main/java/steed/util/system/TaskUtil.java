package steed.util.system;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import steed.exception.runtime.system.SystemInitException;
import steed.util.base.PathUtil;
import steed.util.reflect.ReflectUtil;

public class TaskUtil {
	private static ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(50);
	private static final Map<String, Object[]> taskLoop = new HashMap<String, Object[]>();
	private void startTask(Element element){
//		scheduledExecutorService.schedule((Runnable) ReflectUtil.newInstance(element.attributeValue("class")), Long.parseLong(element.attributeValue("delay")), getTimeUnit(element.attributeValue("timeUnit")));
		Runnable runnable = (Runnable) ReflectUtil.newInstance(element.attributeValue("class"));
		long delay = Long.parseLong(element.attributeValue("delay"));
		TimeUnit timeUnit = getTimeUnit(element.attributeValue("timeUnit"));
		taskLoop.put(element.attributeValue("class"), new Object[]{runnable,delay,timeUnit});
		startTask(runnable, delay, timeUnit);
	}
	
	public static void startTask(Runnable runnable,long delay,TimeUnit unit){
		scheduledExecutorService.schedule(runnable, delay, unit);
	}
	public static void startTask(Class<?> clazz){
		Object[] objects = taskLoop.get(clazz.getName());
		startTask((Runnable) objects[0],(Long)objects[1],(TimeUnit)objects[2]);
	}
	public static void startTaskWidthNewThread(final Class<?> clazz){
			new Thread(new Runnable() {
				@Override
				public void run() {
					startTask(clazz);
				}
			}).start();
	}
	
	private TimeUnit getTimeUnit(String timeUint){
		// 蛋疼╭∩╮（︶︿︶）╭∩╮鄙视你！，为兼容jdk1.6又不能用switch
		String upperCase = timeUint.toUpperCase();
		if("DAYS".equals(upperCase)){
			return TimeUnit.DAYS;
		}else if("HOURS".equals(upperCase)){
			return TimeUnit.HOURS;
		}else if("MICROSECONDS".equals(upperCase)){
			return TimeUnit.MICROSECONDS;
		}else if("MILLISECONDS".equals(upperCase)){
			return TimeUnit.MILLISECONDS;
		}else if("MINUTES".equals(upperCase)){
			return TimeUnit.MINUTES;
		}else if("NANOSECONDS".equals(upperCase)){
			return TimeUnit.NANOSECONDS;
		}else if("SECONDS".equals(upperCase)){
			return TimeUnit.SECONDS;
		}else {
			throw new SystemInitException("在java.util.concurrent.TimeUnit中没有"+timeUint+"类型的枚举变量");
		}
	}
	public void init(){
		try {
			Document doc = new SAXReader().read(new File(PathUtil.getClassesPath()+"properties/task.xml"));
			Element root = doc.getRootElement();
			List<Element> elements = root.elements("task");
			for (Element temp:elements) {
				startTask(temp);
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
}
