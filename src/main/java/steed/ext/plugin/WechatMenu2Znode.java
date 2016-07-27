package steed.ext.plugin;

import java.util.ArrayList;
import java.util.List;

import steed.ext.domain.plugin.Znode;
import steed.util.wechat.domain.send.Button;
import steed.util.wechat.domain.send.Menu;

public class WechatMenu2Znode {
	public List<Znode> getZnodes(Menu menu){
		int id = 1;
		int childrenId = 10000;
		List<Znode> list = new ArrayList<Znode>();
		if(menu != null && menu.getButton() != null){
			for (Button temp:menu.getButton()) {
				Znode znode = new Znode();
	//			znode.setChildOuter(true);
				znode.setName(temp.getName());
				znode.setId(id);
				znode.setpId(0);
				znode.setType(temp.getType());
				znode.setKey(temp.getKey());
				znode.setUrl(temp.getUrl());
				//微信菜单只有二级，不递归
				if (temp.getSub_button() != null) {
					for (Button b:temp.getSub_button()) {
						Znode children = new Znode();
						children.setName(b.getName());
						children.setId(childrenId++);
						children.setpId(id);
						children.setType(b.getType());
						children.setKey(b.getKey());
						children.setUrl(b.getUrl());
						list.add(children);
					}
				}
				id++;
				list.add(znode);
			}
		}
		return list;
	}
	
	
	
}
