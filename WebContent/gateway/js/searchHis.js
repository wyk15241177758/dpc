var table =null;
$(document).ready(function() {

	// 头部的入口链接地址增加random，避免缓存
	$(".top-menu a").each(function() {
		$(this).attr("href", $(this).attr("href") + "?rand=" + Math.random());
	})

	
	//新建按钮
    $("#a_add").click(function(){
    	$("#editModal").modal("show");
    })
    //保存按钮
    $("#editModal").find(".btn-save").click(function(){
		addSearchHis();
	})
	
	//修改按钮
	$(document).on("click","[action='edit']",function(){
		//给弹出层赋值
		var curId=table.row( $(this).parent("td").parent("tr") ).data()[0]
		setSearchHisValue(curId);
	})
	
	//删除按钮
	$(document).on("click","[action='del']",function(){
		var curSearchHisId=table.row( $(this).parent("td").parent("tr") ).data()[0]
		var curSearchHisVal=table.row( $(this).parent("td").parent("tr") ).data()[1]
		$("#warnModal-del").find("[name='searchHisId']").val(curSearchHisId);
		$("#warnmsg-sceneWord-del").html("确定要删除检索历史["+curSearchHisVal+"]吗？删除后无法恢复")
		$("#warnModal-del").modal('show')
	})
	//删除提示浮层按钮绑定事件
    $("#delConfirm").click(function(){
    	var curSearchHisId=$("#warnModal-del").find("[name='searchHisId']").val();
    	$("#warnModal-del").show("hidden");
    	deleteSearchHis(curSearchHisId);
    })
    //设置列宽，排序等
	$(".table-bordered").DataTable( {
		"searching":false,
		"order":[[3,"desc"]],
		  "columns": [
		     { "orderable": false },
		     { "orderable": false },
		     { "orderable": true },
		     { "orderable": true },
		    { "width": "22%" ,"orderable": false }
		  ],
		    "processing": true,
		    "serverSide": true,
		    "ajax": "/QASystem/admin/searchHis/listSearchHis.do",
		    "columnDefs": [
		                   {
		                     "data": null,
		                     "defaultContent": "<a class='btn btn-info btn-sm btn-setting' href='#' action='edit'> <i class='glyphicon glyphicon-edit icon-white'></i> 编辑 </a>  <a class='btn btn-danger btn-sm btn-warn' href='#'  action='del'>     <i class='glyphicon glyphicon-trash icon-white'></i> 删除 </a>",
		                     "targets": 4
		                   },
		                   {
		                	   "targets": 0,
		                	   "visible":false
		                   }
		                 ]
		}
	);
	
	table = $('.table-bordered').DataTable();

	 
//	$('.table-bordered').on( 'click', 'tr', function () {
//	    console.log( table.row( this ).data() );
//	} );
});


//删除
function deleteSearchHis(searchHisId){
	var param={"searchHisId":searchHisId};
	if(searchHisId!=undefined){
		  $.getJSON("/QASystem/admin/searchHis/delSearchHis.do",param,function(data){
				if (data.sig == false) {
					$('#warnmsg').html(data.msg);
					$('#warnModal').modal('show');
				} else {
					// 成功新建任务，自动关闭浮层
					$('#warnmsg').html(data.msg);
					$('#warnModal').modal('show');
					window.setTimeout(function() {
						$('#warnModal').modal('hide');
						// 刷新列表
						refreshList();
					}, 2000)
				}
		  })
	}
}

//新增或修改
function addSearchHis(){
	var operationUrl = "/QASystem/admin/searchHis/saveOrUpdateSearchHis.do"
		if (!paramCheck($("#editModal"))) {
			// paramCheck中处理
		} else {
			$('#saveModal').modal('show');
			var param = {};
			$("#editModal").find("input").each(function() {
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
						$('#editModal').modal('hide');
						//清空浮层中的数据
						$("#editModal").find("input").val("");
						// 刷新场景列表
						refreshList();
					}, 2000)
				}
			})
		}
}
// 刷新
function refreshList() {
	$('.table-bordered').dataTable().fnDraw();
//	var pageSize=1000;
//	var pageIndex=0
//	// 清空右侧数据
//	var table=$(".table-bordered").DataTable();
//	table.clear().draw();
//	var param = {
//		"pageSize" : pageSize,
//		"pageIndex":pageIndex,
//		"random" : Math.random()
//	};

//	$.getJSON("/QASystem/admin/searchHis/listSearchHis.do", param, function(data) {
//
//		
//		for (i in data) {
//			var curRowNode=table.row.add([data[i].searchContent,data[i].searchTimes,data[i].createTime," <a class=\"btn btn-info btn-sm btn-setting\" href=\"#\" action='edit'> 	" +
//			        					"<i class=\"glyphicon glyphicon-edit icon-white\"></i> 编辑 </a>" +
//			        					" <a class=\"btn btn-danger btn-sm btn-warn\" href=\"#\"  action='del'>" +
//			        					"<i class=\"glyphicon glyphicon-trash icon-white\"></i> 删除 </a> "])
//			.draw()
//		    .node();
//			//新增行变色突出
//			$( curRowNode )
//		    .css( 'color', 'black' )
//		    .animate( { color: 'black' },500 );
//			//新增行增加属性
//			$( curRowNode ).attr("searchHisId",data[i].id);
//			$( curRowNode ).attr("searchContent", data[i].searchContent);
//			//增加操作按钮
//			
//		}
//		
////		$('.table-bordered').DataTable();
//	})

}

//给弹出浮层赋值
function setSearchHisValue(searchHisId){
	//先清空现有的输入框内容
	$("#editModal").find("input").each(function(){
		$(this).val("");
	})
	//正在修改的ID
	$('#editModal').find("[name='searchHisId']").val(searchHisId);
	var param={"searchHisId":searchHisId};
	 $.getJSON("/QASystem/admin/searchHis/getSearchHis.do",param,function(data){
		  if(data==null||typeof(data.sig)=='undefined'
			  ||typeof(data.msg)=='undefined'){
				$('#warnmsg').html("获得ID=["+param.searchHisId+"]的搜索历史信息失败");
				$('#warnModal').modal('show');
		  }else if(data.sig==false){
			$('#warnmsg').html(data.msg);
     		$('#warnModal').modal('show');
		  }else{
			  var msg=data.msg;
			  for(i in msg){
				  $("input[name='"+i+"']").val(msg[i]);
			  }
  			  //显示设置浮层
  			 $('#editModal').modal('show');
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
