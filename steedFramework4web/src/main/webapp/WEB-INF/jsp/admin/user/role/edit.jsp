<%@page import="steed.util.system.JspMethod"%>
<%@page import="steed.ext.domain.user.Power"%>
<%@page import="steed.ext.domain.user.Role"%>
<%@page import="steed.util.base.CollectionsUtil"%>
<%@page import="steed.ext.domain.system.Path_Power"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/include/head/tagLib.jsp" %>
<div class="pageContent">
<div class="pageFormContent" layoutH="60">
<form target="navTab" method="post" action="<st:getCurrentUrl lastUrl="update.act" />" class="pageForm required-validate" onsubmit="return iframeCallback(this);">
	<fieldset>
		<legend>修改角色</legend>
		<dl>
			<dt>角色名称:</dt>
			<dd><input readonly="true" value="${requestScope.domain.name }" id="name" class="required" name="name" type="text" /></dd>
		</dl>
		<dl>
			<dt></dt>
			<dd></dd>
		</dl>
	
	</fieldset>
<fieldset>
		<legend>其他信息</legend>
		<dl class="nowrap">
			<dt>描述：</dt>
			<dd><textarea name="description" cols="80" rows="2">${requestScope.domain.description }</textarea></dd>
		</dl>
		<dl class="nowrap">
			<dt>权限：</dt>
			<dd>
				
				<ul class="tree treeFolder treeCheck expand">
				<%=
					JspMethod.traversePath_Power((List<Path_Power>)application.getAttribute("Path_PowerList"),((Role)request.getAttribute("domain")).getPowerSet(),new StringBuffer(),"powerSet")					
				%>
				</ul>
			</dd>
		</dl>
	</fieldset>
	<div class="formBar">
			<ul>
				<li><div class="buttonActive"><div class="buttonContent"><button type="submit">提交</button></div></div></li>
				<li><div class="button"><div class="buttonContent"><button type="button" class="close">取消</button></div></div></li>
			</ul>
		</div>
</form>
</div>
</div>