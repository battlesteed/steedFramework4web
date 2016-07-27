<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/include/head/base.jsp"%>
<title><st:config key="site.name"></st:config>登陆</title>
<link href="${ctx}/images/login/style/login.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="${ctx}/dwz/js/jquery-1.7.2.min.js"></script>
<script src="${ctx}/js/jQuery.md5.js" type="text/javascript"></script>
<script type="text/javascript">
	function submitLogin() {
		$("#btndiv").remove();
		$("#loging").css("display", "");
		setTimeout(function() {
			document.getElementById("loginForm").submit();
		}, 2000);
	}

	var clickable = true;
	function login() {
		var nickName = $("#nickName").val();
		if (isObjNull(nickName)) {
			alert("用户名不能为空！！");
			return false;
		}
		
		var pwd = $("#password").val();
		if (isObjNull(pwd)) {
			alert("密码不能为空！！");
			return false;
		}
		var password = $.md5(pwd);
		/* var securityCode = $("#securityCode").val();
		if (isObjNull(securityCode)) {
			alert("验证码不能为空！！");
			return false;
		} */
		$("#btndiv").hide();
		$("#loging").show();
		$.ajax({
			type : "post",
			url : "${ctx}/<st:config key='site.adminPostLoginPath' />",
			dataType : 'json',
			data : {
				"password" : password,
				"nickName" : nickName
			},
			success : function(json) {
				if (json.statusCode == 200) {
					window.location.href = "${ctx}/admin/index.act";
				} else {
					alert(json.message);
					$("#btndiv").show();
					$("#loging").hide();
					//$("#securityCodeImg").attr("src","./securityCode.jpg?" + Math.random());
				}
			}
		});
		return false;
	}
</script>
</head>
<body>
	<div class="full-bg">
		<div class="full_pic"></div>
		<div class="login">
			<img src="${ctx}/images/login/images/y.jpg" class="y"> 
			<img
				src="${ctx}/images/login/images/logo.png" class="logo" >
			<div class="company"><st:config key="companyName"></st:config></div>
			<div class="menu">
				<form onsubmit="return login();" id="loginForm">
					<div>
						<input type="text" name="nickName" id="nickName" />
						<span><img src="${ctx}/images/login/images/name.jpg"></span>
					</div>
					<div>
						<input type="password" name="password" id="password" />
						<span><img src="${ctx}/images/login/images/mima.jpg"></span>
					</div>
					<%-- <div>
						<input id="securityCode" name="securityCode" class="code" type="text" size="5" />
						<span>
						 <img id="securityCodeImg" title="点击刷新" style="cursor: pointer;text-align:left;width: 80px;height: 26px;position: relative;right:20px;" alt="点击刷新"
							onclick="refreshValidateCode(this);" src='<st:url url="验证码" />' alt="" width="80" height="26" />
					     </span>
					</div> --%>
					<div class="btn" id="btndiv">
						<input class="sub" type="submit" value="登录" style="width: 100%;color: white;font-family:'微软雅黑';font-size: 16px;font-weight: bolder;" />
					</div>
					<center style="font-size: 13px; color: #2FA7E4; display: none; margin-top: 5px;" id="loging">
						<img alt="正在登录..." src="${ctx}/images/login/images/loading.gif" />
						正在登录，请稍后...
					</center>
				</form>
			</div>
			<!-- <div class="jzmm"><a href="#"></a><span>记住密码</span></div> -->
		</div>
	</div>
</body>
</html>