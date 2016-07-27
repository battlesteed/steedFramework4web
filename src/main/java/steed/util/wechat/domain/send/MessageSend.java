package steed.util.wechat.domain.send;

import java.util.HashMap;
import java.util.Map;


/**
 * 消息预览
 * @author 战马
 *
 */
public class MessageSend extends BaseMessageSend {
	private Map<String, Object> filter = new HashMap<String, Object>();
	
	
	public void setIs_to_all(Boolean is_to_all){
		filter.put("is_to_all",is_to_all);
	}
	public void setGroup_id(String group_id){
		filter.put("group_id",group_id);
	}
	
	public Map<String, Object> getFilter() {
		return filter;
	}

	public void setFilter(Map<String, Object> filter) {
		this.filter = filter;
	}
	
}
