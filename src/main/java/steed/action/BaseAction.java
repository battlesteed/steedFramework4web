package steed.action;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import steed.action.annotation.ValidateMe;
import steed.action.annotation.ValidateUser;
import steed.domain.BaseDatabaseDomain;
import steed.domain.BaseDomain;
import steed.domain.BaseUser;
import steed.domain.GlobalParam;
import steed.domain.application.Page;
import steed.exception.runtime.system.AttackedExeception;
import steed.exception.runtime.system.FrameworkException;
import steed.other.SteedHttpServletRequest;
import steed.util.base.BaseUtil;
import steed.util.base.ContextUtil;
import steed.util.base.DomainUtil;
import steed.util.base.PropertyUtil;
import steed.util.base.StringUtil;
import steed.util.dao.DaoUtil;
import steed.util.dao.HibernateUtil;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
/**
 * 基础action类，所以action均需继承该类
 * @author 战马
 *
 * @email battle_steed@163.com
 */
@SuppressWarnings("rawtypes")
public abstract class BaseAction<SteedDomain extends BaseDatabaseDomain> extends ActionSupport implements ModelDriven{
	private static final long serialVersionUID = 7774350640186420795L;
	protected int currentPage = 1;
	
	/**
	 *   ┏┓　　　┏┓
	 * ┏┛┻━━━┛┻┓
	 * ┃　　　　　　　┃
	 * ┃　　　━　　　┃
	 * ┃　┳┛　┗┳　┃
	 * ┃　　　　　　　┃
	 * ┃　　　┻　　　┃
	 * ┃　　　　　　　┃
	 * ┗━┓　　　┏━┛Code is far away from bug with the animal protecting
	 *    ┃　　　┃   神兽保佑
	 *    ┃　　　┃   代码无BUG!
	 *    ┃　　　┗━━━┓
	 *    ┃　　　　　　　┣┓
	 *    ┃　　　　　　　┏┛
	 *    ┗┓┓┏━┳┓┏┛
	 *      ┃┫┫　┃┫┫
	 *      ┗┻┛　┗┻┛
	 */
	
	protected SteedDomain domain;
	
	/**
	 * 分页大小
	 */
	protected int pageSize = StringUtil.isStringEmpty(PropertyUtil.getConfig("page.size"))==true ? 10 : Integer.parseInt(PropertyUtil.getConfig("page.size"));
	/**
	 * 若不想配results，就return该值，则会自动forward到该act对应的jsp页面
	 * (例如该action的Namespace是“admin”，调用的方法是"index",则对应的jsp路径为“/WEB-INF/jsp/admin/index.jsp”)
	 */
	public static final String steed_forward = "steed_forward";
	public int getCurrentPage() {
		return currentPage;
	}
	
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	/**
	 * 往request中放东西
	 * @param key 键
	 * @param obj 值
	 */
	protected void setRequestAttribute(String key,Object obj){
		getRequest().setAttribute(key, obj);
	}
	/**
	 * 从request中取东西
	 * @param key 键
	 */
	protected Object getRequestAttribute(String key){
		return getRequest().getAttribute(key);
	}
	/**
	 * 获取request中的参数
	 * @param key 键
	 */
	protected String getRequestParameter(String key){
		return getRequest().getParameter(key);
	}
	/**
	 * 获取request中的参数
	 * @param key 键
	 */
	protected boolean isRequestParameterEmpty(String key){
		return StringUtil.isStringEmpty(getRequest().getParameter(key));
	}
	protected String[] getRequestParameters(String key){
		return getRequest().getParameterValues(key);
	}
	/**
	 * 往session中放东西
	 * @param key 键
	 * @param obj 值
	 */
	protected void setSessionAttribute(String key,Object obj){
		getSession().setAttribute(key, obj);
	}
	
	public ServletContext getServletContext() {
		//艹，兼容Servlet2.5只能这样写
		return getRequest().getSession().getServletContext();
	}
	public HttpServletResponse getResponse() {
		return ServletActionContext.getResponse();
	}
	public HttpServletRequest getRequest() {
		return ServletActionContext.getRequest();
//		return ContextUtil.getRequest();
	}
	public SteedHttpServletRequest getSteedRequest() {
		return ContextUtil.getRequest();
	}
	
	protected void addCookie(String name,String value) {
		addCookie(name,value,"/",15 * 24 * 60 * 60);
	}
	
	protected void removeCookie(String name) {
		removeCookie(name,"/");
	}
	
	protected void removeCookie(String name,String path) {
		addCookie(name,null,"/",0);
	}
	
	protected void afterDomainCreate(SteedDomain domain){}
	
	protected void addCookie(String name,String value,String path,int maxAge) {
		Cookie cookie = new Cookie(name, value);
		cookie.setPath(path);
		cookie.setMaxAge(maxAge);
		getResponse().addCookie(cookie);
	}
	
	public Cookie getCookie(String name) {
		for(Cookie temp:getRequest().getCookies()){
			if (temp.getName().equals(name)) {
				return temp;
			}
		}
		return null;
	}
	
	public BaseAction() {
		super();
//		domain = getModel();
	}

	public HttpSession getSession() {
		return getRequest().getSession();
	}
	/**
	 * 获取登陆的用户
	 * @return
	 */
	public BaseUser getLoginUser() {
		return (BaseUser) getSession().getAttribute(GlobalParam.attribute.user);
	}
	/**
	 * 往request中放page，key是page
	 * @param page
	 */
	protected void setRequestPage(Page page){
		setRequestAttribute("page", page);
	}
	protected void setRequestDomainList(List list){
		setRequestAttribute("domainList", list);
	}
	protected void setRequestDomain(Object obj){
		setRequestAttribute("domain", obj);
	}
	@SuppressWarnings("unchecked")
	protected Page<SteedDomain> getRequestPage(){
		Object requestAttribute = getRequestAttribute("page");
		if (requestAttribute == null) {
			Page<SteedDomain> page = new Page<SteedDomain>(); 
			page.setCurrentPage(currentPage);
			page.setPageSize(pageSize);
			return page;
		}
		return (Page<SteedDomain>) requestAttribute;
	}

	/**
	 * 通过model注解获取action中的model
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private SteedDomain getModelByReflect(){
		if (domain != null) {
			return domain;
		}
		try {
			ParameterizedType parameterizedType = (ParameterizedType)this.getClass().getGenericSuperclass();
			Class<SteedDomain> clazz = (Class<SteedDomain>) (parameterizedType.getActualTypeArguments()[0]); 
			try {
				domain = clazz.newInstance();
				afterDomainCreate(domain);
				return domain;
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
				throw new FrameworkException(e);
			}
		} catch (Exception e) {
		}
		return null;
	}
	
/*	@SuppressWarnings("unchecked")
	private Field getModelField(Class<? extends BaseAction> clazz){
		Field[] fields = clazz.getDeclaredFields();
		for(Field f:fields){
			Annotation a = f.getAnnotation(Model.class);
			if (a == null) {
				continue;
			}
			return f;
		}
		if (clazz == BaseAction.class) {
			return null;
		}
		return getModelField((Class<? extends BaseAction>)clazz.getSuperclass());
	}
	*/
	
	protected Object afterSave(){
		return null;
	}
	
	
	@Override
	public SteedDomain getModel() {
		return getModelByReflect();
	}


	/**
	 * 把对象以json形式写到输出流，一般用于ajax
	 * @param obj
	 */
	protected void writeObjectMessage(Object obj){
		if (obj == null) {
			return;
		}
		String json = BaseUtil.getJson(obj);
		BaseUtil.getLogger().debug("返回json----->"+json);
		writeString(json);
	}
	
	/**
	 * 把string写到客户端
	 * @param string
	 */
	protected void writeString(String string){
		try {
			ServletOutputStream out;
			out = ServletActionContext.getResponse().getOutputStream();
			out.write(StringUtil.getSystemCharacterSetBytes(string));
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * 自动把model作为查询条件查询数据库
		（关于如何定义实体类并用model作为查询条件请看cn.com.beyondstar.domain.people.People）
		并把结果集放到steed.domain.application.Page中并把page
		以"page"为key放到request域中
		你要做的只是从jsp页面读取page中的结果集并显示出来而已
	 * 
	 * @return steed_forward
	 */
	protected String index(){
		return index(null, null);
	}
	protected String index(List<String> desc,List<String> asc){
		beforeIndex();
		BaseDomain model = getModel();
		DomainUtil.FuzzyQueryInitialize(model);
		setRequestPage(DaoUtil.listObj(pageSize,currentPage, model,desc,asc));
		afterIndex();
		return steed_forward;
	}
 
	protected String add(){
		beforeAdd();
		return steed_forward;
	}
	protected String lookOver(){
		getDomainAndSet2Request();
		return steed_forward;
	}
	/**
	 * 以model字段名做为key，把baseDao.get(modelID)获得的的实体类放到request域
	 */
	protected void getDomainAndSet2Request() {
		setDomain2Request(getModelFromDatabase());
	}
	
	protected SteedDomain getModelFromDatabase() {
		return getModel().smartGet();
	}
	
	/**
	 * 把查询到的domain以domain为key放到request域，常用于跳转到edit页面时
	 * @param domain
	 */
	protected void setDomain2Request(BaseDomain domain){
		setRequestAttribute("domain", domain);
	}
	/**
	 * 获取request域中的domain
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected SteedDomain getRequestDomain(){
		return (SteedDomain) getRequestAttribute("domain");
	}
	
	/**
	 * 通用delete方法
	 * 
	 * 	删除model对应的表中主键为DomainUtil.getDomainId(model)的记录
	 * 	并以json格式返回删除状态（成功与否）到前端，
	 *
	 * @return null
	 */
	protected String delete(){
		beforeDelete();
		BaseDatabaseDomain model = (BaseDatabaseDomain) getModel();
		model.delete();
		writeObjectMessage(afterDelete());
		return null;
	}
	
	/**
	 * 把model保存到数据库，
	 * 并以json格式返回保存状态（成功与否）到前端,
	 * @return null
	 */
	protected String save(){
		beforeSave();
		saveDomain();
		writeObjectMessage(afterSave());
		return null;
	}
	
	protected void beforeSave(){
	}
	

	protected void beforeAdd(){
	}
	protected void beforeEdit(){
	}
	protected void beforeUpdate(){
	}
	
	protected Object afterUpdate(){
		return null;
	}
	
	protected void beforeIndex(){
	}
	
	protected void afterIndex(){
	}
	
	protected void beforeDelete(){
	}
	protected Object afterDelete(){
		return null;
	}
	
	protected boolean saveDomain() {
		BaseDatabaseDomain model = getModel();
		return model.save();
	}

	/**
	 * 	查询model对应的表中主键为DomainUtil.getDomainId(model)的记录
	 * 	并以"domain"为key保存到request域，
	 *	你只需在jsp页面读取domain中的数据并显示出来让用户编辑即可
	 * @return steed_forward
	 */
	protected String edit(){
		beforeEdit();
		getDomainAndSet2Request();
		return steed_forward;
	}
	/**
	 * 将model更新到数据库，并以json格式返回更新状态（成功与否）到前端
	 * 你可能需要steed.action.BaseAction.updateNotNullField()
	 * 
	 * @see #updateNotNullField
	 */
	protected String update(){
		beforeUpdate();
		((BaseDatabaseDomain)getModel()).update();
		writeObjectMessage(afterUpdate());
		return null;
	}
	/**
	 * 将model不为空的字段更新到数据库，并以json格式返回更新状态（成功与否）到前端
	 * 你可能需要steed.action.BaseAction.update()
	 * @return null
	 */
	protected String updateNotNullField(){
		return updateNotNullField(null);
	}
	
	@Override
	public void validate() {
		super.validate();
		if (this.getClass().getAnnotation(ValidateUser.class) != null) {
			validateUserBelond();
		}
		if (this.getClass().getAnnotation(ValidateMe.class) != null) {
			validateCurrentUser();
		}
	}
	
	
	/**
	 * 校验当前model是否属于当前登录用户，防止别人利用modeldriven篡改实体类所属用户
	 * 或偷看当前实体类
	 */
	protected void validateUserBelond() {
		BaseUser domainUser = DomainUtil.getDomainUser(getModel());
		if (!getLoginUser().equals(domainUser)) {
			throw new AttackedExeception("请勿冒用他人账号！！");
		}
		if (!BaseUtil.isObjEmpty(DomainUtil.getDomainId(getModel()))) {
			BaseDomain modelFromDatabase = getModelFromDatabase();
			if (modelFromDatabase != null) {
				if (!getLoginUser().equals(DomainUtil.getDomainUser(modelFromDatabase))) {
					throw new AttackedExeception("请勿冒用其他用户！！！！");
				}else {
					HibernateUtil.getSession().evict(domainUser);
				}
			}
		}
	}
	/**
	 * 校验当前model是否是当前登录用户，防止别人利用modeldriven篡改当前用户
	 * 或偷看其它用户实体类
	 */
	protected void validateCurrentUser() {
		if (!getLoginUser().equals(getModel())) {
			throw new AttackedExeception("请勿冒用他人账号！！");
		}
	}
	
	protected boolean isDomainBelongCurrentUser(){
		return getLoginUser().equals(DomainUtil.getDomainUser(getModel()));
	}
	
	protected String updateNotNullField(List<String> updateEvenNull){
		updateNotNullField(updateEvenNull, true);
		return null;
	}
	
	protected String updateNotNullField(List<String> updateEvenNull,boolean strictlyMode){
		beforeUpdate();
		BaseDatabaseDomain model = (BaseDatabaseDomain) getModel();
		model.updateNotNullField(updateEvenNull,strictlyMode);
		writeObjectMessage(afterUpdate());
		return null;
	}
}
