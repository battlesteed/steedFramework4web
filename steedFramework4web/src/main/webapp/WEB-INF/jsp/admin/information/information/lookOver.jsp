<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/include/head/base.jsp" %>
<%@ include file="/WEB-INF/jsp/include/head/tagLib.jsp" %>
<%@ include file="/WEB-INF/jsp/include/head/ueditor.jsp" %> 
<style type="text/css">
        .title{
            font-size: 20px;
            text-align: center;
            width: 100%;
        }
        .uEditorcontent{
        	padding-left: 50px;
        }
</style>
    </head>
    <body>
<div style="display: block;padding-left: 20px;margin-top: 30px;height: auto;">        
		<div class="title">${requestScope.domain.title }</div>
	<div id="uEditorcontent" class="uEditorcontent">
		${requestScope.domain.content }
	</div>
</div>
<script type="text/javascript">

document.getElementById('uEditorcontent').innerHTML = utf8to16(base64decode(document.getElementById('uEditorcontent').innerHTML));

uParse('.uEditorcontent', {
    rootPath: './plugin/ueditor/'
});
</script>
    </body>
    </html>