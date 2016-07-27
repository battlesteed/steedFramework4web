<%@page import="steed.ext.domain.system.Menu"%>
<%@page import="steed.util.system.JspMethod"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/include/head/adminBase.jsp" %>
<%@ include file="/WEB-INF/jsp/include/head/ueditor.jsp" %> 
<meta http-equiv="X-UA-Compatible" content="IE=7" />
<script type="text/javascript">
$.extend($.validator.messages, {
	required: "必填字段",
	remote: "请修正该字段",
	email: "请输入正确格式的电子邮件",
	url: "请输入合法的网址",
	date: "请输入合法的日期",
	dateISO: "请输入合法的日期 (ISO).",
	number: "请输入合法的数字",
	digits: "只能输入整数",
	creditcard: "请输入合法的信用卡号",
	equalTo: "请再次输入相同的值",
	accept: "请输入拥有合法后缀名的字符串",
	maxlength: $.validator.format("长度最多是 {0} 的字符串"),
	minlength: $.validator.format("长度最少是 {0} 的字符串"),
	rangelength: $.validator.format("长度介于 {0} 和 {1} 之间的字符串"),
	range: $.validator.format("请输入一个介于 {0} 和 {1} 之间的值"),
	max: $.validator.format("请输入一个最大为 {0} 的值"),
	min: $.validator.format("请输入一个最小为 {0} 的值"),
	
	alphanumeric: "字母、数字、下划线",
	lettersonly: "必须是字母",
	phone: "请输入正确的号码"
});
</script>
<style type="text/css">
	#header{height:85px}
	#leftside, #container, #splitBar, #splitBarProxy{top:90px}
</style>
</head>
<body scroll="no">
	<div id="layout">
		<div id="header">
			<div class="headerNav">
				<a class="logo" href="<st:config key="companyUrl" />"><st:config key="companyName" /></a>
				<ul class="nav">
					<li><a href="javascript:navTab.openTab('userCenter','./admin/user/userCenter/index.act',{title:'我的资料'})">欢迎，${sessionScope.admin.nickName }</a></li>
					<li><a href="./admin/index.act">切换菜单</a></li>
					<li><a href="./admin/logout.act">退出</a></li>
				</ul>
				<ul class="themeList" id="themeList">
					<li theme="default"><div class="selected">蓝色</div></li>
					<li theme="green"><div>绿色</div></li>
					<!--<li theme="red"><div>红色</div></li>-->
					<li theme="purple"><div>紫色</div></li>
					<li theme="silver"><div>银色</div></li>
					<li theme="azure"><div>天蓝</div></li>
				</ul>
			</div>
	<div id="navMenu" class="navMenu">
				<ul>
				<c:forEach varStatus="status" var="temp" items="${sessionScope.menuList }">
				<li <c:if test="${status.index == 0 }">class="selected"</c:if> >
					<a href="./admin/menu.act?fatherId=${temp.id }"><span>${temp.name }</span>
				</a></li>
				</c:forEach>
				</ul>
			</div>
			<!-- navMenu -->
		</div>

		<div id="leftside">
			<div id="sidebar_s">
				<div class="collapse">
					<div class="toggleCollapse"><div></div></div>
				</div>
			</div>
			
			<div id="sidebar">
				<div class="toggleCollapse"><h2>主菜单</h2><div>收缩</div></div>

				<div class="accordion" fillSpace="sidebar">
					<%=
						JspMethod.outMenu(((List<Menu>)session.getAttribute("menuList")).get(0).getSonList())
					%>
					
				</div>
			</div>
		</div>
		
		<%@include file="/WEB-INF/jsp/public/adminMain.jsp" %>
		
		</div>
		<%@include file="/WEB-INF/jsp/public/adminFooter.jsp" %>
		
		
</body>
</html>