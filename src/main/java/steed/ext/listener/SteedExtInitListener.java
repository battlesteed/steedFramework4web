package steed.ext.listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import steed.exception.PathIsTopException;
import steed.ext.domain.system.Path_Power;
import steed.ext.domain.user.Power;
import steed.listener.SystemInitListener;
import steed.util.base.CollectionsUtil;
import steed.util.base.PathUtil;
import steed.util.base.PropertyUtil;
import steed.util.dao.DaoUtil;
/**
 * 
 * @author 战马
 * @email java@beyondstar.com.cn 
 *        battle_steed@163.com
 * @company 深圳市星超越科技有限公司
 */
public class SteedExtInitListener extends SystemInitListener{
	@Override
	public void contextInitialized(ServletContextEvent event) {
		super.contextInitialized(event);
		DaoUtil.setAutoManagTransaction(false);
		try{
			ServletContext servletContext = event.getServletContext();
			List<Path_Power> path_PowerList = getPath_PowerList(servletContext);
			servletContext.setAttribute("Path_PowerList", path_PowerList);
			
			if (PropertyUtil.getBoolean("devMode")) {
				saveNnnotationPower2Database(path_PowerList);
			}
		}catch(Error e){
			e.printStackTrace();
		}finally {
			DaoUtil.managTransaction();
		}
		
	}
	
	/**
	 * 把action注解中的权限保存到数据库，不用另外在数据库手动加权限记录
	 * @param path_PowerList
	 */
	public void saveNnnotationPower2Database(List<Path_Power> path_PowerList) {
		if (CollectionsUtil.isListEmpty(path_PowerList)) {
			return;
		}
		for (Path_Power p:path_PowerList) {
			if (DaoUtil.get(Power.class, p.getPower()) == null) {
				new Power(p.getPower()).save();
			}
			saveNnnotationPower2Database(p.getSonList());
		}
	}
	
	public List<Path_Power> getPath_PowerList(ServletContext servletContext){
		List<Path_Power> list = new ArrayList<Path_Power>();
		
		Map<String, Path_Power> map = evalPath_Power2Domain(servletContext);
		for (Entry<String, Path_Power> e:map.entrySet()) {
			
			Path_Power paraent = getParaent(e.getKey(), map);
			//只添加顶级路径，不是顶级路径则放到parent中
			if (paraent != null) {
				paraent.addSon(e.getValue());
			}else {
				list.add(e.getValue());
			}
		}
		return list;
	}

	public Map<String, Path_Power> evalPath_Power2Domain(ServletContext servletContext) {
		Map<String, Path_Power> map = new HashMap<String, Path_Power>();
		Map<String, String> scanActionPower = (Map<String, String>)servletContext.getAttribute("path_powerMap");
		for (Entry<String, String> e:scanActionPower.entrySet()) {
			if (!PropertyUtil.getBoolean("devMode")&&DaoUtil.get(Power.class, e.getValue()) == null) {
				continue;
			}
			Path_Power path_Power = new Path_Power();
			String path = e.getKey();
			path_Power.setPath(path);
			path_Power.setPower(e.getValue());
			
			map.put(path, path_Power);
		}
		return map;
	}
	
	private Path_Power getParaent(String path,Map<String, Path_Power> map){
		try {
			String paraentPath = PathUtil.getParaentPath(path);
			
			if (!path.endsWith("/")) {
				paraentPath += "/";
			}
			Path_Power power = map.get(paraentPath);
			if (power == null) {
				return getParaent(paraentPath, map);
			}else {
				return power;
			}
			
		} catch (PathIsTopException e1) {
			return null;
		}

	}
	
}
