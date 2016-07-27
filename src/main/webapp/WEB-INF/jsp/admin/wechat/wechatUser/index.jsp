<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/include/head/tagLib.jsp" %>
<div class="pageHeader">
	<form id="bonusRecordSearchForm" rel="pagerForm" onsubmit="return navTabSearch(this);" action="<st:getCurrentUrl lastUrl="index.act" />" method="post">
		<input type="hidden" name="ajax" value="1">
	<div class="searchBar">
		<table class="searchContent">
			<tr>
			<td></td>
				<td>
					openID:&nbsp;&nbsp;&nbsp;<input name="openid" type="text" />
				</td>
				<td>
					昵称:&nbsp;&nbsp;&nbsp;<input name="nickname" type="text" />
				</td>
				<td>
					<span style="float: left;line-height: 200%;">
					性别:&nbsp;&nbsp;&nbsp;
					</span>
					<select name="sex">
						<option value="">请选择</option>
						<option value="0">未知</option>
						<option value="1">男</option>
						<option value="2">女</option>
					</select>
				</td>
				<%-- <td>
					<span style="float: left;line-height: 200%;">
					用户组:&nbsp;&nbsp;&nbsp;
					</span>
					<select name="wechatUserGroup.groupName">
						<option value="">请选择</option>
						<c:forEach var="temp" items="${WechatUserGroupList }">
							<option value="${temp }">${temp }</option>
						</c:forEach>
					</select>
				</td> --%>
			</tr>
		</table>
		<div class="subBar">
			<ul>
				<li><div class="buttonActive"><div class="buttonContent"><button type="submit">检索</button></div></div></li>
			</ul>
		</div>
	</div>
	</form>
</div>


<div class="pageContent">
	<div class="panelBar">
		<ul class="toolBar">
			<%-- <li><a class="edit" href="<st:getCurrentUrl lastUrl="edit.act" />?openid={sid_wechatUser}&dialog=1" heigth="500" width="800" rel="editWechatUser" title="编辑微信用户" target="dialog"><span>编辑</span></a></li>
		 --%></ul>
	</div>
	<div id="w_list_print_member">
	<table class="table" width="100%" layoutH="138">
		<thead>
			<tr>
				<th align="center">头像</th>
				<th align="center">openid</th>
				<th align="center">昵称</th>
				<th align="center">性别</th>
				<th align="center">国家</th>
				<th align="center">省份</th>
				<th align="center">城市</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach varStatus="status" items="${page.domainCollection}" var="temp">
		      <tr target="sid_wechatUser" rel="<st:encodUrl url="${temp.openid }" />">
			      <td> 
			      		<c:if test="${temp.headimgurl == '' || temp.headimgurl == null }">
			      		 	 <img width="20px;" src="./images/wechat/defauHead.jpg"/>
			      		 </c:if>
			      		<c:if test="${temp.headimgurl != '' && temp.headimgurl != null }"> 
			      		 	<img width="20px;" src="${temp.headimgurl }"/>
			      		 </c:if>
				  </td>
			      <td> 
				      ${temp.openid }
				  </td>
			      <td> 
				      ${temp.nickname }
				  </td>
				  <td>
			  		<c:if test="${temp.sex == 0 }">未知</c:if>
			  		<c:if test="${temp.sex == 1 }">男</c:if>
			  		<c:if test="${temp.sex == 2 }">女</c:if>
				  </td>
				  	<td>${temp.country }</td>
				  	<td>${temp.province }</td>
				  	<td>${temp.city }</td>
				  	<%-- <td>${temp.wechatUserGroup }</td> --%>
		    	</tr>
		    </c:forEach>
		</tbody>
	</table>
	</div>
	<%@ include file="/WEB-INF/jsp/include/plugin/page.jsp" %>
	 <st:FillInputByParam />
</div>
