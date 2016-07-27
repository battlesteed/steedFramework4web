<%@page import="steed.util.system.JspMethod"%>
<%@page import="steed.ext.domain.user.Power"%>
<%@page import="steed.ext.domain.user.Role"%>
<%@page import="steed.util.base.CollectionsUtil"%>
<%@page import="steed.ext.domain.system.Path_Power"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/include/head/tagLib.jsp" %>
<script type="text/javascript">
<!--
function updateWechatUserAjaxDone(json){
	DWZ.ajaxDone(json);
	if(json.statusCode == 200){
		$.pdialog.closeCurrent();
		navTabPageBreak();
	}
}
//-->
</script>
<div id="editWechatUserPage" class="pageContent">
<div class="pageFormContent" layoutH="60">
<form method="post" action="<st:getCurrentUrl lastUrl="update.act" />" class="pageForm required-validate" onsubmit="return validateCallback(this,updateWechatUserAjaxDone);">
	<fieldset>
		<legend>编辑微信用户</legend>
		<dl>
			<dt>openid:</dt>
			<dd style="width: 50%;"><input style="width: 100%;" readonly="readonly" class="required" name="openid" type="text" /></dd>
		</dl>
		<dl class="nowrap">
			<dt>用户组:</dt>
			<dd>
			<select name="wechatUserGroup.groupName">
						<option value="">请选择</option>
						<c:forEach var="temp" items="${WechatUserGroupList }">
							<option value="${temp }">${temp }</option>
						</c:forEach>
					</select>
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
<st:FillInputByDomain root="editWechatUserPage"/>