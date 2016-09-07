<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/include/head/tagLib.jsp" %>
<script type="text/javascript" src="./js/jQuery.md5.js"></script>
<script type="text/javascript">
    function addMember(form){
    	var password = $("#password");
    	$("#password3").val($.md5(password.val().trim()));
    	return iframeCallback(form);
    }
</script>
<div class="pageContent">
<div class="pageFormContent" layoutH="60">
<form target="navTab" method="post" action="<st:getCurrentUrl lastUrl="save.act" />" class="pageForm required-validate" onsubmit="return addMember(this);">
	<fieldset>
		<legend>基本信息</legend>
		<dl>
			<dt>ID:</dt>
			<dd><input id="nickName" class="required" name="nickName" type="text" /></dd>
		</dl>
		<dl>
			<dt>姓名:</dt>
			<dd>
			<input name="name" id="name" type="text"/>
			</dd>
		</dl>
		<dl>
			<dt>密码:</dt>
			<dd>
			<input id="password3" name="password" type="hidden" value=""/>
			<input minlength="6" maxlength="20" id="password" class="required" type="password" /></dd>
		</dl>
		<dl>
			<dt>确认密码:</dt>
			<dd>
			<input minlength="6" maxlength="20" class="required" equalto="#password" type="password" />
			</dd>
		</dl>
		<dl>
			<dt>性别:</dt>
			<dd>
			<input type="radio" name="sex" value="男"/>男
			<st:space spaceCount="3"></st:space>
			<input type="radio" name="sex" value="女"/>女
			<st:space spaceCount="3"></st:space>
			<input type="radio" name="sex" value="保密" checked="checked"/>保密
			<st:space spaceCount="3"></st:space>
			</dd>
		</dl>
		<dl>
			<dt>邮箱:</dt>
			<dd>
			<input type="text" name="e_mail" class="email"/>
			</dd>
		</dl>
		<dl>
			<dt>联系号码:</dt>
			<dd>
			<input type="text" name="phoneNumber" class="phone"/>
			</dd>
		</dl>
	<!-- 	<dl>
			<dt>账号类型:</dt>
			<dd>
				<select name="userType" class="required combox">
					<option value="">请选择</option>
					<option value="0">平台用户</option>
					<option value="1">商家</option>
					<option value="2">内容提供者</option>
				</select>
			</dd>
		</dl> -->
	</fieldset>
	<fieldset>
		<legend>其他信息</legend>
		<dl class="nowrap">
			<dt>简介：</dt>
			<dd><textarea name="description" cols="80" rows="2"></textarea></dd>
		</dl>
		<dl class="nowrap">
			<dt>用户组：</dt>
			<dd>
				<c:forEach items="${roleNameList }" var="temp" varStatus="status">
				<input type="checkbox" name="roleSet" value="${temp }"/>
				${temp } &nbsp;&nbsp;&nbsp;
				<c:if test="${status.count % 3 == 0 }">
					<br />
				</c:if>
				</c:forEach>
			</dd>
		</dl>
	<%-- 	<dl class="nowrap">
			<dt>部门：</dt>
			<dd>
				<s:iterator value="%{#request.departmentNameList }" var="temp" status="status">
				<input type="checkbox" name="departmentSet" value="${temp }"/>
				${temp } <st:space spaceCount="3" />
				<c:if test="${status.count % 3 == 0 }">
					<br />
				</c:if>
			</s:iterator>
			</dd>
		</dl> --%>
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