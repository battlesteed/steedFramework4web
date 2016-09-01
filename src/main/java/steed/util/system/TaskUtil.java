package steed.util.system;

import java.io.File;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import steed.util.base.PathUtil;
import steed.util.reflect.ReflectUtil;

public class TaskUtil {
	private final static ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(50);
	private void startTask(Element element){
		TaskEngine engine = (TaskEngine) ReflectUtil.newInstance(element.attributeValue("class"));
		
		List<Attribute> attributes = element.attributes();
		for(Attribute a:attributes){
			String name = a.getName();
			if (!"class".equals(name)) {
				ReflectUtil.setValue(name, engine, a.getValue());
			}
		}
		engine.start();
	}
	
	public static ScheduledExecutorService getScheduledexecutorservice() {
		return scheduledExecutorService;
	}

	@SuppressWarnings("unchecked")
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
