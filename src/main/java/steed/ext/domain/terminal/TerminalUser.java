package steed.ext.domain.terminal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import steed.domain.BaseRelationalDatabaseDomain;
import steed.domain.annotation.ValidateReg;
import steed.util.base.RegUtil;

/**
 * 终端用户
 * @author 战马
 *
 */
@Entity
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class TerminalUser extends BaseRelationalDatabaseDomain{
	@ValidateReg(reg={RegUtil.CommonUsedReg.chineseAndChar})
	private String userid;
	/**
	 * 由于不能明文存但又不能进行不可逆加密，所以该字段会经过AES加密
	 */
	private String password;
	@Id
	@GenericGenerator(name="gen1",strategy="assigned")
	@GeneratedValue(generator="gen1")
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Override
	public void initializeAll() {
		initialize();
	}
	
}
