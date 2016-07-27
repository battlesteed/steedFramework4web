<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/include/head/tagLib.jsp" %>
<div class="pageHeader">
	<form id="memberForm" rel="pagerForm" onsubmit="return navTabSearch(this);" action="<st:getCurrentUrl lastUrl="index.act" />" method="post">
	<div class="searchBar">
		<table class="searchContent">
			<tr>
			<td></td>
				<td>
					ID:&nbsp;&nbsp;&nbsp;<input name="nickName" type="text" />
				</td>
				<td>
					姓名:&nbsp;&nbsp;&nbsp;<input name="name" type="text" />
				</td>
				<td>
					用户组:&nbsp;&nbsp;&nbsp;
					<select name="roleSet">
					<option value="">请选择</option>
						<c:forEach items="${requestScope.roleNameList }" var="temp">
							<option value="${temp }">
							${temp }
							</option>
						</c:forEach>
					</select>
				</td>
				<%-- <td>
					部门:&nbsp;&nbsp;&nbsp;<select name="departmentSet">
					<option value="">请选择</option>
				<c:forEach items="${requestScope.departmentNameList }" var="temp">
					<option value="${temp }">
						${temp }
					</option>
				</c:forEach>
					</select>
				</td> --%>
			<!-- 	<td>
					账号类型:&nbsp;&nbsp;&nbsp;
					<select name="userType">
						<option value="">请选择</option>
						<option value="0">平台用户</option>
						<option value="1">商家</option>
						<option value="2">内容提供者</option>
					</select>
				</td> -->
			</tr>
		</table>
		<div class="subBar">
			<ul>
				<li><div class="buttonActive"><div class="buttonContent"><button type="submit">查询</button></div></div></li>
			</ul>
		</div>
	</div>
	<!--    <tr> 
     <td style="width: 80%;">
	<div style="text-align: left;padding-left: 50px;">
         ID:<st:space spaceCount="3" />
         <input class="easyui-textbox" data-options="validType:['IDLegal','IDLenth']" name="nickName" id="keyboard" value="<s:property value="%{nickName}"/>" size="14" type="text">
       <st:space spaceCount="3" />姓名:<st:space spaceCount="3" />
       <input class="easyui-textbox" name="name" value="<s:property value="%{name}"/>" size="14" type="text" />
	       <s:select list="%{#request.roleNameList }" name="roleSet" ></s:select>
        &nbsp;&nbsp;<input value="搜索" type="submit">
        </div></td>
        <td style="width: 20%;"> 
             <button onclick="window.location.href='./admin/user/member/add.act'" class="steedbutton" type="button">新增会员</button>
      	</td>
</tr> -->
	
	</form>
</div>
<div class="pageContent">
	<div class="panelBar">
		<ul class="toolBar">
			<li><a class="add" href="<st:getCurrentUrl lastUrl="add.act" />" rel="addMember" title="添加会员" target="navTab"><span>添加</span></a></li>
			<li><a class="delete" href="<st:getCurrentUrl lastUrl="delete.act" />?nickName={sid_member}" target="ajaxTodo" title="确定要删除吗?"><span>删除</span></a></li>
			<li><a class="edit" rel="editMember" href="<st:getCurrentUrl lastUrl="edit.act" />?nickName={sid_member}" title="修改会员资料" target="navTab"><span>修改</span></a></li>
<%-- 			<li><a class="edit" rel="editMemberPower" href="<st:getCurrentUrl lastUrl="editMemberPower.act" />?nickName={sid_member}" title="修改会员权限" target="navTab"><span>权限</span></a></li> --%>
		</ul>
	</div>
	<div id="w_list_print_member">
	<table class="table" width="100%" layoutH="138">
		<thead>
			<tr>
				<th align="center" width="20%">ID</th>
				<th align="center">姓名</th>
				<th align="center">用户组</th>
				<th align="center">描述</th>
			<!-- 	<th align="left">账号类型</th> -->
			</tr>
		</thead>
		<tbody>
		      <c:forEach items="${page.domainCollection}" var="temp">
			      <tr target="sid_member" rel='<st:encodUrl url="${temp.nickName }" />'>
				      <td> 
					      <%--  <a href="./admin/user/member/lookOver.act?nickName=${requestScope.temp.nickName}" target="navTab" rel="memberDetail" title="${requestScope.temp.nickName}的资料" fresh="false">
					      ${requestScope.temp.nickName} </a> --%>
					      ${temp.nickName}
					  </td>
				  
				      <td> 
				          ${temp.name }
				       </td>
			       
				      <td> 
				          <c:forEach items="${temp.roleSet}" var="temp2">
				          	${temp2.name }&nbsp;&nbsp;
				          </c:forEach>
				       </td>
			       
				      <td> 
				          ${requestScope.temp.description }
				       </td>
			    	</tr>
		    </c:forEach>
		</tbody>
	</table>
	</div>
	<%@include file="/WEB-INF/jsp/include/plugin/page.jsp"%>
	<st:FillInputByParam root="memberForm" />
</div>

