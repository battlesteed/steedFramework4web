<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/include/head/tagLib.jsp" %>
<div class="pageContent">
<div class="pageFormContent" layoutH="60">
<form target="navTab" method="post" action="<st:getCurrentUrl lastUrl="update.act" />" class="pageForm required-validate" onsubmit="return iframeCallback(this);">
	<fieldset>
		<legend>基本信息</legend>
		<dl>
			<dt>标题:</dt>
			<dd>
			<input name="id" value="${requestScope.domain.id }" type="hidden" />
			<input value="${requestScope.domain.title }" class="required" name="title" type="text" /></dd>
		</dl>
		<dl>
			<dt>所属栏目:</dt>
			<dd>
			<select class="required" name="programa.name">
				<option value="">请选择</option>
				<c:forEach items="${requestScope.programaList }" var="temp">
					<option <c:if test="${requestScope.domain.programa.name eq temp}">selected="selected" </c:if> value="${pageScope.temp }">${pageScope.temp }</option>
				</c:forEach>
				</select>
			</dd>
		</dl>
	</fieldset>
	<fieldset>
		<legend>其他信息</legend>
		<dl class="nowrap">
			<dt>正文：</dt>
			<dd>
			<textarea style="width: 122%;height: 220px;" id="${requestScope.ueditorID }content" name="content" >${requestScope.domain.content }</textarea>
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
 <script type="text/javascript">
 document.getElementById("${requestScope.ueditorID }content").value = utf8to16(base64decode(document.getElementById("${requestScope.ueditorID }content").value));
 UE.getEditor('${requestScope.ueditorID }content');
    </script>
