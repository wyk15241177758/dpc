var table =null;
$(document).ready(function() {

	// 头部的入口链接地址增加random，避免缓存
	$(".top-menu a").each(function() {
		$(this).attr("href", $(this).attr("href") + "?rand=" + Math.random());
	})

	//点击提示浮层的关闭按钮时刷新列表
	$("#warnModal").find(".btn-default").click(function(){
		refreshList();
	})
	//新建按钮
    $("#a_add").click(function(){
    	$("#editModal").modal("show");
    })
    //同步内存到数据库
    $("#a_sync").click(function(){
    	syncSearchHis();
    })
    
    //保存按钮
    $("#editModal").find(".btn-save").click(function(){
		addSearchHis();
	})
	
	//修改按钮
	$(document).on("click","[action='edit']",function(){
		//给弹出层赋值
		if($("table :checkbox:checked").length==0||$("table :checkbox:checked").length>1){
			alert("请勾选一条记录");
		}else{
			var curId=table.row($("table :checkbox:checked").parent("td").parent("tr") ).data()[0]
			setSearchHisValue(curId);
		}
	})
	
	//删除按钮
	$(document).on("click","[action='del']",function(){
		$("#warnmsg-sceneWord-del").html("确定要删除这些检索历史吗？删除后无法恢复")
		$("#warnModal-del").modal('show')
	})
	//删除提示浮层按钮绑定事件
    $("#delConfirm").click(function(){
    	var paramIds="";
    	var selectedRows=table.rows($("table :checkbox:checked").parent("td").parent("tr")).data();
    	
    	for(var i=0;i<selectedRows.length;i++){
    		if(paramIds.length==0){
    			paramIds=selectedRows[i][0];
    		}else{
    			paramIds+=","+selectedRows[i][0];
    		}
    	}
    	deleteSearchHis(paramIds);
    })
    //设置列宽，排序等
	$(".table-bordered").DataTable( {
		"searching":true,
		"order":[[4,"desc"]],
		  "columns": [
		     { "orderable": false },
		     { "orderable": false },
		     { "orderable": false,"width": "50%" },
		     { "orderable": true },
		     { "orderable": true }
		  ],
		    "processing": true,
		    "serverSide": true,
		    "ajax": "/QASystem/admin/searchHis/listSearchHis.do",
		    "columnDefs": [
		                   {
		                	   "targets": 0,
		                	   "visible":false
		                   },
		                   {
			                     "data": null,
			                     "defaultContent": "<input type='checkbox'>",
			                     "targets": 1
			                   }
		                 ],
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
		}
	);
	
	table = $('.table-bordered').DataTable();
		

    //全选/反选
	$("table :checkbox:first").change(function(){
	    $(this).closest("table")
	           .find(":checkbox:not(:first)")
	           .prop("checked", this.checked);
	});
//	$('.table-bordered').on( 'click', 'tr', function () {
//	    console.log( table.row( this ).data() );
//	} );
});


//删除
function deleteSearchHis(searchHisIds){
	var param={"searchHisIds":searchHisIds};
	if(searchHisIds!=undefined){
		  $.getJSON("/QASystem/admin/searchHis/delSearchHises.do",param,function(data){
			 $('#warnmsg').html(data.msg);
			 $('#warnModal').modal('show');
//				if (data.sig == false) {
//					$('#warnmsg').html(data.msg);
//					$('#warnModal').modal('show');
//				} else {
//					// 成功新建任务，自动关闭浮层
//					$('#warnmsg').html(data.msg);
//					$('#warnModal').modal('show');
//					window.setTimeout(function() {
//						$('#warnModal').modal('hide');
//						// 刷新列表
//						refreshList();
//					}, 2000)
//				}
		  })
	}
}

//同步内存中的检索历史到数据库
function syncSearchHis(){
	var operationUrl = "/QASystem/admin/searchHisRt/syncToDb.do"
	var param = {};
	$.getJSON(operationUrl, param, function(data) {
			$('#warnmsg').html(data.msg);
			$('#warnModal').modal('show');
	})
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
