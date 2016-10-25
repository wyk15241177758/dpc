$(function(){
	//竖向tab选项卡
     var $tab_li = $(".tab_title ul li");
     $tab_li.click(function(){
	    $(this).addClass("slected").siblings().removeClass("slected");
	    var index = $tab_li.index(this);
		 $(".tab_content > div").eq(index).show().siblings().hide();
	})   
        
    //智能查询tab选项卡
     var $tab2_li = $(".tab2_title ul li");
     $tab2_li.mouseover(function(){
	    $(this).addClass("slected2").siblings().removeClass("slected2");
	    var index = $tab2_li.index(this);
		 $(".tab2_content > div").eq(index).show().siblings().hide();
	})

    //显示滚动条
     $("#message").niceScroll({cursorborder:"",cursorcolor:"#9e0001",cursoropacitymax:0.5,boxzoom:true});

    //可输入字数
	function checkInputCount() {
		var maxLen = 100;	//定义用户可以输入的最多字数
		if (this.value.length > maxLen) {
			this.value = this.value.substring(0, maxLen);	//就去掉多余的字
			$("#number strong").html('超啦').css('color','#fe5325');
		} else {
			$("#number strong").html(maxLen - this.value.length);
		}
	}
	//控制默认提示内容
	var dvalue = "简单输入，精准信息即刻展现~";
	function controlInput_clear() {
		if (this.value && this.value == dvalue) {
			this.value = '';
		} 
	}
	function controlInput_init() {
		if (!this.value) {
			this.value = dvalue;
		} 
	}
	$('#messCon').bind({
		keyup: checkInputCount,
		keydown: checkInputCount,
		focus: controlInput_clear,
		blur: controlInput_init
	});
	
	//使用帮助轮播图
	$(".slideBox").slide({mainCell:".bd ul",autoPlay:true,delayTime:1000});
	
	//发送信息
  $("#sendMess").click(function(){
  	var mess = $.trim($("#messCon").val());
  	if(mess == ""){
  		return false;
  	}else{
  		//$("#message").append("<div>" + mess + "</div>");
  		$("#messCon").val("");
  		qaSearch(mess)
  	}
  })


})

function qaSearch(question){
	var param={
			"question":question,
			"begin":0,
			"end":5};
	$.getJSON("/QASystem/admin/qaSearch.do",param,function(data){
		addQuestion(question);
		addAnswer(data);
		
	})
	
}
//追加问题
function addQuestion(question){
	var template="  <ul class='dialog'> <li class='right'></li> <li class='center'>{question}</li> <li class='left'></li> <li class='bottom'></li> </ul>";
	template=template.replace(/\{question\}/g,question);
	$("#message").append(template);
}
function addAnswer(qalist){
	var template="<div class='dialog_tab'><div class='tab2_title'><ul>{categoryLi}</ul></div><div class='tab2_content'>{qaDiv}</div></div>";
	
	var answerArray=new Array();
	//遍历分类，将答案放入对应分类的answerArray中
	for(i=0;i<categoryArray.length;i++){
		var curCategory={"name":categoryArray[i],"qa":new Array()};
		for(j in qalist.msg){
			if(categoryArray[i]==qalist.msg[j].category){
				curCategory.qa.push(qalist.msg[j]);
			}
		}
		if(curCategory.qa.length!=0){
			answerArray.push(curCategory);
		}
	}
	//遍历归类好的答案并显示
	var categoryLi="";
	var qaDiv="";
	
	for(i=0;i<answerArray.length;i++){
		var curQa=""
		//遍历这个分类下的答案
		for(j=0;j<answerArray[i].qa.length;j++){
			curQa+="<li><a href='"+answerArray[i].qa[j].url+"' target='_blank'>"+answerArray[i].qa[j].title+"</a></li>"
		}
		
		//遍历分类
		if(i==0){
			categoryLi="<li class='slected2'>"+answerArray[i].name+"</li>"
			qaDiv="<div class='container'> <ul class='news'> "+curQa+" </ul> </div>";
		}else{
			categoryLi+="<li>"+answerArray[i].name+"</li>"
			qaDiv+="<div class='container hide'> <ul class='news'> "+curQa+" </ul> </div>";
		}
	}
	
	template=template.replace(/\{categoryLi\}/g,categoryLi);
	template=template.replace(/\{qaDiv\}/g,qaDiv);
	$("#message").append(template);
}
//横向tab选项卡
function setTab(m,n){
	var tli=document.getElementById("menu"+m).getElementsByTagName("li");
	var mli=document.getElementById("main"+m).getElementsByTagName("ul");
	for(i=0;i<tli.length;i++){
	   tli[i].className=i==n?"hover":"";
	   mli[i].style.display=i==n?"block":"none";
	}
}  