var curSceneId = 0;
var curSceneName="";
$(document).ready(function() {

	// 头部的入口链接地址增加random，避免缓存
	$(".top-menu a").each(function() {
		$(this).attr("href", $(this).attr("href") + "?rand=" + Math.random());
	})
	// 显示左侧的场景
	leftScene();

	// 点击左侧场景绑定事件
	$(document).on("click", ".ajax-link", function() {
		sceneClick($(this).children("i").attr("sceneId"));
	})
	// 新建按钮场景绑定事件
	$(document).on("click", "#a_addScene", function() {
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
		  "columns": [
		    null,
		    null,
		    null,
		    { "width": "22%" ,"orderable": false }
		  ],
		  "order": [[ 2, 'desc' ]]
		} );
});
//显示预览结果
function showSearchPreview(curOutWords){
	var param={
			"question":curOutWords,
			"end":10,
			"splitBy":" "
	}
	$("#log").html("");
	 $.getJSON("/QASystem/admin/luceneSearch.do",param,function(data){
		var msgArray=data.msg;
		if(data.sig==true){
			if(msgArray.length==0){
				$("#log").html("检索词["+param.question+"]无命中信息");
			}
			 for(i in msgArray){
				 $("#log").append((parseInt(i)+1)+". <a href='"+msgArray[i].url+"' target='_blank'>"+data.msg[i].title+"</a><br/>")
			 }
		}else{
			$("#log").html("检索["+param.question+"]失败，错误信息为"+msgArray);
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
						// 刷新场景映射词列表
						sceneClick(curSceneId)
						
					}, 2000)
				}

			})
		}
}

//修改场景映射词给弹出浮层赋值
function setSceneWordValue(sceneWordId){
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
  			  //显示设置浮层
  			 $('#sceneWordModal').modal('show');
		  }
		  
	  })
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
