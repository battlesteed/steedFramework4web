<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/include/head/tagLib.jsp" %>
<div class="pageContent">
<div class="pageFormContent" layoutH="60">
<form enctype="multipart/form-data"
	 method="post" 
 action="<st:getCurrentUrl lastUrl="update.act" />" class="pageForm required-validate" onsubmit="return iframeCallback(this);">
	<fieldset>
		<legend>基本参数(不改请不要填)</legend>
		<dl>
			<dt>AppID:</dt>
			<dd><input class="required" name="wechat.appID" type="text" value='<st:config key="wechat.appID" />' /></dd>
		</dl>
		<dl>
			<dt>AppSecret:</dt>
			<dd><input name="wechat.appSecret" type="text" value='' /></dd>
		</dl>
		<dl>
			<dt>Token:</dt>
			<dd><input name="wechat.token" type="text" value='' /></dd>
		</dl>
		<dl>
			<dt>EncodingAESKey:</dt>
			<dd><input name="wechat.encodingAESKey" type="text" value='' /></dd>
		</dl>
	</fieldset>
	<fieldset id="dd">
		<legend>微信支付参数(不改请不要填)</legend>
		<dl>
			<dt>商户号:</dt>
			<dd><input name="wechat.merchant.id" type="text" value='<st:config key="wechat.merchant.id" />' /></dd>
		</dl>
		<dl>
			<dt>商户key:</dt>
			<dd><input name="wechat.merchant.key" type="text" value='' /></dd>
		</dl>
		<dl>
			<dt>证书:</dt>
			<dd><input name="certFile" type="file" /></dd>
		</dl>
		<!-- <dl>
			<dt>发放方式:</dt>
			<dd>
				人工<input value="false" name="wechat.merchant.redPacketAutoSend" type="radio" />
				自动<input value="true" name="wechat.merchant.redPacketAutoSend" type="radio" />
			</dd>
		</dl> -->
	</fieldset>

	<div class="formBar">
			<ul>
				<!-- <li><div class="buttonActive"><div class="buttonContent"><button onclick="test();" type="button">ooo</button></div></div></li> -->
				<li><div class="buttonActive"><div class="buttonContent"><button type="submit">提交</button></div></div></li>
				<li><div class="button"><div class="buttonContent"><button type="button" class="close">取消</button></div></div></li>
			</ul>
	</div>
</form>
</div>
</div>
