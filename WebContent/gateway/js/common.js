
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
					$("#warnmsg").text();
					alert($(this).attr("desc")+"不能大于"+$(this).attr("max"));
					$(this).focus();
					flag=false;
					return false;
				}
			}
			if($(this).attr("min")!=undefined){
				if($(this).val()<$(this).attr("min")){
					alert($(this).attr("desc")+"不能小于"+$(this).attr("mix"));
					$(this).focus();
					flag=false;
					return false;
				}
			}
		})
	}
	return flag;
}

function parseFields(){
	var datafield={isKey:false,type:1,name:1}
	var array=new Array();

	$("#data-syn").children(".form-inline").each(function(){
		
		var name=$(this).find("[type='text']").val();
		var type=$(this).find("select").val();
		var isKey=$(this).find("[type='checkbox']").prop("checked")?true:false;
		if((name+"").length==0){
			return;
		}
		var datafield={isKey:isKey,type:type,name:name}
		array.push(datafield);
	})
	//console.log(array);
	if(array.length!=0){
		$("#fields_mysql").attr("value",JSON.stringify(array));
	}
}
function toggleJob(jqueryObj){
	var curStatus=jqueryObj.attr("jobstatus");
	if(curStatus=='未启动'){
		jqueryObj.attr("jobstatus","已启动");
		jqueryObj.children("td:nth-child(2)").text("已启动");
		jqueryObj.children("td:nth-child(2)").children("span").removeClass("label-warning");
		jqueryObj.children("td:nth-child(2)").children("span").addClass("label-success");
		jqueryObj.children("td:nth-child(3)").text("启动任务");
	}else{
		jqueryObj.attr("jobstatus","未启动");
		jqueryObj.children("td:nth-child(2)").text("未启动");
		jqueryObj.children("td:nth-child(2)").children("span").removeClass("label-success");
		jqueryObj.children("td:nth-child(2)").children("span").addClass("label-warning");
		jqueryObj.children("td:nth-child(3)").text("停止任务");
	}
}
$(document).ready(function () {
	//启动停止任务
	$(document).on("click","[startOrStop_a]",function(){
		var cur_tr=$(this).parents("tr")
		var url="/QASystem/admin/startJob.do"
		if(cur_tr.attr("jobstatus")=='未启动'){
			$.getJSON(url,{jobid:cur_tr.attr("jobid")},function(data){
				if(data.sig==true){
					toggleJob(cur_tr)
				}else{
					$("#warnmsg").html(data.msg);
				}
			})
		}
	})
	
	//获得任务列表
	var list_template="<tr jobname='{name}' jobid='{jobid}' jobstatus='{status}'>" +
			"<td>{name}</td>" +
			"<td class='center'><span class='label {status_class}'>{status}</span></td>" +
			"<td class='center font-right'>" +
			"<a class='btn btn-success btn-sm' startOrStop_a='true' href='#'>" +
			"<i class='glyphicon glyphicon-zoom-in icon-white'></i>{startOrStop}</a>" +
			"<a class='btn btn-success btn-sm' href='#'>" +
			"<i class='glyphicon glyphicon-zoom-in icon-white'></i>查看日志</a>" +
			"<a class='btn btn-info btn-sm btn-setting' href='#'>" +
			"<i class='glyphicon glyphicon-edit icon-white'></i>编辑</a>" +
			"<a class='btn btn-danger btn-sm btn-warn' href='#'>" +
			"<i class='glyphicon glyphicon-trash icon-white'></i>删除</a>" +
			"</td></tr>";
    $.getJSON("/QASystem/admin/listJobs.do",null,function(data){
    	if(data.length==0){
    		$("#tbody_joblist").append("<td colspan='3'>暂无任务</td>");
    	}
    	
    	for(job in data){
    		var cur_td="";
    		cur_td=list_template.replace(/\{name\}/g,data[job].jobName);
    		cur_td=cur_td.replace(/\{jobid\}/g,data[job].jobId);
    		if(data[job].jobStatus==1){
    			cur_td=cur_td.replace(/\{startOrStop\}/g,"启动任务");
    			cur_td=cur_td.replace(/\{status\}/g,"未启动");
    			cur_td=cur_td.replace(/\{status_class\}/g,"label-warning");
    			
    		}else{
    			cur_td=cur_td.replace(/\{startOrStop\}/g,"停止任务");
    			cur_td=cur_td.replace(/\{status\}/g,"已启动");
    			cur_td=cur_td.replace(/\{status_class\}/g,"label-success");
    		}
    		$("#tbody_joblist").append(cur_td);
    	}
    })
	
	
    //设置浮层弹出
    $('.btn-setting').click(function (e) {
        e.preventDefault();
        $('#setModal').modal('show');
    });
    //提示浮层弹出
    $('.btn-warn').click(function (e) {
        e.preventDefault();
        $('#warnModal').modal('show');
        
    });
    //保存浮层弹出
    $('.btn-save').click(function (e) {
    	e.preventDefault();
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
//            console.log(param)
            $.getJSON("/QASystem/admin/addJob.do",param,function(data){
            	$('#saveModal').modal('hide');
            	//console.log(data)
            	//console.log(data.sig==false)
            	if(data.sig==false){
            		$('#warnmsg').html(data.msg);
            		$('#warnModal').modal('show');
            	}else{
            		$('#warnmsg').html(data.msg);
            		$('#warnModal').modal('show');
            		window.setTimeout(function(){
            			$('#warnModal').modal('hide');
            			$('#setModal').modal('hide');
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