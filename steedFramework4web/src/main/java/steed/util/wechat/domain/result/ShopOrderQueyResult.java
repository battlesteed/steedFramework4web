package steed.util.wechat.domain.result;

import java.util.List;

public class ShopOrderQueyResult extends BaseWechatResult{
	private List<ShopOrder> order_list;

	public List<ShopOrder> getOrder_list() {
		return order_list;
	}

	public void setOrder_list(List<ShopOrder> order_list) {
		this.order_list = order_list;
	}
	
}
