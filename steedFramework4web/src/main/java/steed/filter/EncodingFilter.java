package steed.filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import steed.util.base.PropertyUtil;
import steed.util.base.StringUtil;
/**
 * 编码过滤器，全局解决乱码问题，包括URL带中文参数时request.getParameter(String name)乱码问题。
 * 请在config.properties配置服务器编码和项目编码
 * @author 战马
 *
 */
public class EncodingFilter implements Filter {
	private String serverEncoding;
	private String characterSet;
	@Override
	public void destroy() {
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		request.setCharacterEncoding(characterSet);
		response.setCharacterEncoding(characterSet);
		response.setContentType("text/html;charset=" + characterSet);
		if (serverEncoding.equalsIgnoreCase(characterSet)) {
			chain.doFilter(request, response);
		}else {
//			chain.doFilter(new RequestEncodingInterceptor().createProxy(request), response);
//			chain.doFilter(new RequestEncodingInterceptor().createProxy((HttpServletRequest)request), response);
			chain.doFilter(new RequestEncodingWrapper((HttpServletRequest)request), response);
		}
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		serverEncoding = PropertyUtil.getConfig("serverEncoding");
		characterSet = PropertyUtil.getConfig("characterSet");
	}
	
	/**
	 * 解决URL带中文参数时request.getParameter(name)乱码问题
	 * @author battle_steed
	 */
	private class RequestEncodingWrapper extends HttpServletRequestWrapper{
		private Map<String, String[]> map;
		public RequestEncodingWrapper(HttpServletRequest request) {
			super(request);
			Map<String, String[]> tempMap = super.getParameterMap();
			
			if ("GET".equals(request.getMethod().toUpperCase())) {
				map = new HashMap<String, String[]>();
				for (String key:tempMap.keySet()) {
					String[] params = tempMap.get(key);
					String[] newParams = new String[params.length];
					for (int i = 0; i < params.length; i++) {
						newParams[i] = enCode(params[i]);
					}
					map.put(key, newParams);
				}
			}else {
				/*Map<String, String> queryStringMap = queryString2map(request.getQueryString());
				for (Entry<String, String> e:queryStringMap.entrySet()) {
					String[] temp = new String[1];
					temp[0] = enCode(e.getValue());
					map.put(e.getKey(), temp);
				}*/
				map = new HashMap<String, String[]>(tempMap);
				String queryString = request.getQueryString();
				if (!StringUtil.isStringEmpty(queryString)) {
					List<String> list = queryStringKey2List(queryString);
					for(String str:list){
						String[] temp = map.get(str);
						for (int i = 0;i < temp.length;i++) {
							temp[i] = enCode(temp[i]);
						}
						map.put(str, temp);
					}
				}
			}
		}
		

		@Override
		public Map<String, String[]> getParameterMap() {
			return map;
		}

		@Override
		public String getParameter(String name) {
			String[] temp = map.get(name);
			if (temp == null || temp.length < 1) {
				return null;
			}
			return temp[0];
		}

		private String enCode(String param){
			try {
				return StringUtil.getSystemCharacterSetString(param.getBytes(serverEncoding));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return null;
			}
		}

		@Override
		public String[] getParameterValues(String name) {
			return map.get(name);
		}
	}
	
	/**
	 * 把查询字符串存放到map中
	 * @param queryString
	 * @return
	 */
	private Map<String, String> queryString2map(String queryString){
		Map<String, String> map = new HashMap<String, String>();
		String[] split = queryString.split("&");
		for (String s:split) {
			String[] paramMap = s.split("=");
			map.put(paramMap[0], paramMap[1]);
		}
		return map;
	}
	/**
	 * 把查询字符串的key存放到list中
	 * @param queryString
	 * @return
	 */
	private List<String> queryStringKey2List(String queryString){
		List<String> list = new ArrayList<String>();
		String[] split = queryString.split("&");
		for (String s:split) {
			list.add(s.split("=")[0]);
		}
		return list;
	}
	
	
	/**
	 * 解决URL带中文参数时request.getParameter(name)乱码问题
	 * @author battle_steed
	 */
	/*private class RequestEncodingInterceptor implements MethodInterceptor{
		private Map<String, String[]> map;
		private HttpServletRequest req;
		private boolean isGet;
		public HttpServletRequest createProxy(ServletRequest target) {
	        this.req = (HttpServletRequest) target;
	        isGet = req.getMethod().equalsIgnoreCase("GET");
	        Enhancer enhancer = new Enhancer();
	        enhancer.setSuperclass(target.getClass());// 设置代理目标
	        enhancer.setCallback(this);
	        enhancer.setClassLoader(target.getClass().getClassLoader());
	        return (HttpServletRequest)enhancer.create();
	    }
		
		
		private String enCode(String param){
			try {
				return new String(param.getBytes(serverEncoding), characterSet);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				System.out.println("服务器或系统编码配置错误!!!!");
				throw new RuntimeException("服务器或系统编码配置错误!!!!");
			}
		}
		@Override
		public Object intercept(Object obj, Method method, Object[] args,
				MethodProxy methodProxy) throws Throwable {
			Object superReturned = methodProxy.invokeSuper(obj, args);
			if (!isGet) {
				return superReturned;
			}
			String methodName = method.getName();
			if ("getParameter".equals(methodName)) {
				String param = (String) superReturned;
				if (param != null) {
					param = enCode(param);
				}
				return param;
			}else if ("getParameterValues".equals(methodName)) {
				String[] params = (String[]) superReturned;
				String[] newParams = getValues(params);
				return newParams;
			}else if("getParameterMap".equals(methodName)){
				if (map == null) {
					map = new HashMap<String, String[]>();
					@SuppressWarnings("unchecked")
					Map<String, String[]> tempMap = (Map<String, String[]>) superReturned;
					for (Entry<String, String[]> e:tempMap.entrySet()) {
						map.put(e.getKey(), getValues(e.getValue()));
					}
				}
				return map;
			}
			return superReturned;
		}
		
		private String[] getValues(String[] params) {
			if (params == null) {
				return params;
			}
			String[] newParams = new String[params.length];
			for (int i = 0; i < params.length; i++) {
				newParams[i] = enCode(params[i]);
			}
			return newParams;
		}
	}*/

}
