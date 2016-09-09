package steed.test;

import steed.util.base.BaseUtil;

public class Run {
	public static void p(String name){
		synchronized (name) {
			BaseUtil.out(name,"in");
			while (true) {
				BaseUtil.out(name);
				try {
					Thread.sleep(1000*3);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
