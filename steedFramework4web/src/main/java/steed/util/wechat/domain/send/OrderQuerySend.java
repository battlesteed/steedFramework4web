package steed.util.wechat.domain.send;

/**
 * 订单查询
 * @author 战马
 */
public class OrderQuerySend extends OrderSend {
	
	private String transaction_id;

	public String getTransaction_id() {
		return transaction_id;
	}

	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
	}
}
