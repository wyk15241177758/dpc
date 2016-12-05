

$(document).ready(function () {
	//头部的入口链接地址增加random，避免缓存
	$(".top-menu a").each(function(){
		$(this).attr("href",$(this).attr("href")+"?rand="+Math.random());
	})
	leftScene();
});


function leftScene(){
	var template="<li><a href=\"javascript:void(0)\" class=\"ajax-link\">     <i name=\"leftword\" sceneId={sceneId} sceneName={sceneName}></i>{sceneName}</a></li>"
	 $.getJSON("/QASystem/admin/scene/listScenes.do",null,function(data){
		 for(i in data){
			 var cur=template.replace(/\{sceneId\}/g,data[i].sceneId).replace(/\{sceneName\}/g,data[i].sceneName);
			 $("#leftScene").append(cur);
		 }
	 }) 
}