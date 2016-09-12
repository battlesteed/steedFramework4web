package steed.ext.domain.system;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Id;

import steed.domain.BaseRelationalDatabaseDomain;

public class Path_Power extends BaseRelationalDatabaseDomain{
	private static final long serialVersionUID = -7618602732432015406L;
	private String path;
	private String power;

	private List<Path_Power> sonList = new ArrayList<Path_Power>();
	
//	private Path_Power father;
	
	/*public Path_Power getFather() {
		return father;
	}
	public void setFather(Path_Power father) {
		this.father = father;
	}*/
	@Id
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getPower() {
		return power;
	}
	public void setPower(String power) {
		this.power = power;
	}
	public List<Path_Power> getSonList() {
		return sonList;
	}
	public void setSonList(List<Path_Power> sonList) {
		this.sonList = sonList;
	}
	
	public void addSon(Path_Power son){
		/**
		 * 防止add方法需要“添加”权限，save方法也需要“添加”权限时
		 * 权限树出现两个同级的添加权限
		 */
		for (Path_Power p:sonList) {
			if (p.getPower().equals(son.getPower())) {
				return;
			}
		}
		sonList.add(son);
	}
	
	
}
