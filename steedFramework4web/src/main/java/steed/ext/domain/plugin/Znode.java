package steed.ext.domain.plugin;

import steed.util.wechat.domain.send.Button;

/**
 * ztree节点实体类
 * @author 战马
 *
 */
public class Znode extends Button{
	private Integer id;

	private Integer pId;

//	private String name;

	private Boolean childOuter;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getpId() {
		return pId;
	}

	public void setpId(Integer pId) {
		this.pId = pId;
	}

	/*public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}*/

	public Boolean getChildOuter() {
		return childOuter;
	}

	@Override
	public void setName(String name) {
		super.setName(name);
	}

	@Override
	public String getName() {
		return super.getName();
	}

	public void setChildOuter(Boolean childOuter) {
		this.childOuter = childOuter;
	}


}
