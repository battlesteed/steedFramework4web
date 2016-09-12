package steed.util.wechat.domain.result;

import java.util.List;

public class ArticleSummaryResult extends BaseWechatResult{
	private List<ArticleSummaryResultItem> list;

	public List<ArticleSummaryResultItem> getList() {
		return list;
	}

	public void setList(List<ArticleSummaryResultItem> list) {
		this.list = list;
	}
}
