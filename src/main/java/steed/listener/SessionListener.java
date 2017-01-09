package steed.listener;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import steed.hibernatemaster.util.DaoUtil;

public class SessionListener implements HttpSessionListener{
	private Map<Object, HttpSession> map = new HashMap<>();
	public Map<Object, HttpSession> getAllSession() {
		return map;
	}

	@Override
	public void sessionCreated(HttpSessionEvent event) {
		HttpSession session = event.getSession();
		Object sessionID = getSessionID(session);
		if (sessionID != null) {
			map.put(sessionID, session);
		}
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent event) {
		map.remove(event.getSession().getId());
	}
	
	/**
	 * 获取session的id,你可以重写该方法,返回保存在session中的用户id,实现统计在线人数等
	 * @param session
	 * @return
	 */
	protected Object getSessionID(HttpSession session) {
		return session.getId();
	}

}
