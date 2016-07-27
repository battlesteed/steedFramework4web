<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>


<!-- #section:basics/sidebar -->
			<div id="sidebar" class="sidebar responsive">
				<script type="text/javascript">
					try{ace.settings.check('sidebar' , 'fixed')}catch(e){}
				</script>

					<!-- /菜单顶部图标 -->
				<div class="sidebar-shortcuts" id="sidebar-shortcuts">
					<div class="sidebar-shortcuts-large" id="sidebar-shortcuts-large">
						<button class="btn btn-success">
							<i class="ace-icon fa fa-signal"></i>
						</button>

						<button class="btn btn-info">
							<i class="ace-icon fa fa-pencil"></i>
						</button>

						<!-- #section:basics/sidebar.layout.shortcuts -->
						<button class="btn btn-warning">
							<i class="ace-icon fa fa-users"></i>
						</button>

						<button class="btn btn-danger">
							<i class="ace-icon fa fa-cogs"></i>
						</button>

						<!-- /section:basics/sidebar.layout.shortcuts -->
					</div>
					<div class="sidebar-shortcuts-mini" id="sidebar-shortcuts-mini">
						<span class="btn btn-success"></span>

						<span class="btn btn-info"></span>

						<span class="btn btn-warning"></span>

						<span class="btn btn-danger"></span>
					</div>
				</div><!-- /.sidebar-shortcuts -->
				<!-- #菜单顶部图标 -->

<!-- /菜单主体 -->

				<ul class="nav nav-list">
					<li class="active">
						<a href="./user/index.act">
							<i class="menu-icon fa fa-tachometer"></i>
							<span class="menu-text"> 控制面板 </span>
						</a>

						<b class="arrow"></b>
					</li>

					<li>
						<a class="dropdown-toggle">
							<i class="menu-icon fa fa-desktop"></i>
							<span class="menu-text">
								微信设置
							</span>

							<b class="arrow fa fa-angle-down"></b>
						</a>

						<b class="arrow"></b>

						<ul class="submenu">
							
							<li class="">
								<a href="./user/wechat/account/index.act">
									<i class="menu-icon fa fa-caret-right"></i>
									公众号设置
								</a>

								<b class="arrow"></b>
							</li>
							<li class="">
								<a href="javascript:isWechatSeted('./user/wechat/menu/index.act');">
									<i class="menu-icon fa fa-caret-right"></i>
									自定义菜单
								</a>

								<b class="arrow"></b>
							</li>
							<li class="">
								<a href="javascript:isWechatSeted('./user/wechat/autoReply/index.act');">
									<i class="menu-icon fa fa-caret-right"></i>
									自动回复
								</a>

								<b class="arrow"></b>
							</li>
						</ul>
					</li>


				<li>
						<a class="dropdown-toggle">
							<i class="menu-icon fa fa-desktop"></i>
							<span class="menu-text">
								素材管理
							</span>

							<b class="arrow fa fa-angle-down"></b>
						</a>

						<b class="arrow"></b>


						<ul class="submenu">
							<li class="">
								<a href="./user/matter/matter/index.act">
									<i class="menu-icon fa fa-caret-right"></i>
									素材库
								</a>

								<b class="arrow"></b>
							</li>
							
							<li class="">
								<a href="./user/matter/matterPayLog/index.act">
									<i class="menu-icon fa fa-caret-right"></i>
									已购买素材
								</a>

								<b class="arrow"></b>
							</li>
							
							
						</ul>
					</li>
					<li class="">
						<a href="./user/service/serviceApply/index.act">
							<i class="menu-icon fa fa-list-alt"></i>
							<span class="menu-text"> 服务申请 </span>
						</a>

						<b class="arrow"></b>
					</li>
					<li class="">
						<a href="./user/integralRecharge/index.act">
							<i class="menu-icon fa fa-list-alt"></i>
							<span class="menu-text">积分充值 </span>
						</a>
						<b class="arrow"></b>
					</li>

				</ul><!-- /.nav-list -->
				<!-- #菜单主体 -->
				
				
				<!-- #section:basics/sidebar.layout.minimize -->
				<div class="sidebar-toggle sidebar-collapse" id="sidebar-collapse">
					<i class="ace-icon fa fa-angle-double-left" data-icon1="ace-icon fa fa-angle-double-left" data-icon2="ace-icon fa fa-angle-double-right"></i>
				</div>

				<!-- /section:basics/sidebar.layout.minimize -->
				<script type="text/javascript">
					try{ace.settings.check('sidebar' , 'collapsed')}catch(e){}
				</script>
			</div>