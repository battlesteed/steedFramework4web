package steed.alipay.domain;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import steed.exception.runtime.system.FrameworkException;
import steed.util.base.DateUtil;
import steed.util.base.PropertyUtil;
import steed.util.digest.AESUtil;
import steed.util.system.FlowUtil;

/**
 * 阿里妈妈批量支付实体类
 * 
 *  付款账号：
    * 必填 
付款账户名：
    * 必填，个人支付宝账号是真实姓名公司支付宝账号是公司名称 
付款当天日期：
    * 必填，格式：年[4位]月[2位]日[2位]，如：20100801 
批次号：
    * 必填，格式：当天日期[8位]+序列号[3至16位]，如：201008010000001 
付款总金额：
    * 必填，即参数detail_data的值中所有金额的总和 
付款笔数：
    * 必填，即参数detail_data的值中，“|”字符出现的数量加1，最大支持1000笔（即“|”字符出现的数量999个） 
付款详细数据：
    * 必填，格式：流水号1^收款方帐号1^真实姓名^付款金额1^备注说明1|流水号2^收款方帐号2^真实姓名^付款金额2^备注说明2.... 
 * 
 * @author 战马
 */
public class BatPay implements Serializable{
	private static final long serialVersionUID = -2558416540059349846L;
	/**
	 * 付款账号：
	 */
	private String payAccount;
	/**
	 * 付款账户名
	 */
	private String companyName;
	private String payDate;
	private String batNumber;
	private double totalAmount;
	private int totalCount;
	private List<PayDetail> payDetailList;
	private StringBuffer payDetailString = new StringBuffer();
	public List<PayDetail> getPayDetailList() {
		return payDetailList;
	}
	public void setPayDetailList(List<PayDetail> payDetailList) {
		this.payDetailList = payDetailList;
		totalAmount = 0;
		totalCount = 0;
		payDetailString = new StringBuffer();
		for (PayDetail temp:payDetailList) {
			totalAmount += temp.getAmount();
			totalCount++;
			payDetailString.append(temp.getFlowNumber())
							.append("^").append(temp.getReceiveAccount())
							.append("^").append(temp.getRealName())
							.append("^").append(temp.getAmount())
							.append("^").append(temp.getRemark()).append("|");
		}
	}
	
	
	public String getPayDetailString() {
		String string = payDetailString.toString();
		if (string.endsWith("|")) {
			string = string.substring(0, string.length()-1);
		}
		return string;
	}
	public String getPayAccount() {
		return payAccount;
	}
	public void setPayAccount(String payAccount) {
		this.payAccount = payAccount;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getPayDate() {
		return payDate;
	}
	public void setPayDate(String payDate) {
		this.payDate = payDate;
	}
	public String getBatNumber() {
		return batNumber;
	}
	public void setBatNumber(String batNumber) {
		this.batNumber = batNumber;
	}
	public double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	
	/**
	 * 调该方法提交支付数据
	 * @param request
	 * @param response
	 */
	public void submit(HttpServletRequest request,HttpServletResponse response){
		try {
			request.setAttribute("batPay", this);
			request.getRequestDispatcher("/WEB-INF/jsp/alipay/paybat/alipayapi.jsp").forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
			throw new FrameworkException(e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new FrameworkException(e);
		}
	}
	
	/**
	 * 初始化,从配置文件读取付款账户等
	 */
	public void init(){
		payAccount = AESUtil.aesDecode(PropertyUtil.getConfig("alipay.seller_email"));
		companyName = PropertyUtil.getConfig("alipay.companyName");
		payDate = DateUtil.getStringFormatDate(new Date(), "yyyyMMdd");
		batNumber = FlowUtil.getFlowString("alipayFlow", 6, true);
	}
}
