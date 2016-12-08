package steed.util.system;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import steed.domain.system.LogisticsCompany;
import steed.hibernatemaster.util.DaoUtil;
import steed.util.base.BaseUtil;
import steed.util.http.HttpUtil;

/**
 * 物流查询工具
 * 
 * @author 战马
 * @email java@beyondstar.com.cn battle_steed@163.com
 * @company 深圳市星超越科技有限公司
 */
public class LogisticsUtil {
	private static final String queryUrl = "http://www.kuaidi100.com/query?type=#TYPE#&postid=#POSTID#";
	private static final Logger log = LoggerFactory.getLogger(LogisticsUtil.class);
	/**
	 * 
	 * @param companyCode 见LogisticsCompany表
	 * @param number 物流单号
	 * @return
	 */
	public static QueryResult Query(String companyCode,String number) {
		String requestString = HttpUtil.getRequestString(HttpUtil.http_get, 
				queryUrl.replaceAll("#TYPE#", companyCode).replaceAll("#POSTID#", number), null, null);
		log.debug("公司编码:{},物流单号:{},\n返回结果:{}", new Object[]{companyCode,number,requestString});
		return BaseUtil.parseJson(requestString, QueryResult.class);
	}
	
	
	/**
	 * 
	 * @param companyCode 快递公司名 见LogisticsCompany表
	 * @param number 物流单号
	 * @return
	 */
	public static QueryResult QueryByCompanyName(String companyName,String number) {
		LogisticsCompany logisticsCompany = DaoUtil.get(LogisticsCompany.class, companyName);
		String code = "";
		if (logisticsCompany != null) {
			code = logisticsCompany.getCode();
		}
		return Query(code, number);
	}

	public class QueryResult {
		private String nu;

		private String message;

		private String companytype;

		private String ischeck;

		private String com;

		private String updatetime;

		private String status;

		private String condition;

		private String codenumber;

		private List<Data> data;
		/**
		 * 0：在途，即货物处于运输过程中；
		 *	1：揽件，货物已由快递公司揽收并且产生了第一条跟踪信息；
		 *	2：疑难，货物寄送过程出了问题；
			3：签收，收件人已签收；
			4：退签，即货物由于用户拒签、超区等原因退回，而且发件人已经签收；
			5：派件，即快递正在进行同城派件；
			6：退回，货物正处于退回发件人的途中；
		 */
		private String state;

		public String getNu() {
			return nu;
		}

		public void setNu(String nu) {
			this.nu = nu;
		}
		public boolean isSuccess(){
			return "ok".equals(message);
		}
		/**
		 * 是否已签收
		 * @return
		 */
		public boolean isSign(){
			return "3".equals(state)||"1".equals(ischeck);
		}
		public String getMessage() {
			return message;
		}
		
		public String getStateStr(){
			int sta = Integer.parseInt(state);
			String temp;
			switch (sta) {
			case 0:
				temp = "在途";
				break;
			case 1:
				temp = "揽件";
				break;
			case 2:
				temp = "疑难";
				break;
			case 3:
				temp = "签收";
				break;
			case 4:
				temp = "退签";
				break;
			case 5:
				temp = "派件";
				break;
			case 6:
				temp = "退回";
				break;

			default:
				temp = "";
				break;
			}
			return temp;
		}
		
		public void setMessage(String message) {
			this.message = message;
		}

		public String getCompanytype() {
			return companytype;
		}

		public void setCompanytype(String companytype) {
			this.companytype = companytype;
		}

		public String getIscheck() {
			return ischeck;
		}

		public void setIscheck(String ischeck) {
			this.ischeck = ischeck;
		}

		public String getCom() {
			return com;
		}

		public void setCom(String com) {
			this.com = com;
		}

		public String getUpdatetime() {
			return updatetime;
		}

		public void setUpdatetime(String updatetime) {
			this.updatetime = updatetime;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public String getCondition() {
			return condition;
		}

		public void setCondition(String condition) {
			this.condition = condition;
		}

		public String getCodenumber() {
			return codenumber;
		}

		public void setCodenumber(String codenumber) {
			this.codenumber = codenumber;
		}

		public List<Data> getData() {
			return data;
		}

		public void setData(List<Data> data) {
			this.data = data;
		}

		public String getState() {
			return state;
		}

		public void setState(String state) {
			this.state = state;
		}
		
	}

	public class Data {
		private String time;

		private String location;

		private String context;

		private String ftime;

		public String getTime() {
			return time;
		}

		public void setTime(String time) {
			this.time = time;
		}

		public String getLocation() {
			return location;
		}

		public void setLocation(String location) {
			this.location = location;
		}

		public String getContext() {
			return context;
		}

		public void setContext(String context) {
			this.context = context;
		}

		public String getFtime() {
			return ftime;
		}

		public void setFtime(String ftime) {
			this.ftime = ftime;
		}

	}

}
