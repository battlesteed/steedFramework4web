package steed.alipay.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 阿里妈妈批量支付回调实体类
 * 参数 	参数名称 	类型（长度范围） 	参数说明 	是否可为空 	样例
notify_time 	通知时间 	Date 	通知的发送时间。 格式yyyy-MM-dd HH:mm:ss。 	不可空 	2009-08-12 11:08:32
notify_type 	通知类型 	String 	通知的类型。 	不可空 	batch_trans_notify
notify_id 	通知校验ID 	String 	支付宝通知校验ID，商户可以用这个流水号询问支付宝该条通知的合法性。 	不可空 	70fec0c2730b27528665af4517c27b95
sign_type 	签名方式 	String 	DSA、RSA、MD5三个值可选，必须大写。 	不可空 	DSA
sign 	签名 	String 	请参见“签名验证”。 	不可空 	_p_w_l_h_j0b_gd_aejia7n_ko4_m%2Fu_w_jd3_nx_s_k_mxus9_hoxg_y_r_lunli_pmma29_t_q==
batch_no 	批次号 	String 	转账批次号。 	不可空 	20100101001
pay_user_id 	付款账号ID 	String 	付款的支付宝账号对应的支付宝唯一用户号。 以2088开头的16位纯数字组成。 	不可空 	2088002464631181
pay_user_name 	付款账号姓名 	String 	付款账号姓名。 	不可空 	毛毛
pay_account_no 	付款账号 	String 	付款账号。 	不可空 	20880024646311810156
success_details 	转账成功的详细信息 	String 	批量付款中成功付款的信息。 格式为：流水号^收款方账号^收款账号姓名^付款金额^成功标识(S)^成功原因(null)^支付宝内部流水号^完成时间。 每条记录以“|”间隔。 	可空 	0315001^gonglei1@handsome.com.cn^龚本林^20.00^S^null^200810248427067^20081024143652|
fail_details 	转账失败的详细信息 	String 	批量付款中未成功付款的信息。 格式为：流水号^收款方账号^收款账号姓名^付款金额^失败标识(F)^失败原因^支付宝内部流水号^完成时间。 每条记录以“|”间隔。 	可空 	0315006^xinjie_xj@163.com^星辰公司1^20.00^F^TXN_RESULT_TRANSFER_OUT_CAN_NOT_EQUAL_IN^200810248427065^200810
 * @author 战马
 */
public class BatPayCallBack implements Serializable{
	private static final long serialVersionUID = -2558416540059349846L;
	private String sign;

	private String notify_time;

	private String pay_user_id;

	private String pay_user_name;

	private String sign_type;

	private List<BatPayCallBackDetail> successDetailList;
	private String success_details;
	private List<BatPayCallBackDetail> failDetailList;
	private String fail_details;

	private String notify_type;

	private String pay_account_no;

	private String notify_id;

	private String batch_no;

	public String getSign() {
		return sign;
	}

	private List<BatPayCallBackDetail> parseBatPayCallBackDetail(String str){
		if (str == null) {
			return null;
		}else if("".equals(str)){
			return new ArrayList<BatPayCallBackDetail>();
		}
		List<BatPayCallBackDetail> list = new ArrayList<BatPayCallBackDetail>();
		for (String temp:str.split("\\|")) {
			String[] detail = temp.split("\\^");
			
			BatPayCallBackDetail callBackDetail = new BatPayCallBackDetail();
			callBackDetail.setFlowNumber(detail[0]);
			callBackDetail.setReceiveAccount(detail[1]);
			callBackDetail.setRealName(detail[2]);
			callBackDetail.setAmount(Double.parseDouble(detail[3]));
			callBackDetail.setSuccess("S".equalsIgnoreCase(detail[4]));
			callBackDetail.setReason(detail[5]);
			callBackDetail.setAlipayFlowNumber(detail[6]);
			callBackDetail.setComplectTime(detail[7]);
			
			list.add(callBackDetail);
		}
		return list;
	}
	
	public List<BatPayCallBackDetail> getSuccessDetailList() {
		if (successDetailList == null) {
			successDetailList = parseBatPayCallBackDetail(success_details);
		}
		return successDetailList;
	}

	public List<BatPayCallBackDetail> getFailDetailList() {
		if (failDetailList == null) {
			failDetailList = parseBatPayCallBackDetail(fail_details);
		}
		return failDetailList;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getNotify_time() {
		return notify_time;
	}

	public void setNotify_time(String notify_time) {
		this.notify_time = notify_time;
	}

	public String getPay_user_id() {
		return pay_user_id;
	}

	public void setPay_user_id(String pay_user_id) {
		this.pay_user_id = pay_user_id;
	}

	public String getPay_user_name() {
		return pay_user_name;
	}

	public void setPay_user_name(String pay_user_name) {
		this.pay_user_name = pay_user_name;
	}

	public String getSign_type() {
		return sign_type;
	}

	public void setSign_type(String sign_type) {
		this.sign_type = sign_type;
	}

	public String getSuccess_details() {
		return success_details;
	}

	public void setSuccess_details(String success_details) {
		this.success_details = success_details;
	}

	public String getFail_details() {
		return fail_details;
	}

	public void setFail_details(String fail_details) {
		this.fail_details = fail_details;
	}

	public String getNotify_type() {
		return notify_type;
	}

	public void setNotify_type(String notify_type) {
		this.notify_type = notify_type;
	}

	public String getPay_account_no() {
		return pay_account_no;
	}

	public void setPay_account_no(String pay_account_no) {
		this.pay_account_no = pay_account_no;
	}

	public String getNotify_id() {
		return notify_id;
	}

	public void setNotify_id(String notify_id) {
		this.notify_id = notify_id;
	}

	public String getBatch_no() {
		return batch_no;
	}

	public void setBatch_no(String batch_no) {
		this.batch_no = batch_no;
	}
}
