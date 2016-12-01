//list的template
var list_template="<tr sceneId='{sceneId}' sceneName='{sceneName}'><td>{sceneName}</td>" +
"<td class='center'>{enterWords}</td>" +
"<td  class='center'>{outWords}</td><td  class='center'>{createTime}</td>" +
"<td class='center font-right'>" +
"<a class='btn btn-success btn-sm' href='javascript:void(0)' action='search_a'>" +
"<i class='glyphicon glyphicon-zoom-in icon-white'></i>预览检索结果</a>  " +
"<a class='btn btn-info btn-sm btn-setting' href='javascript:void(0)'  action='edit_a'>" +
"<i class='glyphicon glyphicon-edit icon-white'></i>编辑</a>  " +
"<a class='btn btn-danger btn-sm btn-warn' href='javascript:void(0)'  action='delete_a'>" +
"<i class='glyphicon glyphicon-trash icon-white'></i>删除</a></td></tr>";
function paramCheck(){
	var flag=true;
	//非空校验
	$("[required='true']").each(function(){
		if($(this).val().length==0){
			alert($(this).attr("desc")+"不能为空");
			$(this).focus();
			flag=false;
			return false;
		}
	})
	if(flag){
		//数字校验
		$("[num='true']").each(function(){
			$(this).val(parseInt($(this).val()));
			if($(this).attr("max")!=undefined){
				if($(this).val()>$(this).attr("max")){
					alert($(this).attr("desc")+"不能大于"+$(this).attr("max"));
					$(this).focus();
					flag=false;
					return false;
				}
			}
			if($(this).attr("min")!=undefined){
				if($(this).val()<$(this).attr("min")){
					alert($(this).attr("desc")+"不能小于"+$(this).attr("min"));
					$(this).focus();
					flag=false;
					return false;
				}
			}
		})
	}
	return flag;
}


//根据指定场景检索
function sceneSearch(sceneId){
	var param={"sceneId":sceneId};
	
	//获得场景的出口词
	 $.getJSON("/QASystem/admin/scene/getScene.do",param,function(data){
		 if(data.sig==true){
			 if(typeof(data.msg)!='undefined'){
				 var outWords=data.msg.outWords.replace(/;/g," ");
				 if(outWords.length>0){
					 var param={"question":outWords,"begin":0,"end":10};
					 //根据出口词检索，不做NLP分析直接全文检索，多个词的检索参数以空格分隔
					 $("#log").html("");
					 $.getJSON("/QASystem/admin/luceneSearch.do",param,function(data){
						 var msgArray=data.msg;
						 for(i in msgArray){
							 $("#log").append((parseInt(i)+1)+". <a href='"+msgArray[i].url+"' target='_blank'>"
									 +msgArray[i].title+"</a><br/>")
						 }
						 if(msgArray=='undefined'||msgArray.length==0){
							 $("#log").html("没有检索到结果");
						 }
					 })
				 }
			 }
			 
		 }
	})
//	
//	//任务数量
//	var jobNum=$("#tbody_joblist").children("tr").length;
//	//已刷新日志个数，用于所有任务执行完成后更换图片的功能
//	var jobRefreshNum=0;
//	$("#tbody_joblist").children("tr").each(function(){
//		//没有任务则结束循环
//		if($(this).html()=="<td colspan='6'>暂无任务</td>"){
//			return false;
//		}
//		var param={"jobid":$(this).attr("jobid")};
//		 $.getJSON("/QASystem/admin/getRunningLog.do",param,function(data){
//			 if(data.sig==true){
//				 if(typeof(data.msg)!='undefined'){
//					 var msgArray=data.msg;
//					 if((data.msg+"").indexOf("结束执行任务")==-1){
//						$("#tbody_joblist").children("tr[jobid='"+param.jobid+"']").children("td:nth-child(2)").html("<span class='label-success label'>正在执行</span>")
//					 }else{
//						$("#tbody_joblist").children("tr[jobid='"+param.jobid+"']").children("td:nth-child(2)").html("<span class='label label-warning'>未执行</span>")
//					 }
//					 //未选择查看日志，则默认显示第一个任务的日志
//					 var curTR=$("#tbody_joblist").children("tr[jobid='"+param.jobid+"']")
//					 var curTRJobName=curTR.attr("jobname")
//					 var curTRJobId=curTR.attr("jobid")
//					 if(curJobId==0){
//						 //第一个TR
//						 if($("#tbody_joblist").children("tr").index(curTR)==0){
//							 $("ul[role='tablist']").find("strong").text("任务日志["+curTRJobName+"]");
//							 //先清空日志，再输出
//							 $("#log").html("");
//							 for(i in msgArray){
//								 $("#log").append((parseInt(i)+1)+"."+msgArray[i]+"<br/>")
//							 }
//						 }
//					 }else{
//						 //显示指定的任务日志
//						 if(curJobId==curTRJobId){
//							 $("ul[role='tablist']").find("strong").text("任务日志["+curTRJobName+"]");
//							 //先清空日志，再输出
//							 $("#log").html("");
//							 for(i in msgArray){
//								 $("#log").append((parseInt(i)+1)+"."+msgArray[i]+"<br/>")
//							 }
//						 }
//						 
//					 }
//				 }
//			 }
//			  
//		  })
//		
//	 })
}

//获得场景列表
function getSceneList(){
    $.getJSON("/QASystem/admin/scene/listScenes.do",null,function(data){
    	$("#tbody_joblist").html("");
    	if(data.length==0){
    		$("#tbody_joblist").append("<tr><td colspan='5'>暂无场景</td></tr>");
    	}
    	
    	for(scene in data){
    		var cur_td="";
    		cur_td=list_template.replace(/\{sceneName\}/g,data[scene].sceneName);
    		cur_td=cur_td.replace(/\{sceneId\}/g,data[scene].sceneId);
    		cur_td=cur_td.replace(/\{enterWords\}/g,data[scene].enterWords);
    		cur_td=cur_td.replace(/\{outWords\}/g,data[scene].outWords);
    		cur_td=cur_td.replace(/\{createTime\}/g,data[scene].createTime);
    		$("#tbody_joblist").append(cur_td);
    	}
    })
}
//删除场景
function deleteScene(curTrSceneId){
	var param={"sceneId":curTrSceneId};
	if(curTrSceneId!=undefined){
		  $.getJSON("/QASystem/admin/scene/delScene.do",param,function(data){
			  if(data==null||typeof(data.sig)=='undefined'
				  ||typeof(data.msg)=='undefined'){
				$('#warnmsg').html("删除sceneId=["+param.sceneId+"]的场景信息失败");
          		$('#warnModal').modal('show');
			  }else{
				$('#warnmsg').html(data.msg);
        		$('#warnModal').modal('show');
			  }
			  getSceneList();
		  })
	}
}

$(document).ready(function () {
	
	//提示登录信息
	var msg=getUrlParam("msg");
	if(msg!=null&&msg.length>0){
		$(".alert").html(msg);
		$(".alert").show();
		$(".alert").css("display","block");
	}
	
	//获得场景列表
	getSceneList();
	
    //新建场景
    $('.btn-setting').click(function (e) {
        e.preventDefault();
        //先清空现有的输入框内容
    	$("#setModal").find("input").each(function(){
    		$(this).val("");
    	})
        $('#setModal').modal('show');
    });
    //提示浮层弹出
    $('.btn-warn').click(function (e) {
        e.preventDefault();
        $('#warnModal').modal('show');
        
    });
    //点击预览按钮
    $(document).on("click","a[action='search_a']",function(){
    	console.log("search_a click")
    	sceneSearch($(this).parents("tr").attr("sceneId"))
    })
    
    //点击编辑
    $(document).on("click","a[action='edit_a']",function(){
    	//先清空现有的输入框内容
    	$("#setModal").find("input").each(function(){
    		$(this).val("");
    	})
    	var curTrSceneId=$(this).parents("tr").attr("sceneId");
    	var param={"sceneId":curTrSceneId};
    	if(curTrSceneId!=undefined){
    		  $.getJSON("/QASystem/admin/scene/getScene.do",param,function(data){
    			  if(data==null||typeof(data.sig)=='undefined'
    				  ||typeof(data.msg)=='undefined'){
    					$('#warnmsg').html("获得id=["+param.sceneId+"]的场景信息失败");
                		$('#warnModal').modal('show');
    			  }else if(data.sig==false){
    				  console.log(11);
    				$('#warnmsg').html(data.msg);
              		$('#warnModal').modal('show');
    			  }else{
    				  //给弹出层赋值
	       			  $("input[name='sceneId']").val(data.msg.sceneId);
	       			  $("input[name='sceneName']").val(data.msg.sceneName);
	       			  $("input[name='enterWords']").val(data.msg.enterWords);
	       			  $("input[name='outWords']").val(data.msg.outWords);
	       			  //显示设置浮层
	       			 $('#setModal').modal('show');
    			  }
    			  
    		  })
    	}
    });
    
    //删除任务
    $(document).on("click","a[action='delete_a']",function(){
    	var curTrSceneId=$(this).parents("tr").attr("sceneId");
    	var curTrSceneName=$(this).parents("tr").attr("sceneName");
    	$('#warnmsg-del').html("确定要删除场景["+curTrSceneName+"]吗？删除后无法恢复");
    	$('#warnmsg-del').attr("delSceneId",curTrSceneId)
    	$("#warnModal-del").modal('show');
//    	
//    	if(curTrJobId!=undefined){
//    		  $.getJSON("/QASystem/admin/delJob.do",param,function(data){
//    			  if(data==null||typeof(data.sig)=='undefined'
//    				  ||typeof(data.msg)=='undefined'){
//    					$('#warnmsg').html("删除jobid=["+param.jobid+"]的任务信息失败");
//                		$('#warnModal').modal('show');
//    			  }else{
//    				$('#warnmsg').html(data.msg);
//              		$('#warnModal').modal('show');
//    			  }
//    			  getJobList();
//    		  })
//    	}
    })
    //点击删除确认按钮
    $("#delconfirm").click(function(){
    	$("#warnModal-del").show("hidden");
    	deleteScene($('#warnmsg-del').attr("delSceneId"));
    })
    
    //保存浮层弹出
    $('.btn-save').click(function (e) {
    	e.preventDefault();
    	var operationUrl="/QASystem/admin/scene/saveOrUpdateScene.do";
    	if(!paramCheck()){
    		 //paramCheck中处理
    	}else{
    		$('#saveModal').modal('show');
    		var param={};
            $(".modal-body").find("input").each(function(){
            	if($(this).attr("name")!=undefined){
            		param[$(this).attr('name')]=$(this).val();
            	}
            })
            $.getJSON(operationUrl,param,function(data){
            	$('#saveModal').modal('hide');
            	if(data.sig==false){
            		$('#warnmsg').html(data.msg);
            		$('#warnModal').modal('show');
            	}else{
            		//成功新建任务，自动关闭浮层
            		$('#warnmsg').html(data.msg);
            		$('#warnModal').modal('show');
            		window.setTimeout(function(){
            			$('#warnModal').modal('hide');
            			$('#setModal').modal('hide');
            			//刷新任务列表
                		getSceneList();
            		}, 2000)
            		
            	}
            })
    	}
    });
    $('#saveModal').on('show.bs.modal', function () {
	   $('#setModal').css("z-index","100");
	})
    $('#saveModal').on('hide.bs.modal', function () {
	   $('#setModal').css("z-index","1050");
	})
    
});

//获取url中的参数
function getUrlParam(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
    var r = window.location.search.substr(1).match(reg);  //匹配目标参数
    if (r != null) return unescape(r[2]); return null; //返回参数值
}