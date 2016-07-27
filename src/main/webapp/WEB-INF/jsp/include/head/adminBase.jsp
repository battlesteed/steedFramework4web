<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/include/head/base.jsp" %>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><st:config key="site.name"></st:config>管理后台</title>
<!-- dwz开始 -->
<link href="./dwz/themes/default/style.css" rel="stylesheet" type="text/css" media="screen"/>
<link href="./dwz/themes/css/core.css" rel="stylesheet" type="text/css" media="screen"/>
<link href="./dwz/themes/css/print.css" rel="stylesheet" type="text/css" media="print"/>
<link href="./dwz/uploadify/css/uploadify.css" rel="stylesheet" type="text/css" media="screen"/>
<!--[if IE]>
<link href="./dwz/themes/css/ieHack.css" rel="stylesheet" type="text/css" media="screen"/>
<![endif]-->

<!--[if lte IE 9]>
<script src="./dwz/js/speedup.js" type="text/javascript"></script>
<![endif]-->
<script src="./dwz/js/jquery-1.7.2.js" type="text/javascript"></script>
<script src="./dwz/js/jquery.cookie.js" type="text/javascript"></script>
<script src="./dwz/js/jquery.validate.js" type="text/javascript"></script>
<script src="./dwz/js/jquery.bgiframe.js" type="text/javascript"></script>
<script src="./dwz/xheditor/xheditor-1.2.1.min.js" type="text/javascript"></script>
<script src="./dwz/xheditor/xheditor_lang/zh-cn.js" type="text/javascript"></script>
<script src="./dwz/uploadify/scripts/jquery.uploadify.js" type="text/javascript"></script>


<!-- 可以用dwz.min.js替换前面全部dwz.*.js (注意：替换是下面dwz.regional.zh.js还需要引入)
<script src="bin/dwz.min.js" type="text/javascript"></script>
-->
<script src="./dwz/bin/dwz.min.js" type="text/javascript"></script>
<script src="./dwz/js/dwz.regional.zh.js" type="text/javascript"></script>

<script type="text/javascript">
$(function(){
	DWZ.init("./dwz/dwz.frag.xml", {
		loginUrl:"login_dialog.html", loginTitle:"登录",	// 弹出登录对话框
//		loginUrl:"login.html",	// 跳到登录页面
		statusCode:{ok:200, error:300, timeout:301}, //【可选】
		pageInfo:{pageNum:"pageNum", numPerPage:"numPerPage", orderField:"orderField", orderDirection:"orderDirection"}, //【可选】
		keys: {statusCode:"statusCode", message:"message"}, //【可选】
		ui:{hideMode:'display'}, //【可选】hideMode:navTab组件切换的隐藏方式，支持的值有’display’，’offsets’负数偏移位置的值，默认值为’display’
		debug:false,	// 调试模式 【true|false】
		callback:function(){
			initEnv();
			$("#themeList").theme({themeBase:"./dwz/themes"}); // themeBase 相对于index页面的主题base路径
		}
	});
});
</script>
<!-- dwz结束 -->
<!-- ztree开始 -->
<style type="text/css">
.ztree li span.button.add {margin-left:2px; margin-right: -1px; background-position:-144px 0; vertical-align:top; *vertical-align:middle}
	</style>
	<%@include file="/WEB-INF/jsp/include/head/ztreeCheckBox.jsp" %>
<!-- ztree结束 -->

<!-- 通用js开始 -->
	<script type="text/javascript" src="./js/jquery.json-2.4.min.js"></script>
<!-- 通用js结束 -->
	
 <script type="text/javascript">
	function iframeCallback(form,callback){
		var $form = $(form), $iframe = $("#callbackframe");
		if(!$form.valid()) {return false;}

		if ($iframe.size() == 0) {
			$iframe = $("<iframe id='callbackframe' name='callbackframe' src='about:blank' style='display:none'></iframe>").appendTo("body");
		}
		if(!form.ajax) {
			$form.append('<input type="hidden" name="ajax" value="1" />');
		}
		form.target = "callbackframe";
		
		_iframeResponse($iframe[0], callback || navTabAjaxDone);
	}
	function validateCallback(form, callback, confirmMsg) {
		var $form = $(form);

		if (!$form.valid()) {
			return false;
		}
		
		var _submitFn = function(){
			$.ajax({
				type: form.method || 'POST',
				url:$form.attr("action"),
				data:$form.serializeArray(),
				dataType:"json",
				cache: false,
				success: callback || navTabAjaxDone,
				error: DWZ.ajaxError
			});
		}
		
		if (confirmMsg) {
			alertMsg.confirm(confirmMsg, {okCall: _submitFn});
		} else {
			_submitFn();
		}
		
		return false;
	}
	
	</script> 
	