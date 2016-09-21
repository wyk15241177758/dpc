
function paramCheck(){
	var flag=true;
	//非空校验
	$("#setModal[required='true']").each(function(){
		if($(this).val().length()==0){
			$("warnmsg").text($(this).attr("desc")+"不能为空");
			flag=false;
			return false;
		}
	})
	if(flag){
		//数字校验
		$("#setModal[num='true']").each(function(){
			$(this).val(parseInt($(this).val()));
			if($(this).val()>$(this).attr("max")){
				$("warnmsg").text($(this).attr("desc")+"不能大于"+$(this).attr("max"));
				flag=false;
				return false;
			}
			
			if($(this).val()<$(this).attr("min")){
				$("warnmsg").text($(this).attr("desc")+"不能小于"+$(this).attr("mix"));
				flag=false;
				return false;
			}
		})
	}
	return flag;
}

function parseFields(){
	var datafield={isKey:false,type:1,name:1}
	var array=new Array();
	$("#data-syn .form-inline").each(function(){
		var name=$(this).children("[type=text]").val();
		var type=$(this).children("select").val();
		var isKey=$(this).children("[type=checkbox]").val()=="checked"?true:false;
		if(name.length==0){
			return;
		}
		datafield.name=name;
		datafield.type=type;
		datafield.isKey=isKey;
		array.push(datafield);
	})
	if(array.length!=0){
		$("fields_mysql").val(JSON.stringify(array));
	}
}

$(document).ready(function () {
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
    		 $('#warnModal').modal('show');
    	}else{
    		$('#saveModal').modal('show');
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
								'<label><input type="checkbox" value="">主键</label>'+
							'</div>'+
	                        '<div class="form-group data-syn">'+
			                    '<input type="text" class="form-control">'+
			                    '<select class="form-control">'+
								  '<option>存储</option>'+
								  '<option>检索</option>'+
								'</select>'+
			                '</div>'+
			            '</div>	';
    	$("#data-syn").append(dataset)
    })
    
});