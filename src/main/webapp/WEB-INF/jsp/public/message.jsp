<%@page import="steed.util.dao.DaoUtil"%>
<%@page import="steed.exception.MessageExceptionInterface"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/include/head/tagLib.jsp" %>
<%@page import="steed.util.base.BaseUtil"%>
<%@page import="steed.domain.application.DWZMessage"%>
<%@page import="steed.exception.MessageException"%>
<%
Exception e = (Exception)request.getAttribute("exception");
e.printStackTrace();
try{
	DaoUtil.rollbackTransaction();
}catch(NullPointerException ee){
	
}
%>
<c:if test="${param.ajax != null && param.ajax != '' }">
	<%
		String messageJson;
		Exception obj = (Exception)request.getAttribute("exception");
		DWZMessage dwzMessage = new DWZMessage();
		dwzMessage.setStatusCode(300);
		dwzMessage.setTitle("出错啦！");
		if(obj instanceof MessageExceptionInterface){
			dwzMessage.setMessage(((Exception)obj).getMessage());
		}else{
			dwzMessage.setMessage("系统繁忙！");
		}
		messageJson = BaseUtil.getJson(dwzMessage);
	%>
	<%=messageJson %>
</c:if>
<c:if test="${param.ajax == null || param.ajax == '' }">
<%@include file="/WEB-INF/jsp/include/head/base.jsp" %>
<title>出错啦</title>
</head>
<body style="background-color: #E5E9F2;">
  <div style="text-align: center;height: 40%;width: 100%;margin-top: 10%;font-size: 14px;font-weight: bolder;">
    	${requestScope.exception.message }
    	<br /><br />
    	<script>
	    	setTimeout(function() {
	    	<c:if test="${requestScope.exception.msg.url != null }">
					window.location.href = "${requestScope.exception.msg.url}";
	    	</c:if>
	    	<c:if test="${requestScope.exception.msg.url == null && param.dialog == null }">
    			navTab.closeCurrentTab();
    		</c:if>
	    	<c:if test="${param.dialog != null }">
	    		$.pdialog.closeCurrent();
    		</c:if>
			}, 2000);
    	</script>
  </div>
  </body>
  </html>
</c:if>

