package steed.alipay.domain;

/**
 * 付款详情
 * 流水号1^收款方帐号1^真实姓名^付款金额1^备注说明1
 * @author 战马
 */
public class PayDetail{
	/**
	 * 流水号
	 */
	private String flowNumber;
	/**
	 * 收款方帐号
	 */
	private String receiveAccount;
	/**
	 * 真实姓名
	 */
	private String realName;
	/**
	 * 付款金额
	 */
	private double amount;
	/**
	 * 备注说明
	 */
	private String remark;
	public String getFlowNumber() {
		return flowNumber;
	}
	public void setFlowNumber(String flowNumber) {
		this.flowNumber = flowNumber;
	}
	public String getReceiveAccount() {
		return receiveAccount;
	}
	public void setReceiveAccount(String receiveAccount) {
		this.receiveAccount = receiveAccount;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
