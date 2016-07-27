package steed.util.wechat.domain.result;

import steed.util.wechat.domain.send.Menu;

public class MenuResult extends BaseWechatResult{
	private Menu menu;

	public Menu getMenu() {
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}

}
