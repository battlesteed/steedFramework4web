package steed.ext.engine.wechat;

import steed.engine.wechat.MessageEngine;
import steed.util.wechat.domain.sys.MessageReceive;
/**
 * 扫描事件回复引擎
 * @author 战马
 *
 */
public class ScanEvenEngine implements MessageEngine {

	@Override
	public String getMessage(MessageReceive messageReceive) {
		String bonusCode = messageReceive.getScanCodeInfo().getScanResult();
//		return new SendRedPacketEngine().sweepStake(messageReceive, bonusCode);
		return "";
	}
	

}
