<%@page import="steed.ext.domain.system.Menu"%>
<%@page import="steed.util.system.JspMethod"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/include/head/base.jsp" %>
<div class="accordion" fillSpace="sideBar">
<%=
		JspMethod.outMenu(((Menu)request.getAttribute("sonMenuList")).getSonList())
%>
</div>