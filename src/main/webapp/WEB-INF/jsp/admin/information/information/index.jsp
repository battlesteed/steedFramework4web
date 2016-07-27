<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/include/head/tagLib.jsp" %>
<div class="pageHeader">
	<form id="informationForm" rel="pagerForm" onsubmit="return navTabSearch(this);" action="<st:getCurrentUrl lastUrl="index.act" />" method="post">
	<div class="searchBar">
		<table class="searchContent">
			<tr>
			<td></td>
				<td>
					标题:&nbsp;&nbsp;&nbsp;<input name="title"  type="text" />
				</td>
				<td>
					作者:&nbsp;&nbsp;&nbsp;<input name="author.nickName"  type="text" />
				</td>
				<td>
					栏目:&nbsp;&nbsp;&nbsp;<select name="programa.name">
					<option value="">请选择</option>
				<c:forEach items="${requestScope.programaList }" var="temp">
					<option <c:if test="${param['programa.name'] eq temp }">selected="selected"</c:if> value="${temp }">
						${temp }
					</option>
				</c:forEach>
					</select>
			       <%-- 用户组: <s:select value="" list="%{#request.roleNameList }" name="roleSet" ></s:select> --%>
				</td>
				<td>
					开始日期:&nbsp;&nbsp;&nbsp;
					<input name="publishDate_min_1" type="text" class="date" readonly="true"/>
				</td>
				<td>
					结束日期:&nbsp;&nbsp;&nbsp;
					<input name="publishDate_max_1" type="text" class="date" readonly="true"/>
				</td>
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
			<li><a class="add" rel="addInformation" href="<st:getCurrentUrl lastUrl="add.act" />" rel="addInformation" title="添加文章" target="navTab"><span>添加</span></a></li>
			<li><a class="delete" href="<st:getCurrentUrl lastUrl="delete.act" />?id={sid_information}" target="ajaxTodo" title="确定要删除该文章吗?"><span>删除</span></a></li>
			<li><a class="edit" rel="editInformation" href="<st:getCurrentUrl lastUrl="edit.act" />?id={sid_information}" title="修改文章" target="navTab"><span>修改</span></a></li>
			<li><a class="edit" rel="lookOverInformation" external="true" href="<st:getCurrentUrl lastUrl="lookOver.act" />?id={sid_information}" title="查看文章" target="navTab"><span>查看</span></a></li>
		</ul>
	</div>
	<div id="w_list_print_member">
	<table class="table" width="100%" layoutH="138">
		<thead>
			<tr>
				<th align="center">标题</th>
				<!-- <th align="center">预览</th> -->
				<th align="center">栏目</th>
				<th align="center">日期</th>
				<th align="center">作者</th>
			</tr>
		</thead>
		<tbody>
      <c:forEach items="${page.domainCollection }" var="temp">
	      <tr target="sid_information" rel="<st:encodUrl url="${temp.id }" />">
		      <td> 
			      ${temp.title}
			  </td>
		      <td> 
			      ${temp.programa}
			  </td>
		     <%--  <td> 
			      ${requestScope.temp.content }
			  </td> --%>
		      <td> 
		      	<fmt:formatDate value="${temp.publishDate}" pattern="yyyy-MM-dd"/>
		       </td>
		      <td> 
			      ${temp.author }
		       </td>
	    	</tr>
    </c:forEach>
		</tbody>
	</table>
	</div>
	<%@ include file="/WEB-INF/jsp/include/plugin/page.jsp" %>
</div>
<st:FillInputByParam root="informationForm"/>

