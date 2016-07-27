<%@page import="steed.domain.GlobalParam"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/include/head/tagLib.jsp" %>
<script type="text/javascript" src="./js/jQuery.md5.js"></script>
<script type="text/javascript">
    function editMember(form){
    	var password = $("#password");
    	$("#password3").val($.md5(password.val().trim()));
    	return iframeCallback(form);
    }
</script>






<div class="pageContent">
<div class="pageFormContent" layoutH="60">
<form target="navTab" method="post" action="<st:getCurrentUrl lastUrl="update.act" />" class="pageForm required-validate" onsubmit="return editMember(this);">
	<fieldset>
		<legend>基本信息</legend>
		<dl>
			<dt>ID:</dt>
			<dd><input readonly="true" value="${domain.nickName }" id="nickName" class="required" name="nickName" type="text" /></dd>
		</dl>
		<dl>
			<dt>姓名:</dt>
			<dd>
			<input value="${domain.name }" name="name" id="name" type="text"/>
			</dd>
		</dl>
		<dl>
			<dt>密码:</dt>
			<dd>
			<input id="password3" name="password" type="hidden" value=""/>
			<input minlength="6" maxlength="20" id="password" type="password" />&nbsp;&nbsp;(不修改请留空)</dd>
		</dl>
		<dl>
			<dt>确认密码:</dt>
			<dd>
			<input minlength="6" maxlength="20" equalto="#password" type="password" />
			</dd>
		</dl>
		<dl>
			<dt>性别:</dt>
			<dd>
			<input type="radio" name="sex" <c:if test="${domain.sex  == '男'}">checked="checked"</c:if> value="男"/>男
			<st:space spaceCount="3" />
			<input type="radio" name="sex" <c:if test="${domain.sex  == '女'}">checked="checked"</c:if> value="女"/>女
			<st:space spaceCount="3" />
			<input type="radio" name="sex" <c:if test="${domain.sex  == '保密'}">checked="checked"</c:if> value="保密"/>保密
			<st:space spaceCount="3" />
			</dd>
		</dl>
		<dl>
			<dt>邮箱:</dt>
			<dd>
			<input value="${domain.e_mail }" type="text" name="e_mail" class="email"/>
			</dd>
		</dl>
		<dl>
			<dt>联系号码:</dt>
			<dd>
			<input value="${domain.phoneNumber }" type="text" name="phoneNumber" class="phone"/>
			</dd>
		</dl>
	</fieldset>
	<fieldset>
		<legend>其他信息</legend>
		<dl class="nowrap">
			<dt>简介：</dt>
			<dd><textarea name="description" cols="80" rows="2">${domain.description }</textarea></dd>
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