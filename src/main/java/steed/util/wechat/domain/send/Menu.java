package steed.util.wechat.domain.send;

import java.util.List;

import steed.util.wechat.domain.result.BaseWechatResult;

public class Menu extends BaseWechatResult{
	private List<Button> button;

	public List<Button> getButton() {
		return button;
	}

	public void setButton(List<Button> button) {
		this.button = button;
	}

}
