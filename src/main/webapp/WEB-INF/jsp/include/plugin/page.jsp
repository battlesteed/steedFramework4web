<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/include/head/tagLib.jsp" %>
	<form id="pagerForm" method="post" action='#rel#'>
		<input type="hidden" name="pageNum" value="1" />
		<input type="hidden" name="numPerPage" value="${requestScope.page.pageSize }" />
	</form>
	<div class="panelBar">
		<div class="pages">
			<span>每页显示</span>
			<select name="numPerPage" onchange="navTabPageBreak({numPerPage:this.value})">
				<option <c:if test="${requestScope.page.pageSize == 10 }">selected="selected"</c:if> value="10">10</option>
				<option <c:if test="${requestScope.page.pageSize == 20 }">selected="selected"</c:if> value="20">20</option>
				<option <c:if test="${requestScope.page.pageSize == 50 }">selected="selected"</c:if> value="50">50</option>
				<option <c:if test="${requestScope.page.pageSize == 100 }">selected="selected"</c:if> value="100">100</option>
			</select>
			<span>条，共${requestScope.page.recordCount }条</span>
		</div>
		<div class="pagination" totalCount="${requestScope.page.recordCount }" numPerPage="${requestScope.page.pageSize }" pageNumShown="10" currentPage="${requestScope.page.currentPage }"></div>
	</div>