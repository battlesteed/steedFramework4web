<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/include/head/tagLib.jsp" %>
<div class="pageHeader">
	<form id="programaForm" rel="pagerForm" onsubmit="return navTabSearch(this);" action="<st:getCurrentUrl lastUrl="index.act" />" method="post">
	<div class="searchBar">
		<table class="searchContent">
			<tr>
			<td></td>
				<td>
					栏目名:&nbsp;&nbsp;&nbsp;<input name="name" type="text" />
				</td>
			</tr>
		</table>
		<div class="subBar">
			<ul>
				<li><div class="buttonActive"><div class="buttonContent"><button type="submit">查询</button></div></div></li>
			</ul>
		</div>
	</div>
	</form>
</div>


<div class="pageContent">
	<div class="panelBar">
		<ul class="toolBar">
			<li><a class="add" rel="addPrograma" href="<st:getCurrentUrl lastUrl="add.act" />" rel="addMember" title="添加栏目" target="navTab"><span>添加</span></a></li>
			<li><a class="delete" href="<st:getCurrentUrl lastUrl="delete.act" />?name={sid_department}" target="ajaxTodo" title="确定要删除该部门吗?"><span>删除</span></a></li>
			<li><a class="edit" rel="editPrograma" href="<st:getCurrentUrl lastUrl="edit.act" />?name={sid_department}" title="修改栏目" target="navTab"><span>修改</span></a></li>
		</ul>
	</div>
	<div id="w_list_print_department">
	<table class="table" width="100%" layoutH="138">
		<thead>
			<tr>
				<th align="center">栏目名</th>
				<th align="center">描述</th>
			</tr>
		</thead>
		<tbody>
	      <c:forEach items="${page.domainCollection}" var="temp">
		      <tr target="sid_department" rel="<st:encodUrl url="${temp.name }" />">
			      <td> 
				      &nbsp;&nbsp;${temp.name}
				  </td>
			      <td> 
			          &nbsp;&nbsp;${temp.description }
			       </td>
	    	  </tr>
	    	</c:forEach>
		</tbody>
	</table>
	</div>
	<%@include file="/WEB-INF/jsp/include/plugin/page.jsp" %>
</div>
<st:FillInputByParam root="programaForm"/>

