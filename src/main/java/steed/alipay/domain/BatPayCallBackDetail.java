package steed.alipay.domain;

/**
 * 
 * 批量支付回调详细
 * 流水号^收款方账号^收款账号姓名^付款金额^成功标识(S)^成功原因(null)^支付宝内部流水号^完成时间
 * @author 战马
 *
 */
public class BatPayCallBackDetail {
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
	private boolean isSuccess;
	private String reason;
	private String alipayFlowNumber;
	private String complectTime;
	public String getFlowNumber() {
		return flowNumber;
	}
	public String getAlipayFlowNumber() {
		return alipayFlowNumber;
	}
	public void setAlipayFlowNumber(String alipayFlowNumber) {
		this.alipayFlowNumber = alipayFlowNumber;
	}
	public boolean isSuccess() {
		return isSuccess;
	}
	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
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
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	
	public String getComplectTime() {
		return complectTime;
	}
	public void setComplectTime(String complectTime) {
		this.complectTime = complectTime;
	}
}
