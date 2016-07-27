<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/include/head/tagLib.jsp" %>
<script type="text/javascript">
	function gotoPage(aNode,currentPage){
		var temp = $(aNode).parents("[class*=dataTables_wrapper]");
		var form = $("form[rel='searchForm']",temp);
		$("input[name='currentPage']",form).val(currentPage);
		form.submit();
	}
	function changePageSize(selectNode){
		var temp = $(selectNode).parents("[class*=dataTables_wrapper]");
		var form = $("form[rel='searchForm']",temp);
		$("input[name='numPerPage']",form).val(selectNode.value);
		form.submit();
	}
</script>
<div class="row">
    <div class="col-xs-6">
        <div class="dataTables_info">
            	每页显示<select class="form-control input-sm" onchange="changePageSize(this);" name="numPerPage">
            				
                          <option <c:if test="${param.numPerPage == 15 }">selected="selected"</c:if> value="10">
                              15
                          </option>
                          <option <c:if test="${param.numPerPage == 25 }">selected="selected"</c:if> value="25">
                              25
                          </option>
                          <option <c:if test="${param.numPerPage == 50 }">selected="selected"</c:if> value="50">
                              50
                          </option>
                          <option <c:if test="${param.numPerPage == 100 }">selected="selected"</c:if> value="100">
                              100
                          </option>
                      </select>
            	
            	条，
            	共${requestScope.page.recordCount }条数据
        </div>
    </div>
    <div class="col-xs-6">
        <div id="sample-table-2_paginate" class="dataTables_paginate paging_simple_numbers">
            <ul class="pagination">
                <li class="paginate_button previous">
                    <a onclick="gotoPage(this,1);" href="javascript:;">
                        	首页
                    </a>
                </li>
                <c:forEach varStatus="status" step="1" begin="${requestScope.page.currentPage - 5 > 0 ? requestScope.page.currentPage - 5:1 }"
                	 end="${requestScope.page.currentPage + 5 > requestScope.page.pageInAll ?  requestScope.page.pageInAll:requestScope.page.currentPage + 5}">
                	<c:if test='${status.count != requestScope.page.currentPage }'> 
	                	<li class="paginate_button">
	                   		 <a href="javascript:;" onclick="gotoPage(this,${status.count });">
	                        	${status.count }
	                   		 </a>
	                	</li>
                	</c:if>
                	<c:if test='${status.count == requestScope.page.currentPage }'> 
	                	<li class="paginate_button active">
	                   		 <a>
	                        	${status.count }
	                   		 </a>
	                	</li>
                	</c:if>
                </c:forEach>
                <li class="paginate_button next">
                    <a onclick="gotoPage(this,${requestScope.page.pageInAll });" href="javascript:;">
                       	尾页
                    </a>
                </li>
            </ul>
        </div>
    </div>
</div>
</form>
</div>