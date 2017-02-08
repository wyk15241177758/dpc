var curSceneId = 0;
var curSceneName="";
var sjfl=""
$(document).ready(function() {
	//登录判断，防止缓存
	isLogin();
	// 头部的入口链接地址增加random，避免缓存
	$(".top-menu a").each(function() {
		$(this).attr("href", $(this).attr("href") + "?rand=" + Math.random());
	})

	
	// 显示左侧的场景
	leftScene();
	//显示关联分类
	getAllSjfl("scene",function(data){
		$("#sceneSjfl").empty().append(data);
	})
	
//	$("#a_addScene")
	//预设html的提示，这个字段有值时，自动将页面标题和页面地址的值置为“空”
	$(document).on("mouseenter",".pageHtml_label",function(){
		$(this).siblings(".tooltip-my").css("display","block");
	})
	$(document).on("mouseout",".pageHtml_label",function(){
		$(this).siblings(".tooltip-my").css("display","none");
	})
	$(document).on("focusout","textarea",function(){
		var textVal=$.trim($(this).val())
		if(textVal.length>0){
			$(this).parents(".form-inline").siblings().find("input[type='text']").each(function(){
				if($.trim($(this).val()).length!=0){
					$(this).prop("disabled",true)
				}else{
					$(this).val("空").prop("disabled",true)
				}
			})
			
		}else{
			if($(this).parents(".form-inline").siblings().find("input[type='text']").val()=="空"){
				$(this).parents(".form-inline").siblings().find("input[type='text']").val("").prop("disabled",false)
			}else{
				$(this).parents(".form-inline").siblings().find("input[type='text']").prop("disabled",false)
			}
		}
	})
	//绑定增加预设页面的增加按钮
	$(".title-right button").click(function(){
		addUpdatePage(0,"","","","")
	})
	
	//绑定删除预设页面按钮
	$(document).on("click","[action='a_delPage']",function(){
		$(this).parents(".pageEleDiv").next("hr").remove();
		$(this).parents(".pageEleDiv").remove()
	})
	
	// 点击左侧场景绑定事件
	$(document).on("click", ".ajax-link", function() {
		sceneClick($(this).children("i").attr("sceneId"));
	})
	// 新建按钮场景绑定事件
	$(document).on("click", "#a_addScene", function() {
		
		//先清空现有的输入框内容
    	$("#sceneModal").find("input").each(function(){
    		$(this).val("");
    	})
		$("#sceneModal").modal("show");
	})
	// 保存按钮场景绑定事件
	$("#sceneModal").find(".btn-save").click(function(){
		addScene();
	})
	//修改场景按钮绑定事件
	$(document).on("click", "#a_editScene", function() {
		//给弹出层赋值
		setSceneValue(curSceneId);
	})
	//删除场景按钮绑定事件
	$(document).on("click", "#a_delScene", function() {
    	$('#warnmsg-del').html("确定要删除场景["+curSceneName+"]吗？删除后无法恢复");
    	$("#warnModal-del").modal('show');
	})
	//删除场景提示浮层按钮绑定事件
    $("#delconfirm").click(function(){
    	$("#warnModal-del").show("hidden");
    	deleteScene(curSceneId);
    })
	//新建按钮场景映射词绑定事件
    $("#a_addSceneWord").click(function(){
    	//先清空现有的输入框内容
    	$("#sceneWordModal").find("input").each(function(){
    		$(this).val("");
    		$(this).prop("checked",false)
    		$("[type='checkbox']").checkboxradio({
	    		icon: false
	    	});	
    	})
    	//清空预设页面
		$("#pageDiv").empty();
    	$("#sceneWordModal").modal("show");
    })
    //保存按钮场景映射词绑定事件
    $("#sceneWordModal").find(".btn-save").click(function(){
		addSceneWord();
	})
	
	//修改场景映射词按钮绑定事件
	$(document).on("click","[action='sceneWordEdit']",function(){
		//先清空现有的输入框内容
    	$("#sceneWordModal").find("input").each(function(){
    		$(this).val("");
    	})
		//给弹出层赋值
		var curSceneWordId=$(this).parent("td").parent("tr").attr("sceneWordId")
		setSceneWordValue(curSceneWordId);
	})
	
	//修改场景映射词按钮绑定事件
	$(document).on("click","[action='sceneWordDel']",function(){
		var curSceneWordId=$(this).parent("td").parent("tr").attr("sceneWordId")
		$("#warnModal-sceneWord-del").find("[name='sceneWordId']").val(curSceneWordId);
		$("#warnmsg-sceneWord-del").html("确定要删除本场景映射词吗？删除后无法恢复");
    	$("#warnModal-sceneWord-del").modal('show');
	})
	//删除场景映射词提示浮层按钮绑定事件
    $("#sceneWord_delConfirm").click(function(){
    	var curSceneWordId=$("#warnModal-sceneWord-del").find("[name='sceneWordId']").val();
    	$("#warnModal-sceneWord-del").show("hidden");
    	deleteSceneWord(curSceneWordId);
    })
    
    //检索预览按钮
    $(document).on("click","a[action='searchPreview']",function(){
    	var curOutWords=$(this).parent("td").parent("tr").children("td:eq(1)").html();
   		showSearchPreview(curOutWords);
    })
    //设置列宽
	$(".table-bordered").dataTable( {
		"stateSave": true,
		"bStateSave":true,
		  "columns": [
		    null,
		    null,
		    null,
		    { "width": "22%" ,"orderable": false }
		  ],
		  "order": [[ 2, 'desc' ]],
          "language": {
         	 "loadingRecords":"正在初始化...",
         	 "processing":"正在搜索...",
         	 "paginate": {
         		 "previous":"上一页",
         		 "next": "下一页"
         	 },
         	 "info": " _PAGE_/_PAGES_页，共_TOTAL_条 ",
         	 "infoFiltered": " - 在 _MAX_ 条记录中检索",
         	 "infoEmpty": "没有找到相关记录",
         	 "search": "搜索",
         	 "zeroRecords": "没有搜索到相关内容"
          }
		} );
//		window.setTimeout(function(){
//			$($("[action='sceneWordEdit']")[0]).click();
//		},1000)
});

//获得所有的分类
function getAllSjfl(eleIdPre,callBack){
	var template="<label for='{eleId}'>{sjfl}<input type='checkbox' name='{eleId}' id='{eleId}' ></label>"
	var url="/QASystem/admin/scene/getQaSjfl.do";
	var param=null;
	$.getJSON(url,param,function(data){
		var out="";
		if(data.sig){
			for(var i=0;i<data.msg.length;i++){
				var msg=data.msg[i];
				var obj=[
					{"name":"eleId","value":eleIdPre+"_"+i},
					{"name":"sjfl","value":msg}
				];
				out+=replaceTemplate(template,obj);
			}
			if(typeof(callBack)=="function"){
				callBack(out)
			}
		}
		
	})
}

//显示预览结果
function showSearchPreview(curOutWords){
	var param={
			"question":curOutWords,
			"end":10,
			"splitBy":" ",
			"isSplit":"true"
	}
	$("#log").html("");
	$.getJSON("/QASystem/admin/qaSearch.do",param,function(data){
		var template="<div id='previewDiv'><ul>{categoryLi}</ul>{qaDiv}</div>";
		var categoryLi="";
		var qaDiv="";
		
		var msg=data.msg;
		var index=0;
		if(data.sig){
			for(i in msg){
				if(msg[i].length==0){
					continue;
				}
				index++;
				var curQaLi="";
				var curQaDiv="";
				//遍历分类
					categoryLi+="<li><a href='#previewTab_"+index+"'>"+i+"</a></li>"
					for(var j=0;j<msg[i].length;j++){
						//如果有预设页面html则优先展示
						if(msg[i][j].html!=undefined&&msg[i][j].html!=null&&msg[i][j].html.length>0){
							curQaDiv+=msg[i][j].html;
						}else{
							if(msg[i][j].url.indexOf("http")==-1){
								curQaLi+="<li><a href='http://"+msg[i][j].url+"' target='_blank'>"+msg[i][j].title+"</a></li>"
							}else{
								curQaLi+="<li><a href='"+msg[i][j].url+"' target='_blank'>"+msg[i][j].title+"</a></li>"
							}
						}
					}
					qaDiv+="<div id='previewTab_"+index+"'>"+curQaDiv+"<ul> "+curQaLi+" </ul> </div>";
			}
		}else{
			qaDiv="无结果";
		}
		
		template=template.replace(/\{categoryLi\}/g,categoryLi);
		template=template.replace(/\{qaDiv\}/g,qaDiv);
		 	
		$("#log").html(template); 	
		$("#previewDiv").tabs({
      		event: "mouseover"
   		});
	  })
}

//预设页面
function addUpdatePage(pageId,pageTitle,pageLink,pageHtml,sjfl){
	var curPageIndex=$(".pageEleDiv").length;
	
	var template="<div class='pageEleDiv'><input type='hidden' name='pageId' value='{pageId}' /><div class='form-inline'><div><a href='#' action='a_delPage'>删除此页面</a></div><div class='form-group'><label class='control-label'>页面标题</label> <input {disabled} type='text' class='form-control' required='true' desc='页面标题' name='pageTitle' value='{pageTitle}'></div></div><div class='form-inline'><div class='form-group'><label class='control-label'>页面地址</label> <input {disabled} type='text' class='form-control' required='true' desc='页面地址' name='pageLink' value='{pageLink}'></div></div><div class='form-inline' style='height:55px'><label class='control-label'>关联分类</label><div style='width:77%;float:right' id='{sjflId}'></div></div><div class='form-inline'><div class='form-group' style='position: relative'><div class='tooltip-my'>此字段有值会自动屏蔽页面标题和页面地址</div><label class='control-label pageHtml_label' >页面html&nbsp;<i class='glyphicon glyphicon-exclamation-sign'></i></label> <textarea class='form-control' style='width: 315px;height: 100px;overflow-y: scroll' required='false' desc='页面html' name='pageHtml'>{pageHtml}</textarea></div></div></div>"
	
	if(curPageIndex>0){
		template="<hr>"+template;
	}
	var obj=[
				{"name":"pageTitle","value":pageTitle},
				{"name":"pageLink","value":pageLink},
				{"name":"pageHtml","value":(pageHtml==null?"":pageHtml)},
				{"name":"pageId","value":pageId},
				{"name":"sjflId","value":"sjflId_"+curPageIndex},
				{"name":"disabled","value":""}
			];
	if(pageHtml!=null&&pageHtml.length>0){
		obj[5].value="disabled"
	}
	template=replaceTemplate(template,obj);
	$("#pageDiv").append(template);
	getAllSjfl("pageSjfl_"+curPageIndex,function(data){
		$("#sjflId_"+curPageIndex).empty().append(data);
		//修改选中状态
		if(sjfl){
			initSjflStatus("sjflId_"+curPageIndex,sjfl.split(";"))
		}else{
			initSjflStatus("sjflId_"+curPageIndex,new Array())
		}
		
	})
}


//删除场景
function deleteScene(sceneId){
	var param={"sceneId":sceneId};
	if(sceneId!=undefined){
		  $.getJSON("/QASystem/admin/scene/delScene.do",param,function(data){
				if (data.sig == false) {
					$('#warnmsg').html(data.msg);
					$('#warnModal').modal('show');
				} else {
					// 成功新建任务，自动关闭浮层
					$('#warnmsg').html(data.msg);
					$('#warnModal').modal('show');
					window.setTimeout(function() {
						$('#warnModal').modal('hide');
						// 刷新场景列表
						leftScene();
					}, 2000)
				}
		  })
	}
}

//删除场景映射词
function deleteSceneWord(sceneWordId){
	var param={"sceneWordId":sceneWordId};
	if(sceneWordId!=undefined){
		  $.getJSON("/QASystem/admin/scene/delSceneWord.do",param,function(data){
				if (data.sig == false) {
					$('#warnmsg').html(data.msg);
					$('#warnModal').modal('show');
				} else {
					// 成功新建任务，自动关闭浮层
					$('#warnmsg').html(data.msg);
					$('#warnModal').modal('show');
					window.setTimeout(function() {
						$('#warnModal').modal('hide');
						// 刷新场景映射词
						sceneClick(curSceneId)
					}, 2000)
				}
		  })
	}
}


//新增或修改场景
function addScene(){
	var operationUrl = "/QASystem/admin/scene/saveOrUpdateScene.do"
		if (!paramCheck($("#sceneModal"))) {
			// paramCheck中处理
		} else {
			$('#saveModal').modal('show');
			var param = {};
			$("#sceneModal").find("input").each(function() {
				if ($(this).attr("name") != undefined) {
					param[$(this).attr('name')] = $(this).val();
				}
			})
			$.getJSON(operationUrl, param, function(data) {
				$('#saveModal').modal('hide');
				if (data.sig == false) {
					$('#warnmsg').html(data.msg);
					$('#warnModal').modal('show');
				} else {
					// 成功新建任务，自动关闭浮层
					$('#warnmsg').html(data.msg);
					$('#warnModal').modal('show');
					window.setTimeout(function() {
						$('#warnModal').modal('hide');
						$('#sceneModal').modal('hide');
						//清空浮层中的数据
						$("#sceneModal").find("input").val("");
						// 刷新场景列表
						leftScene();
					}, 2000)
				}
			})
		}
}
// 点击场景
function sceneClick(sceneId) {
	
	// 修改当前场景ID
	curSceneId = sceneId;
	curSceneName =$("i[sceneId="+sceneId+"]").attr("sceneName")
	$("i[sceneId="+sceneId+"]").parent("a").parent("li").addClass("active").siblings().removeClass("active");
	// 清空右侧数据
	var table=$(".table-bordered").DataTable();
	table.clear().draw();
	// 修改右侧数据
	var template = "<tr sceneId="
			+ curSceneId
			+ " sceneWordId={sceneWordId}><td>{enterWord}</td><td>{outWord}</td>" 
			+ "<td>{createTime}</td><td class=\"center font-right\">" +
					"<a class=\"btn btn-success btn-sm\" href=\"#\" action='searchPreview'>" +
					"<i class=\"glyphicon glyphicon-zoom-in icon-white\"></i>检索预览</a>" +
					" <a class=\"btn btn-info btn-sm btn-setting\" href=\"#\" action='sceneWordEdit'> 	" +
					"<i class=\"glyphicon glyphicon-edit icon-white\"></i>编辑 </a>" +
					" <a class=\"btn btn-danger btn-sm btn-warn\" href=\"#\"  action='sceneWordDel'>" +
					"<i class=\"glyphicon glyphicon-trash icon-white\"></i>删除 </a> " +
					"</td> </tr>";
	var param = {
		"sceneId" : curSceneId,
		"random" : Math.random()
	};
	$.getJSON("/QASystem/admin/scene/listSceneWords.do", param, function(data) {
		for (i in data) {
			var curRowNode=table.row.add([data[i].enterWords,data[i].outWords,data[i].createTime,"<a class=\"btn btn-success btn-sm\" href=\"#\" action='searchPreview'>" +
			          					"<i class=\"glyphicon glyphicon-zoom-in icon-white\"></i>检索预览</a>" +
			        					" <a class=\"btn btn-info btn-sm btn-setting\" href=\"#\" action='sceneWordEdit'> 	" +
			        					"<i class=\"glyphicon glyphicon-edit icon-white\"></i>编辑 </a>" +
			        					" <a class=\"btn btn-danger btn-sm btn-warn\" href=\"#\"  action='sceneWordDel'>" +
			        					"<i class=\"glyphicon glyphicon-trash icon-white\"></i>删除 </a> "])
			.draw()
		    .node();
			//新增行变色突出
			$( curRowNode )
		    .css( 'color', 'black' )
		    .animate( { color: 'black' },500 );
			//新增行增加属性
			$( curRowNode ).attr("sceneId",curSceneId);
			$( curRowNode ).attr("sceneWordId", data[i].sceneWordId);
			//增加操作按钮
			
		}
		
//		$('.table-bordered').DataTable();
	})

}

// 左侧场景
function leftScene() {
	// 清空场景列表
	$("#leftScene").empty();
	var template = "<li><a href=\"javascript:void(0)\" class=\"ajax-link\">     <i name=\"leftword\" sceneId={sceneId} sceneName={sceneName}></i>{sceneName}</a></li>"
	$.getJSON("/QASystem/admin/scene/listScenes.do", null, function(data) {
		var cur = "";
		for (i in data) {
			cur += template.replace(/\{sceneId\}/g, data[i].sceneId).replace(
					/\{sceneName\}/g, data[i].sceneName);
			if (i == 0) {
				curSceneId = data[i].sceneId;
				curSceneName = data[i].sceneName;
			}
		}
		$("#leftScene").append(cur);
		// 点击curSceneId场景用于显示右侧场景映射词
		$("i[sceneid=" + curSceneId + "]").parent("a").click();
	})
}


//新增或修改场景映射词
function addSceneWord(){
	var operationUrl = "/QASystem/admin/scene/saveOrUpdateSceneWord.do"
		if (!paramCheck($("#sceneWordModal"))) {
			// paramCheck中处理
		} else {
			$('#saveModal').modal('show');
			//处理关联分类
			var sjfl="";
			sjfl=generateSjflParam($("#sceneSjfl"),";")
			$("#sceneWordModal").find("input[name='sjfl']").val(sjfl);
			//处理预设页面
			var pageTitles="";
			var pageLinks="";
			var pageHtmls="";
			var pageIds="";
			var pageSjfls="";
			$(".pageEleDiv").each(function(){
				if(pageTitles.length==0){
					pageTitles=$(this).find("[name='pageTitle']").val();
					pageLinks=$(this).find("[name='pageLink']").val();
					pageHtmls=$(this).find("[name='pageHtml']").val();
					pageIds=$(this).find("[name='pageId']").val();
					pageSjfls=generateSjflParam($(this),",")
				}else{
					pageTitles+=";"+$(this).find("[name='pageTitle']").val();
					pageLinks+=";"+$(this).find("[name='pageLink']").val();
					pageHtmls+=";"+$(this).find("[name='pageHtml']").val();
					pageIds+=";"+$(this).find("[name='pageId']").val();
					pageSjfls+=";"+generateSjflParam($(this),",")					
				}
			})
			$("#sceneWordModal").find("input[name='pageTitles']").val(pageTitles);
			$("#sceneWordModal").find("input[name='pageLinks']").val(pageLinks);
			$("#sceneWordModal").find("input[name='pageHtmls']").val(pageHtmls);
			$("#sceneWordModal").find("input[name='pageIds']").val(pageIds);
			$("#sceneWordModal").find("input[name='pageSjfls']").val(pageSjfls);
			
			var param = {
					"sceneName":curSceneName,
					"sceneId":curSceneId
					};
			$("#sceneWordModal").find("input").each(function() {
				if ($(this).attr("name") != undefined) {
					//中文分号替换为英文，避免输错
					param[$(this).attr('name')] = $(this).val().replace(/；/g,";");
				}
			})
			$.getJSON(operationUrl, param, function(data) {
				$('#saveModal').modal('hide');
				if (data.sig == false) {
					$('#warnmsg').html(data.msg);
					$('#warnModal').modal('show');
				} else {
					// 成功新建映射词，自动关闭浮层
					$('#warnmsg').html(data.msg);
					$('#warnModal').modal('show');
					window.setTimeout(function() {
						$('#warnModal').modal('hide');
						$('#sceneWordModal').modal('hide');
						//清空浮层中的数据
						$("#sceneWordModal").find("input").val("");
						$("#sceneWordModal").find("textarea").val("");
						// 刷新场景映射词列表
						sceneClick(curSceneId)
						
					}, 2000)
				}

			})
		}
}

//组合提交时关联分类的值
function generateSjflParam(obj,splitBy){
	var sjfl="";
	obj.find("input[type='checkbox']").each(function(){
		if($(this).prop("checked")){
			if(sjfl.length==0){
				sjfl=$(this).parent("label").text();
			}else{
				sjfl+=splitBy+$(this).parent("label").text();
			}
		}
	})
	return sjfl;
}

//修改场景映射词给弹出浮层赋值
function setSceneWordValue(sceneWordId){
	//先清空原先的值
	$("#sceneWordModal").find("textarea").val("");
	$("#sceneWordModal").find("input").each(function(){
		var name=$(this).attr("name")
		if($(this).attr("type")=='checkbox'){
			$(this).prop("checked",false)
		}
		else{
			$(this).val("")
		}
	})
	
	
	//正在修改的sceneWordId
	$('#sceneWordModal').find("[name='sceneWordId']").val(sceneWordId);
	var param={"sceneWordId":sceneWordId};
	 $.getJSON("/QASystem/admin/scene/getSceneWord.do",param,function(data){
		  if(data==null||typeof(data.sig)=='undefined'
			  ||typeof(data.msg)=='undefined'){
				$('#warnmsg').html("获得sceneWordId=["+param.sceneWordId+"]的映射词信息失败");
				$('#warnModal').modal('show');
		  }else if(data.sig==false){
			$('#warnmsg').html(data.msg);
     		$('#warnModal').modal('show');
		  }else{
			  var msg=data.msg;
			  $("input[name='enterWords']").val(msg.enterWords);
			  $("input[name='outWords']").val(msg.outWords);
			  if(msg.sjfl){
			  	$("input[name='sceneSjfl']").val(msg.sjfl)
			  	var tempArr=msg.sjfl.split(";")
				initSjflStatus("sceneSjfl",tempArr)
			  }
			//清空预设页面
			$("#pageDiv").empty();
			//预设页面
			var pageArr=msg.scenePageList;
			for(var i=0;i<pageArr.length;i++){
				addUpdatePage(pageArr[i].scenePageId,pageArr[i].pageTitle,pageArr[i].pageLink,pageArr[i].html,pageArr[i].sjfl)
			}
  			//显示设置浮层
  			 $('#sceneWordModal').modal('show');
		  }
		  
	  })
}

//修改关联分类的选中状态
function initSjflStatus(divId,sjflArr){
  	$("#"+divId).find("label").each(function(){
		for(var i=0;i<sjflArr.length;i++){
			if($(this).text()==sjflArr[i]){
				$(this).children("input").prop("checked",true)
				break;
			}
		}
	})
	$("[type='checkbox']").checkboxradio({
		icon: false
	});	
}

//修改场景给弹出浮层赋值
function setSceneValue(sceneId){
	//正在修改的sceneWordId
	$('#sceneModal').find("[name='sceneId']").val(sceneId);

	
	var param={"sceneId":sceneId};
	 $.getJSON("/QASystem/admin/scene/getScene.do",param,function(data){
		  if(data==null||typeof(data.sig)=='undefined'
			  ||typeof(data.msg)=='undefined'){
				$('#warnmsg').html("获得sceneId=["+param.sceneId+"]的场景信息失败");
				$('#warnModal').modal('show');
		  }else if(data.sig==false){
			$('#warnmsg').html(data.msg);
     		$('#warnModal').modal('show');
		  }else{
			  var msg=data.msg;
			  $("input[name='sceneName']").val(msg.sceneName);
  			  //显示设置浮层
  			 $('#sceneModal').modal('show');
		  }
		  
	  })
}

// 参数校验
function paramCheck(obj) {
	var flag = true;
	// 非空校验
	obj.find("[required='true']").each(function() {
		if ($(this).val().length == 0) {
			alert($(this).attr("desc") + "不能为空");
			$(this).focus();
			flag = false;
			return false;
		}
	})
	if (flag) {
		// 数字校验
		obj.find("[num='true']").each(function() {
			$(this).val(parseInt($(this).val()));
			if ($(this).attr("max") != undefined) {
				if ($(this).val() > $(this).attr("max")) {
					alert($(this).attr("desc") + "不能大于" + $(this).attr("max"));
					$(this).focus();
					flag = false;
					return false;
				}
			}
			if ($(this).attr("min") != undefined) {
				if ($(this).val() < $(this).attr("min")) {
					alert($(this).attr("desc") + "不能小于" + $(this).attr("min"));
					$(this).focus();
					flag = false;
					return false;
				}
			}
		})
	}

	return flag;
}
function replaceTemplate(template,obj){
	var out="";
	for(i in obj){
		var reg=new RegExp("\\{"+obj[i].name+"\\}","g");
		template=template.replace(reg,obj[i].value);
	}
	out=template;
	return out;
}

//登录判断，未登录跳转到登录页。防止缓存
function isLogin(){
	var param={"ran":Math.random()};
	$.getJSON("/QASystem/admin/loginStatus.act",param,function(data){
		if(!data.sig){
			window.location="/QASystem/gateway/login.html"
		}
	})
}


