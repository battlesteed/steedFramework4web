package steed.ext.domain.user;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;

import org.hibernate.Hibernate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import steed.domain.BaseRelationalDatabaseDomain;
import steed.domain.BaseUser;
import steed.domain.annotation.EditNotAllowed;
import steed.domain.annotation.ValidateReg;
import steed.util.base.CollectionsUtil;
import steed.util.base.RegUtil;
import steed.util.base.StringUtil;
import steed.util.digest.AESUtil;
import steed.util.digest.DigestUtil;
/**
 * 每个数据库实体类必须是steed.domain.BaseDatabaseDomain的子类
 * 注解配置请按标准的hibernate注解配置即可，还有一些其他的注解请看
 * steed.domain.annotation包下面的类的注释.
 * 用实体类作为查询条件:
 * 	1，实体类所有的数据库字段都可以用作查询条件；
 * 	2，实体类所有的后缀查询字段都可以用作查询条件；（后缀查询字段请参看
		cn.com.beyondstar.domain.people.People.roleSet_not_in_1注释）
	3，不为null的字段才是有效的查询条件
	4，更详细的请参看本类某些字段的注释，
		你可以调用DaoUtil.getSelectHql(domain, desc, asc);
		看看你的查询对象生成的是什么样的hql
 * @author 战马
 *
 */
@Entity
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class User extends BaseRelationalDatabaseDomain implements BaseUser{
	private static final long serialVersionUID = -1764586832947652845L;
	private String sex;
	/**
	 * 做查询条件时表明要查找的people实体类的name字段一定要等于该值
	 */
	private String name;
	@ValidateReg(reg={RegUtil.CommonUsedReg.chineseAndChar})
	private String nickName;
	private String phoneNumber;
	private String description;
	/**
	 * 既是数据库字段又是查询条件，
	 * 做查询条件时表明要查找的people实体类一定要包含有
	 * roleSet里面的role
	 */
	private Set<Role> roleSet;
	/**
	 * 用来做查询条件用的，没有对应的数据库字段，
	 * 表明要查找的people实体类不能关联有roleSet_not_in_1里面的role对象
	 * 
	 * 
	 * 
	 * 	roleSet_not_in_1称为“查询条件后缀字段”，
	 * 	该字段名后面的”_not_in_1“称为“查询条件后缀”，
	 * 	roleSet称为roleSet_not_in_1的约束字段
	 * 
	 * 	查询条件后缀有："_max_1","_min_1","_like_1","_not_in_1"，
	 * 	（请参看steed.util.dao.DaoUtil.indexSuffix）
	 * 	
	 * 	查询条件后缀用法：
	 * 	"_max_1"表明对应的"约束字段"要<="查询条件后缀字段"，
	 * 	"_min_1"类似上面，
	 * 	"_like_1"表明对应的"约束字段"要"like"(数据库里的like)"查询条件后缀字段"，
	 * 	"_not_in_1"表明要查找的实体类不能关联有"查询条件后缀字段"里面的对象，
	 * 	
	 */
	private Set<Role> roleSet_not_in_1;
	
	@ValidateReg(reg={RegUtil.CommonUsedReg.notStrictEmail})
	private String e_mail;
	private String password;
	/**
	 * 0平台用户，1商家，2
	 */
	private Integer userType;
	/**
	 * 状态，0，邮箱未验证，1邮箱已验证
	 */
	@EditNotAllowed
	private Integer status;
	/**
	 * 积分
	 */
	@EditNotAllowed
	private Long integration;
//	private FullAddress fullAddress;
	
//	@OneToOne
//	public FullAddress getFullAddress() {
//		return fullAddress;
//	}
//	public void setFullAddress(FullAddress fullAddress) {
//		this.fullAddress = fullAddress;
//	}
	public User() {
		
	}
	public User(String nickName) {
		this.nickName = nickName;
	}

	public Integer getUserType() {
		return userType;
	}
	public void setUserType(Integer userType) {
		this.userType = userType;
	}
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getE_mail() {
		return e_mail;
	}
	
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	@Transient
	public Set<Role> getRoleSet_not_in_1() {
		return roleSet_not_in_1;
	}
	public void setRoleSet_not_in_1(Set<Role> roleSet_not_in_1) {
		this.roleSet_not_in_1 = roleSet_not_in_1;
	}
	public void setE_mail(String e_mail) {
		this.e_mail = e_mail;
	}
	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name="User_Role",joinColumns={@JoinColumn(name="user_nickName")},
			inverseJoinColumns={@JoinColumn(name="role_name")})
	public Set<Role> getRoleSet() {
		return roleSet;
	}
	public void setRoleSet(Set<Role> roleSet) {
		this.roleSet = roleSet;
	}

	public Long getIntegration() {
		return integration;
	}
	public void setIntegration(Long integration) {
		this.integration = integration;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
/*	
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name="People_Power",joinColumns={@JoinColumn(name="people_nickName")},
			inverseJoinColumns={@JoinColumn(name="power_name")})
	public Set<Power> getPowerSet() {
		return powerSet;
	}
	public void setPowerSet(Set<Power> powerSet) {
		this.powerSet = powerSet;
	}*/
	@Id
	@GenericGenerator(name="gen1",strategy="assigned")
	@GeneratedValue(generator="gen1")
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public boolean hasPower(Power power){
		return hasPower(power.getName());
	}
	@Override
	public void initialize() {
		Hibernate.initialize(this);
	}
	
	public boolean hasPower(String powerName){
		boolean hasPower = false;
		
		if (CollectionsUtil.isSetEmpty(roleSet)) {
			return false;
		}
		for (Role r:roleSet) {
			if (r.hasPower(powerName)) {
				hasPower = true;
				break;
			}
		}
		return hasPower;
	}
	
	public void aesDecodePassword(){
		if ("d41d8cd98f00b204e9800998ecf8427e".equals(password)) {
			password = null;
		}
		if (!StringUtil.isStringEmpty(password)) {
			password = AESUtil.aesEncode(password);
		}
	}
	public String Md532minAndAESPassword(){
		if (!StringUtil.isStringEmpty(password)) {
			password = DigestUtil.Md532minAndAES(password);
		}
		return getPassword();
	}
	
	@Override
	public void initializeAll() {
		initialize();
		domainInitializeSetAll(getRoleSet());
//		domainInitializeAll(getFullAddress());
	}
	
}
