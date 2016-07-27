<%@page import="steed.exception.MessageExceptionInterface"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/include/head/tagLib.jsp" %>
<div id="container">
			<div id="navTab" class="tabsPage">
				<div class="tabsPageHeader">
					<div class="tabsPageHeaderContent"><!-- 显示左右控制时添加 class="tabsPageHeaderMargin" -->
						<ul class="navTab-tab">
							<li tabid="main" class="main"><a href="javascript:;"><span><span class="home_icon">我的主页</span></span></a></li>
						</ul>
					</div>
					<div class="tabsLeft">left</div><!-- 禁用只需要添加一个样式 class="tabsLeft tabsLeftDisabled" -->
					<div class="tabsRight">right</div><!-- 禁用只需要添加一个样式 class="tabsRight tabsRightDisabled" -->
					<div class="tabsMore">more</div>
				</div>
				<ul class="tabsMoreList">
					<li><a href="javascript:;">我的主页</a></li>
				</ul>
				<div class="navTab-panel tabsPageContent layoutBox">
					<div class="page unitBox">
						<div class="accountInfo">
							<div class="alertInfo">
								<h2><a href="#" target="_blank"><st:config key="companyName" /></a></h2>
								<a href="<st:config key="companyUrl" />" target="_blank"><st:config key="companyName" /></a>
							</div>
							<div class="right">
								<p><a href="<st:config key="companyUrl" />" target="_blank" style="line-height:19px"><st:config key="companyName" /></a></p>
								<p><a href="<st:config key="companyUrl" />" target="_blank" style="line-height:19px">联系我们</a></p>
							</div>
							<p><span><st:config key="companyName" />平台框架</span></p>
							<p>官方网址:<a href="<st:config key="companyUrl" />" target="_blank"><st:config key="companyUrl" /></a></p>
						</div>
						<div class="pageFormContent" layoutH="80">
							<iframe width="100%" height="100%" class="share_self"  frameborder="0" scrolling="auto" src=""></iframe>
						</div>
					</div>
					
				</div>
			</div>
		</div>

