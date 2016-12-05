package steed.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import steed.util.base.BaseUtil;
import steed.util.wechat.WechatInterfaceInvokeUtil;
import steed.util.wechat.domain.result.BaseWechatResult;
import steed.util.wechat.domain.send.Button;
import steed.util.wechat.domain.send.Menu;
import steed.util.wechat.domain.send.RefundSend;
import steed.util.wechat.domain.send.SetIndustrySend;
import steed.util.wechat.domain.send.TemplateMessageSend;

public class WechatTest {
	
	/**
	 * 退款
	 */
	@Test
	public void sendRefund(){
		RefundSend refundSend = new RefundSend();
		refundSend.setOp_user_id("战马");
		refundSend.setOut_trade_no("20150818000010");
		refundSend.setTotal_fee(1);
		refundSend.setRefund_fee(1);
		refundSend.setOut_refund_no("20150818000010");
		BaseUtil.outJson(WechatInterfaceInvokeUtil.reFund(refundSend));
	}
	/**
	 * 设置公众号订单
	 */
	@Test
	public void setMenus2(){
		Menu menu = new Menu();
		Button button = new Button();
		button.setType("view");
		button.setName("首页");
		button.setUrl("http://battlesteed.cn");
		Button button2 = new Button();
		button2.setType("view");
		button2.setName("战马的博客");
		button2.setUrl("http://blog.battlesteed.cn");
		
		List<Button> list = new ArrayList<Button>();
		list.add(button2);
		list.add(button);
		menu.setButton(list);
		BaseUtil.outJson(WechatInterfaceInvokeUtil.setMenu(menu));
	}
	
	/**
	 * 发送模板消息
	 */
	@Test
	public void sendTemplateMessage(){
		TemplateMessageSend send = new TemplateMessageSend();
		send.setTouser("omxe4s9cr4DdT7pMIH40zgRvOB6o");
		send.setTopcolor("#ff0000");
		send.addData("productType", "#ff00ff", "ddd");
		BaseUtil.outJson(WechatInterfaceInvokeUtil.sendTemplateMessage(send, "OTM005"));
	}
	
	
	/**
	 * 设置微信模板消息所属行业
	 */
	@Test
	public void testSetInsturdy(){
		SetIndustrySend send = new SetIndustrySend();
		send.setIndustry_id1("4");
		send.setIndustry_id2("9");
		BaseWechatResult setIndustry = WechatInterfaceInvokeUtil.setIndustry(send);
		BaseUtil.outJson(setIndustry);
		BaseUtil.outJson(setIndustry.getMessage());
	}
	
	
}
