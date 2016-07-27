package steed.ext.engine.wechat;

import steed.util.wechat.domain.sys.MessageReceive;
/**
 * 微信菜单点击相应引擎
 * @author 战马
 *
 */
public class MenuClickEngine implements MessageEngine {

	/*@Override
	public String getMessage(Map<String, String> requestMap) {
		String evenType = requestMap.get("Event");
		if("click".equalsIgnoreCase(evenType)){
			NewsMessage newsMessage = new NewsMessage();
			MessageUtil.fitMessage(requestMap, newsMessage);
			String[] ids = requestMap.get("EventKey").split("\\,");
			Matter2ResponseArticlePlugin plugin = new Matter2ResponseArticlePlugin();
			List<Matter> matterList = DaoUtil.listByKeys(Matter.class, ids);
			List<Article> articleList = new ArrayList<Article>(matterList.size());
			for (Matter temp:matterList) {
				articleList.add(plugin.matter2Article(temp));
			}
			newsMessage.setArticles(articleList);
			return MessageUtil.newsMessageToXml(newsMessage);
		}
		return "";
	}
*/
	@Override
	public String getMessage(MessageReceive messageReceive) {
		/*String evenType = messageReceive.getEvent();
		if("click".equalsIgnoreCase(evenType)){
			NewsMessage newsMessage = new NewsMessage();
			MessageUtil.fitMessage(messageReceive, newsMessage);
			String[] ids = messageReceive.getEventKey().split("\\,");
			Matter2ResponseArticlePlugin plugin = new Matter2ResponseArticlePlugin();
			List<Matter> matterList = DaoUtil.listByKeys(Matter.class, ids);
			List<Article> articleList = new ArrayList<Article>(matterList.size());
			for (Matter temp:matterList) {
				articleList.add(plugin.matter2Article(temp));
			}
			newsMessage.setArticles(articleList);
			return MessageUtil.newsMessageToXml(newsMessage);
		}*/
		return "";
	}
}
