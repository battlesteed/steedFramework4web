package steed.listener;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;

import steed.action.annotation.Power;
import steed.domain.GlobalParam;
import steed.util.UtilsUtil;
import steed.util.base.PathUtil;
import steed.util.base.PropertyUtil;
import steed.util.file.FileUtil;
import steed.util.system.TaskUtil;
/**
 * 启动时初始化系统
 * @author 战马
 *
 */
public class SystemInitListener implements ServletContextListener {
	@Override
	public void contextDestroyed(ServletContextEvent event) {
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		ServletContext servletContext = event.getServletContext();
		initGlobalParam(servletContext);
		UtilsUtil.initUtils();
		servletContext.setAttribute("path_powerMap", scanActionPowerAndDomain());
		
		//启动定时任务
		new TaskUtil().init();
	}

	
	private void initGlobalParam(ServletContext servletContext) {
		/**
		 * 获取项目路径
		 */
		GlobalParam.FOLDER.rootPath = servletContext.getRealPath("/");
		GlobalParam.FOLDER.contextPath = servletContext.getContextPath();
		GlobalParam.FOLDER.serverAddress = PropertyUtil.getConfig("site.rootURL");
		GlobalParam.FOLDER.contextUrl = GlobalParam.FOLDER.serverAddress + GlobalParam.FOLDER.contextPath;
		GlobalParam.config.devMode = PropertyUtil.getBoolean("devMode");
	}
	
	/**
	 * 扫描action中的权限注解
	 * @return Map<路径, 权限名>
	 */
	private Map<String, String> scanActionPowerAndDomain(){
		String classesPath = PathUtil.getClassesPath();
		int len = classesPath.length() - 1;
		
		List<File> allFile = new FileUtil().getAllFile(classesPath,null);
		
		Map<String, String> path_powerMap = new HashMap<String, String>();
/*		
 * 
 * 经测试性能不如上面的方法，不用
 * List<File> allFile = new FileUtil().getAllFile(classesPath,new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				File f = new File(String.format("%s%s%s", dir.getAbsolutePath(),File.separator,name));
				if (f.isDirectory()) {
					return true;
				}else{
					return name.endsWith("Action.class");
				}
			}
		});
*/		
		for (File f:allFile) {
			String absolutePath = f.getAbsolutePath();
			if (absolutePath.endsWith("Action.class")) {
				String replaceAll = absolutePath.substring(len).replaceAll("\\\\", "/").replaceAll("\\/", ".");
				try {
					Class actionClass = Class.forName(replaceAll.substring(0,replaceAll.length() - 6));
					Power power = (Power) actionClass.getAnnotation(Power.class);
					Namespace nameSpace = (Namespace)actionClass.getAnnotation(Namespace.class);
					if (nameSpace == null) {
						continue;
					}
					String nameSpacePath = nameSpace.value() + "/";
					if (power != null) {
						path_powerMap.put(nameSpacePath, power.value());
					}
					
					for(Method m:actionClass.getMethods()){
						Power methodPower = m.getAnnotation(Power.class);
						if (methodPower != null) {
							String path;
							Action action = m.getAnnotation(Action.class);
							if (action != null) {
								path = action.value();
							}else{
								path = m.getName();
							}
							path_powerMap.put(String.format("%s%s%s", nameSpacePath,path,".act"), methodPower.value());
						}
					}
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}/*else if(absolutePath.contains("domain") && PropertyUtil.getBoolean("isSignalDatabase")){
				String replaceAll = absolutePath.substring(len).replaceAll("\\\\", "/").replaceAll("\\/", ".");
				try {
					String domainClassName = replaceAll.substring(0,replaceAll.length() - 6);
					Class domainClass = Class.forName(domainClassName);
					Entity entity = (Entity) domainClass.getAnnotation(Entity.class);
					if (entity != null) {
						domainList.add(domainClassName);
					}
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}*/
		}
		
		return path_powerMap;
	}
	
	
	
}
