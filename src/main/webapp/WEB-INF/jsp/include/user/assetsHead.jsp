<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
		<!-- bootstrap & fontawesome -->
		<link rel="stylesheet" href="./ace/assets/css/bootstrap.css" />
		<link rel="stylesheet" href="./ace/assets/css/font-awesome.css" />
<!-- page specific plugin styles -->
		<link rel="stylesheet" href="./ace/assets/css/jquery-ui.css" />
		<!-- page specific plugin styles -->
<script src="./dwz/bin/dwz.min.js" type="text/javascript"></script>
<script src="./dwz/js/dwz.regional.zh.js" type="text/javascript"></script>
		<!-- text fonts -->
		<link rel="stylesheet" href="./ace/assets/css/ace-fonts.css" />
		<link rel="stylesheet" href="./ace/assets/css/jquery.gritter.css" />

		<!-- ace styles -->
		<link rel="stylesheet" href="./ace/assets/css/ace.css" class="ace-main-stylesheet" id="main-ace-style" />

		<!--[if lte IE 9]>
			<link rel="stylesheet" href="./ace/assets/css/ace-part2.css" class="ace-main-stylesheet" />
		<![endif]-->

		<!--[if lte IE 9]>
		  <link rel="stylesheet" href="./ace/assets/css/ace-ie.css" />
		<![endif]-->

		<!-- inline styles related to this page -->

		<!-- ace settings handler -->
		<script src="./ace/assets/js/ace-extra.js"></script>

		<!-- HTML5shiv and Respond.js for IE8 to support HTML5 elements and media queries -->

		<!--[if lte IE 8]>
		<script src="./ace/assets/js/html5shiv.js"></script>
		<script src="./ace/assets/js/respond.js"></script>
		<![endif]-->
		
		<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->

		<!--[if lt IE 9]>
		<script src="./ace/assets/js/html5shiv.js"></script>
		<script src="./ace/assets/js/respond.js"></script>
		<![endif]-->
		<!-- 输入验证 -->
	<!-- 	<script src="./ace/assets/js/jquery.inputlimiter.1.3.1.js"></script> -->
		<script src="./ace/assets/js/jquery.validate.js"></script>
		<script src="./ace/assets/js/additional-methods.js"></script>
		<script type="text/javascript" src="./js/steed/steed.js"></script>
		<script type="text/javascript" src="./js/beyondstar.js"></script>
		
		<script type="text/javascript">
			function isWechatSeted(url){
				if(${isWechatSeted}){
					window.location.href = url;
				}else{
					alert("请先设置当前公众账号！！");
				}
			}
		</script>