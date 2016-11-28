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


//刷新所有任务的状态、执行情况、执行时间、推送数据量
function refreshJobLog(){
	//任务数量
	var jobNum=$("#tbody_joblist").children("tr").length;
	//已刷新日志个数，用于所有任务执行完成后更换图片的功能
	var jobRefreshNum=0;
	$("#tbody_joblist").children("tr").each(function(){
		//没有任务则结束循环
		if($(this).html()=="<td colspan='6'>暂无任务</td>"){
			return false;
		}
		var param={"jobid":$(this).attr("jobid")};
		 $.getJSON("/QASystem/admin/getRunningLog.do",param,function(data){
			 if(data.sig==true){
				 if(typeof(data.msg)!='undefined'){
					 var msgArray=data.msg;
					 if((data.msg+"").indexOf("结束执行任务")==-1){
						$("#tbody_joblist").children("tr[jobid='"+param.jobid+"']").children("td:nth-child(2)").html("<span class='label-success label'>正在执行</span>")
					 }else{
						$("#tbody_joblist").children("tr[jobid='"+param.jobid+"']").children("td:nth-child(2)").html("<span class='label label-warning'>未执行</span>")
					 }
					 //未选择查看日志，则默认显示第一个任务的日志
					 var curTR=$("#tbody_joblist").children("tr[jobid='"+param.jobid+"']")
					 var curTRJobName=curTR.attr("jobname")
					 var curTRJobId=curTR.attr("jobid")
					 if(curJobId==0){
						 //第一个TR
						 if($("#tbody_joblist").children("tr").index(curTR)==0){
							 $("ul[role='tablist']").find("strong").text("任务日志["+curTRJobName+"]");
							 //先清空日志，再输出
							 $("#log").html("");
							 for(i in msgArray){
								 $("#log").append((parseInt(i)+1)+"."+msgArray[i]+"<br/>")
							 }
						 }
					 }else{
						 //显示指定的任务日志
						 if(curJobId==curTRJobId){
							 $("ul[role='tablist']").find("strong").text("任务日志["+curTRJobName+"]");
							 //先清空日志，再输出
							 $("#log").html("");
							 for(i in msgArray){
								 $("#log").append((parseInt(i)+1)+"."+msgArray[i]+"<br/>")
							 }
						 }
						 
					 }
				 }
			 }
			  
		  })
		
	 })
}

//获得场景列表
function getSceneList(){
    $.getJSON("/QASystem/admin/listScenes.do",null,function(data){
    	$("#tbody_joblist").html("");
    	if(data.length==0){
    		$("#tbody_joblist").append("<tr><td colspan='6'>暂无任务</td></tr>");
    	}
    	
    	for(job in data){
    		var cur_td="";
    		cur_td=list_template.replace(/\{jobname\}/g,data[job].jobName);
    		cur_td=cur_td.replace(/\{jobid\}/g,data[job].jobId);
    		if(data[job].jobStatus==1){
    			cur_td=cur_td.replace(/\{jobstatus\}/g,"未执行");
    			cur_td=cur_td.replace(/\{status_class\}/g,"label-warning");
    		}else{
    			cur_td=cur_td.replace(/\{jobstatus\}/g,"正在执行");
    			cur_td=cur_td.replace(/\{status_class\}/g,"label-success");
    		}
    		$("#tbody_joblist").append(cur_td);
    	}
    })
}
//删除任务
function deleteJob(curTrJobId){
	var param={"jobid":curTrJobId};
	if(curTrJobId!=undefined){
		  $.getJSON("/QASystem/admin/delJob.do",param,function(data){
			  if(data==null||typeof(data.sig)=='undefined'
				  ||typeof(data.msg)=='undefined'){
					$('#warnmsg').html("删除jobid=["+param.jobid+"]的任务信息失败");
          		$('#warnModal').modal('show');
			  }else{
				$('#warnmsg').html(data.msg);
        		$('#warnModal').modal('show');
			  }
			  getJobList();
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
	
	//获得任务列表
	getJobList();
	
    //点击刷新任务日志
    $("#refresh_a").click(function(){
    	refreshJobLog();
    })
    //每隔5s自动刷新
    window.setInterval("refreshJobLog()", 5000);
	
    //新建任务
    $('.btn-setting').click(function (e) {
        e.preventDefault();
        //先清空现有的输入框内容
    	$("#setModal").find("input").each(function(){
    		$(this).val("");
    	})
    	//任务名称改成可以编辑
    	$("input[name='taskname']").removeAttr("disabled");
    	//改为新增模式
    	$("input[name='saveOrUpdate']").val("save");
        $('#setModal').modal('show');
    });
    //提示浮层弹出
    $('.btn-warn').click(function (e) {
        e.preventDefault();
        $('#warnModal').modal('show');
        
    });
    
    //点击立即启动按钮，修改全局变量curJobId为点击的jobid，且立即启动
    $(document).on("click","a[action='startImmediate']",function(){
    	var curTrJobId=$(this).parents("tr").attr("jobid");
    	var param={"jobid":curTrJobId};
    	$.getJSON("/QASystem/admin/startImmediate.do",param,function(data){
    		//nothing to do
    	});
    	if(curTrJobId!=undefined){
    		curJobId=curTrJobId;
    		console.log("修改curJobId为"+curJobId)
    		refreshJobLog();
    	}
    })
    
    
    
    //点击查看日志按钮，修改全局变量curJobId为点击的jobid
    $(document).on("click","a[action='joblog_a']",function(){
    	var curTrJobId=$(this).parents("tr").attr("jobid");
    	if(curTrJobId!=undefined){
    		curJobId=curTrJobId;
    		console.log("修改curJobId为"+curJobId)
    		refreshJobLog();
    	}
    })
    
    //点击编辑
    $(document).on("click","a[action='edit_a']",function(){
    	//先清空现有的输入框内容
    	$("#setModal").find("input").each(function(){
    		$(this).val("");
    	})
    	//改为编辑模式
    	$("input[name='saveOrUpdate']").val("update");
    	var curTrJobId=$(this).parents("tr").attr("jobid");
    	var param={"jobid":curTrJobId};
    	if(curTrJobId!=undefined){
    		  $.getJSON("/QASystem/admin/getJobAndFields.do",param,function(data){
    			  if(data==null||typeof(data.sig)=='undefined'
    				  ||typeof(data.msg)=='undefined'||typeof(data.msg.jobinf)=='undefined'
    					  ||typeof(data.msg.config)=='undefined'){
    					$('#warnmsg').html("获得jobid=["+param.jobid+"]的任务信息失败");
                		$('#warnModal').modal('show');
    			  }else if(data.sig==false){
    				$('#warnmsg').html(data.msg);
              		$('#warnModal').modal('show');
    			  }else{
    				  //给弹出层赋值
					  var jobinf=data.msg.jobinf;
					  var config=data.msg.config;
					  //任务名称不允许修改
					  $("input[name='taskname']").attr("disabled","");
					  $("input[name='taskname']").val(jobinf.jobName);
	       			  $("input[name='indexpath']").val(jobinf.indexPath);
	       			  //分钟
	       			  $("input[name='internalmin']").val(jobinf.cronExpression.split(" ")[1].split("/")[1]);
	       			  //小时
	       			  if(jobinf.cronExpression.split(" ")[2]=='*'){
	       				  $("input[name='internalhour']").val(0);
	       			  }else{
	       				  $("input[name='internalhour']").val(jobinf.cronExpression.split(" ")[2].split("/")[1]);
	       			  }
	       			  
	       			  $("input[name='sqlip']").val(jobinf.sqlIp);
	       			  $("input[name='sqlport']").val(jobinf.sqlPort);
	       			  $("input[name='sqluser']").val(jobinf.sqlUser);
	       			  $("input[name='sqlpw']").val(jobinf.sqlPw);
	       			  $("input[name='sqldb']").val(jobinf.sqlDb);
	       			  $("input[name='sqltable']").val(jobinf.sqlTable);
	       			  //sql字段
	       			  $("#data-syn").html("");
	       			  for(i in config){
	       				 var dataset = '<div class="form-inline">'+
		                    '<div class="checkbox-inline" style="margin-right:4px">'+
								'<label><input type="checkbox" '+(config[i].tableKey?'checked':'')+'/>主键</label>'+
							'</div>'+
	                        '<div class="form-group data-syn">'+
			                    '<input type="text" class="form-control" value="'+config[i].name+'"/>'+
			                    '<select class="form-control">'+
								  '<option '+(config[i].type=='存储'?'selected':'')+'>存储</option>'+
								  '<option '+(config[i].type=='检索'?'selected':'')+'>检索</option>'+
								'</select>'+
			                '</div>'+
			            '</div>	';
	       				 $("#data-syn").append(dataset)
	       			  }
	       			  //显示设置浮层
	       			 $('#setModal').modal('show');
    			  }
    			  
    		  })
    	}
    });
    
    //删除任务
    $(document).on("click","a[action='delete_a']",function(){
    	var curTrJobId=$(this).parents("tr").attr("jobid");
    	var curTrJobName=$(this).parents("tr").attr("jobname");
    	$('#warnmsg-del').html("确定要删除任务["+curTrJobName+"]吗？删除后无法恢复");
    	$('#warnmsg-del').attr("deljobid",curTrJobId)
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
    	deleteJob($('#warnmsg-del').attr("deljobid"));
    })
    
    //保存浮层弹出
    $('.btn-save').click(function (e) {
    	e.preventDefault();
    	//新增或修改模式为不同的url
    	var operationUrl="";
    	if($("input[name='saveOrUpdate']").val()=="update"){
    		operationUrl="/QASystem/admin/updateJob.do";
    	}else{
    		operationUrl="/QASystem/admin/addJob.do";
    	}
    	parseFields();
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
            	//console.log(data)
            	//console.log(data.sig==false)
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
                		getJobList();
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
    //新建同步字段
    $(".data-add").click(function(){  	
	    var dataset = '<div class="form-inline">'+
		                    '<div class="checkbox-inline" style="margin-right:4px">'+
								'<label><input type="checkbox" />主键</label>'+
							'</div>'+
	                        '<div class="form-group data-syn">'+
			                    '<input type="text" class="form-control"/>'+
			                    '<select class="form-control">'+
								  '<option>存储</option>'+
								  '<option>检索</option>'+
								'</select>'+
			                '</div>'+
			            '</div>	';
	   $("#data-syn").append(dataset)
    	//$("#data-syn").append("<div class='form-inline'><div><input type='text'/></div></div>")
    })
    
});

//获取url中的参数
function getUrlParam(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
    var r = window.location.search.substr(1).match(reg);  //匹配目标参数
    if (r != null) return unescape(r[2]); return null; //返回参数值
}