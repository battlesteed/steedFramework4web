package steed.ext.domain.user;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import org.hibernate.Hibernate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import steed.domain.BaseDatabaseDomain;
import steed.domain.annotation.ValidateReg;
import steed.util.base.CollectionsUtil;
import steed.util.base.RegUtil;
@Entity
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class Role extends BaseDatabaseDomain{
	private static final long serialVersionUID = 1L;
	@ValidateReg(reg={RegUtil.CommonUsedReg.chineseAndChar})
	private String name;
	private Set<Power> powerSet;
	private String description;
	/**
	 * 是否属于系统角色(不可删除)
	 */
	private Boolean isSystemRole;
	
	public Boolean getIsSystemRole() {
		if (isSystemRole == null) {
			return false;
		}
		return isSystemRole;
	}
	public void setIsSystemRole(Boolean isSystemRole) {
		this.isSystemRole = isSystemRole;
	}
	public Role(String name) {
		this.name = name;
	}
	public Role() {
	}
	@Id
	@GenericGenerator(name="generator",strategy="assigned")
	@GeneratedValue(generator="generator")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name="Role_Power",joinColumns={@JoinColumn(name="role_name")},
			inverseJoinColumns={@JoinColumn(name="power_name")})
	@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
	public Set<Power> getPowerSet() {
		return powerSet;
	}
	public void setPowerSet(Set<Power> powerSet) {
		this.powerSet = powerSet;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public boolean hasPower(Power power) {
		return hasPower(power.getName());
	}
	public boolean hasPower(String powerName) {
		//TODO 超级管理员拥有所有权限
//		if(GlobalParam.SYSTEM.SUPER_ADMIN_NAME.equals(this.name)){
//			return true;
//		}
		if (CollectionsUtil.isSetEmpty(powerSet)) {
			return false;
		}
		
		boolean hasPower = false;
		for (Power p : powerSet) {
			if (powerName.equals(p.getName())) {
				hasPower = true;
				break;
			}
		}
		return hasPower;
	}
	@Override
	public void initialize() {
		Hibernate.initialize(this);
	}
	@Override
	public void initializeAll() {
		initialize();
		for (Power p:getPowerSet()) {
			p.initialize();
		}
	}
}
