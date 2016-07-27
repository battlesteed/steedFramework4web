<%@page import="steed.domain.GlobalParam"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/include/head/adminBase.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<style type="text/css">
.tableborder td {
	padding-left: 10px;
}
</style>
</head>
<body>
	<div style="display: block;" class="tab-page" id="baseinfo">
		<table class="tableborder" align="center" width="100%" cellpadding="3"
			cellspacing="1">
			<tbody>
				<tr class="header">
					<td height="25" width="16%">
						<div align="left">会员信息</div>
					</td>
					<td>
				</tr>
			</tbody>
		</table>
		<table class="tableborder" align="center" width="100%" cellpadding="3"
			cellspacing="1">
			<tbody>
				<tr>
					<td bgcolor="ffffff" height="25" width="16%">ID</td>
					<td bgcolor="ffffff" align="left">${member.nickName } 
					<input type="hidden" name="nickName" value="${member.nickName }" />
					</td>
				</tr>
				<tr>
					<td bgcolor="ffffff" height="25" width="16%">性别</td>
					<td bgcolor="ffffff">${member.sex }</td>
				</tr>
				<tr>
					<td bgcolor="ffffff" height="25" width="16%">名字</td>
					<td bgcolor="ffffff">${member.name }</td>
				</tr>
				<tr>
					<td bgcolor="ffffff" height="25" width="16%">邮箱</td>
					<td bgcolor="ffffff">${member.e_mail }</td>
				</tr>
				<tr>
					<td bgcolor="ffffff" height="25" width="16%">联系号码</td>
					<td bgcolor="ffffff">${member.phoneNumber }</td>
				</tr>
				<tr>
					<td bgcolor="ffffff" height="25" width="16%">描述</td>
					<td bgcolor="ffffff">${member.description }</td>
				</tr>
				<tr>
					<td bgcolor="ffffff" height="25" width="16%">用户组</td>
					<td bgcolor="ffffff">
						<s:iterator value="%{#request.member.roleSet}" var="temp" status="status">
							${temp.name }
					    <st:space spaceCount="3" />
						<c:if test="${status.count % 3 == 0 }">
							<br />
						</c:if>
						</s:iterator>
					</td>
				</tr>
				<tr style="background: #ffffff;">
					<td>地址：</td>
					<td style="height: 50px;">${requestScope.member.fullAddress }
					</td>
				</tr>
			</tbody>
		</table>
	</div>
</body>
</html>