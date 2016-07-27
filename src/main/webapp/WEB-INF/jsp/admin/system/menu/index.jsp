<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/include/head/tagLib.jsp" %>
<style type="text/css">
.ztree li span.button.add {margin-left:2px; margin-right: -1px; background-position:-144px 0; vertical-align:top; *vertical-align:middle}
.button{
float:none;
}
	</style>
	 <SCRIPT type="text/javascript">
		<!--
		var setting = {
			view: {
				addHoverDom: addHoverDom,
				removeHoverDom: removeHoverDom,
				selectedMulti: false
			},
			edit: {
				enable: true,
				editNameSelectAll: true,
				showRemoveBtn: showRemoveBtn,
				showRenameBtn: showRenameBtn
			},
			data: {
				simpleData: {
					enable: true
				}
			},
			callback: {
				beforeDrag: beforeDrag,
				beforeDrop: beforeDrop,
				beforeDragOpen: beforeDragOpen,
				onDrag: onDrag,
				onDrop: onDrop,
				onExpand: onExpand,
				beforeEditName: beforeEditName,
				beforeRemove: beforeRemove,
				beforeRename: beforeRename,
				onRemove: onRemove,
				onRename: onRename
			}
		};
		var zNodes =[
			${zNodes }
			{ id:0, pId:0, name:"根目录",childOuter:false}
		];
		/* function beforeDrag(treeId, treeNodes) {
			return false;
		} */
		function beforeEditName(treeId, treeNode) {
			return true;
		}
		function beforeRemove(treeId, treeNode) {
			if(treeNode.id < 0){
				return true;
			}else{
				alert(treeNode.name+"是系统默认菜单，不能删除！");
				return false;
			}
		}
		function onRemove(e, treeId, treeNode) {
			
		}
		function beforeRename(treeId, treeNode, newName, isCancel) {
			if (newName.length == 0) {
				alert("节点名称不能为空.");
				var zTree = $.fn.zTree.getZTreeObj("treeDemo");
				setTimeout(function(){zTree.editName(treeNode)}, 10);
				return false;
			}
			return true;
		}
		function onRename(e, treeId, treeNode, isCancel) {
		}
		function showRemoveBtn(treeId, treeNode) {
			return treeNode.id < 0;
		}
		function showRenameBtn(treeId, treeNode) {
			return treeNode.id != 0;
		}
		var menuId = 0;
		function addHoverDom(treeId, treeNode) {
			var sObj = $("#" + treeNode.tId + "_span");
			if (treeNode.editNameFlag || $("#addBtn_"+treeNode.tId).length>0) return;
			var addStr = "<span class='button add' id='addBtn_" + treeNode.tId
				+ "' title='添加菜单' onfocus='this.blur();'></span>";
			sObj.after(addStr);
			var btn = $("#addBtn_"+treeNode.tId);
			if (btn) btn.bind("click", function(){
				menuId -= 1; 
				for(var a = 0;a < zNodes.length;a++){
					var temp = zNodes[a].id;
					if(menuId > temp){
						menuId = temp - 1;
					}
				}
				var url1 = prompt("请填写url（可以为空）");
				var zTree = $.fn.zTree.getZTreeObj("treeDemo");
				zTree.addNodes(treeNode, {id:(menuId), pId:treeNode.id, url:url1, name:"菜单"});
				return false;
			});
		};
		function removeHoverDom(treeId, treeNode) {
			$("#addBtn_"+treeNode.tId).unbind().remove();
		};
		
		function modifyMenu(){
			var zObj = $.fn.zTree.getZTreeObj('treeDemo');
			var nodes = zObj.transformToArray(zObj.getNodes());
			var menus = new Array(nodes.length - 1);
			for(var i = 1; i < nodes.length; i++){
				var temp = new Object();
				temp.id = nodes[i].id;
				temp.url = nodes[i].url;
				temp.fatherId = nodes[i].pId;
				temp.name = nodes[i].name;
				temp.order1 = i;
				menus[i - 1] = temp
			}
			$.ajax({ 
				type: "post", 
				url : "<st:getCurrentUrl lastUrl="update.act" />", 
				dataType:'json',
				data: {menuList:$.toJSON(menus)}, 
				success: function(json){
					if(json.statusCode == 200){
						alert("修改成功！！");
					}else{
						alert(json.message);
					}
				}
				});
		}
		
		/* 拖拽功能 */
		
		function dropPrev(treeId, nodes, targetNode) {
			var pNode = targetNode.getParentNode();
			if (pNode && pNode.dropInner === false) {
				return false;
			} else {
				for (var i=0,l=curDragNodes.length; i<l; i++) {
					var curPNode = curDragNodes[i].getParentNode();
					if (curPNode && curPNode !== targetNode.getParentNode() && curPNode.childOuter === false) {
						return false;
					}
				}
			}
			return true;
		}
		function dropInner(treeId, nodes, targetNode) {
			if (targetNode && targetNode.dropInner === false) {
				return false;
			} else {
				for (var i=0,l=curDragNodes.length; i<l; i++) {
					if (!targetNode && curDragNodes[i].dropRoot === false) {
						return false;
					} else if (curDragNodes[i].parentTId && curDragNodes[i].getParentNode() !== targetNode && curDragNodes[i].getParentNode().childOuter === false) {
						return false;
					}
				}
			}
			return true;
		}
		function dropNext(treeId, nodes, targetNode) {
			var pNode = targetNode.getParentNode();
			if (pNode && pNode.dropInner === false) {
				return false;
			} else {
				for (var i=0,l=curDragNodes.length; i<l; i++) {
					var curPNode = curDragNodes[i].getParentNode();
					if (curPNode && curPNode !== targetNode.getParentNode() && curPNode.childOuter === false) {
						return false;
					}
				}
			}
			return true;
		}

		function beforeDrag(treeId, treeNodes) {
			for (var i=0,l=treeNodes.length; i<l; i++) {
				if (treeNodes[i].drag === false) {
					curDragNodes = null;
					return false;
				} else if (treeNodes[i].parentTId && treeNodes[i].getParentNode().childDrag === false) {
					curDragNodes = null;
					return false;
				}
			}
			curDragNodes = treeNodes;
			return true;
		}
		function beforeDragOpen(treeId, treeNode) {
			autoExpandNode = treeNode;
			return true;
		}
		function beforeDrop(treeId, treeNodes, targetNode, moveType, isCopy) {
			if(targetNode.id == 0 ){
				return "prev".equals(moveType) || "next".equals(moveType);
			}
			return true;
		}
		function onDrag(event, treeId, treeNodes) {
		}
		function onDrop(event, treeId, treeNodes, targetNode, moveType, isCopy) {
		}
		function onExpand(event, treeId, treeNode) {
		}
		
		$(document).ready(function(){
			$.fn.zTree.init($("#treeDemo"), setting, zNodes);
		});
		//-->
	</SCRIPT>
	<div style="padding-left: 10%;padding-top: 5px;" class="zTreeDemoBackground left">
		<div style="float: right;margin-right: 30px;" class="button"><div class="buttonContent">
		<button onclick="modifyMenu();">提交</button>
		</div></div>
		<ul style="text-align: right;" id="treeDemo" class="ztree"></ul>
	</div>
