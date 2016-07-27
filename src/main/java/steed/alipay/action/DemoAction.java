package steed.alipay.action;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;

import steed.action.BaseAction;
import steed.alipay.domain.BatPay;
import steed.alipay.domain.PayDetail;

@Namespace("/alipay")
@ParentPackage(value="steed")
@InterceptorRefs({@InterceptorRef(value="mydefault")})
public class DemoAction extends BaseAction{
	
	@Action("demo")
	public String demo(){
		BatPay batPay = new BatPay();
		batPay.init();
		PayDetail payDetail = new PayDetail();
		payDetail.setAmount(0.01);
		payDetail.setFlowNumber("ddd");
		payDetail.setRealName("马将文");
		payDetail.setReceiveAccount("1255372919@qq.com");
		payDetail.setRemark("备注");
		List<PayDetail> list = new ArrayList<PayDetail>();
		list.add(payDetail);
		batPay.setPayDetailList(list);
		batPay.submit(getRequest(), getResponse());
		return null;
	}
	
}
