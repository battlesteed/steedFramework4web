<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/include/head/tagLib.jsp" %>
<div class="pageContent">
<div class="pageFormContent" layoutH="60">
<form target="navTab" method="post" action="<st:getCurrentUrl lastUrl="save.act" />" class="pageForm required-validate" onsubmit="return iframeCallback(this);">
	<fieldset>
		<legend>基本信息</legend>
		<dl>
			<dt>栏目名:</dt>
			<dd><input class="required" name="name" type="text" /></dd>
		</dl>
	</fieldset>
	<fieldset>
		<legend>其他信息</legend>
		<dl class="nowrap">
			<dt>描述：</dt>
			<dd><textarea name="description" cols="80" rows="2"></textarea></dd>
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