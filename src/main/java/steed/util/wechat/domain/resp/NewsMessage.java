package steed.util.wechat.domain.resp;

import java.util.List;

import steed.util.wechat.MessageUtil;

/**
 * 文本消息
 * 
 * @author liufeng
 * @date 2013-05-19
 */
public class NewsMessage extends BaseMessage {
	// 图文消息个数，限制为10条以内
	private int ArticleCount;
	// 多条图文消息信息，默认第一个item为大图
	private List<Article> Articles;

	public NewsMessage() {
		super();
		setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
	}

	public int getArticleCount() {
		return ArticleCount;
	}

	public void setArticleCount(int articleCount) {
		ArticleCount = articleCount;
	}

	public List<Article> getArticles() {
		return Articles;
	}

	public void setArticles(List<Article> articles) {
		Articles = articles;
		ArticleCount = articles.size();
	}
}