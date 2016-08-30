<%@page import="steed.domain.GlobalParam"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	if ("/".equals(path)) {
		path = "";
	}
	request.setAttribute("ctx", path);
	request.setAttribute("contextUrl", GlobalParam.FOLDER.contextUrl);
%>
<base href="<%=basePath%>">