package steed.test.orm;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import steed.ext.domain.information.Information;
import steed.ext.domain.information.Programa;
import steed.util.UtilsUtil;
import steed.util.base.BaseUtil;
import steed.util.dao.DaoUtil;
import steed.util.dao.HibernateUtil;

public class TestSonQuery {
	@Test
	public void t1(){
		UtilsUtil.initUtils();
		Map<String, Object> map = new HashMap<>();
		Information obj = new Information();
		Programa programa = new Programa();
//		programa.setName("3333");
		programa.setDescription("wwww");
		obj.setPrograma(programa);
//		DaoUtil.putField2Map(obj, map);
		DaoUtil.getHql(Information.class, map, null, null, "");
		BaseUtil.out(DaoUtil.listAllObj(obj).size());
	}
	@Test
	public void t2(){
		String hql = "select information_steed_00 from steed.ext.domain.information.Information information_steed_00 where 1=1 and information_steed_00.programa.description = 'wwww' ";
		UtilsUtil.initUtils();
		BaseUtil.out(DaoUtil.createQuery(new HashMap<>(), new StringBuffer(hql)).list().size());
	}
}
