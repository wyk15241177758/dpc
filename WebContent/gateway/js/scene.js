var curSceneId=0;

$(document).ready(function () {
	
	//头部的入口链接地址增加random，避免缓存
	$(".top-menu a").each(function(){
		$(this).attr("href",$(this).attr("href")+"?rand="+Math.random());
	})
	//显示左侧的场景
	leftScene();
	
	
	
	
	$(document).on("click",".ajax-link",function(){
		sceneClick($(this));
	})
	
});
//点击场景
function sceneClick(obj){
	//修改当前场景ID
	curSceneId=obj.children("i").attr("sceneId");
	obj.parent("li").addClass("active").siblings().removeClass("active");
	//清空右侧数据
	 $(".table-bordered tbody").empty();
	//修改右侧数据
	var template="<tr sceneId="+curSceneId+" sceneWordId={sceneWordId}><td>{enterWord}</td><td>{outWord}</td><td>{createTime}</td><td class=\"center font-right\"><a class=\"btn btn-success btn-sm\" href=\"#\"><i class=\"glyphicon glyphicon-zoom-in icon-white\"></i>查看日志</a> <a class=\"btn btn-info btn-sm btn-setting\" href=\"#\"> 	<i class=\"glyphicon glyphicon-edit icon-white\"></i>编辑 </a> <a class=\"btn btn-danger btn-sm btn-warn\" href=\"#\"><i class=\"glyphicon glyphicon-trash icon-white\"></i>删除 </a> </td> </tr>";
	var param={"sceneId":curSceneId,"random":Math.random()};
	$.getJSON("/QASystem/admin/scene/listSceneWords.do",param,function(data){
		var cur="";
		 for(i in data){
			 cur+=template.replace(/\{sceneWordId\}/g,data[i].sceneWordId).replace(/\{enterWord\}/g,data[i].enterWords).replace(/\{outWord\}/g,data[i].outWords).replace(/\{createTime\}/g,data[i].createTime);
		 }
		 $(".table-bordered tbody").append(cur);
		 $('.table-bordered').DataTable();
	 })
	 
}

//左侧场景
function leftScene(){
	var template="<li><a href=\"javascript:void(0)\" class=\"ajax-link\">     <i name=\"leftword\" sceneId={sceneId} sceneName={sceneName}></i>{sceneName}</a></li>"
	 $.getJSON("/QASystem/admin/scene/listScenes.do",null,function(data){
		 for(i in data){
			 var cur=template.replace(/\{sceneId\}/g,data[i].sceneId).replace(/\{sceneName\}/g,data[i].sceneName);
			 $("#leftScene").append(cur);
		 }
	 }) 
}