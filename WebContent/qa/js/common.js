//没有结果时显示内容
var noResult="未找到您要的信息，建议您进入“<a href='http://www.bing.com' target='_blank' style='text-decoration: underline;color: #d40a06; font-weight: bold;'>市长信箱</a>”栏目提交您要咨询的问题。"



$(function(){
	
	  $(document).on("mouseover",".tipDiv li",function(){
		  $(this).addClass("tipLiActive").siblings("li").removeClass("tipLiActive")
	  })
	
	  $(document).on("click",".tipDiv li",function(e){
	  	$("#messCon").val($(this).text());
	  	$(".tipDiv").css("display","none")
		stopPropagation(e) //停止dom事件层次传播
	  })
 	  $(document).on("click","body",function(){
 		 $(".tipDiv").css("display","none")
 	  })
 	  
 	  var showTipTimeOut;
	$("#messCon").keyup(function(){
		 
		clearTimeout(showTipTimeOut);
		var msg=$(this).val();
		showTipTimeOut=setTimeout(function(){
			$(".tipDiv").css("display","none")
			showTip();
		},500);
	})
	
	//左侧标签点击效果
	$("#main0 a").click(function(){
		qaSearchList($(this).text());
	})
	//竖向tab选项卡
     var $tab_li = $(".tab_title ul li");
     $tab_li.click(function(){
	    $(this).addClass("slected").siblings().removeClass("slected");
	    var index = $tab_li.index(this);
		 $(".tab_content > div").eq(index).show().siblings().hide();
	})   
	
	//智能查询tab选项卡
     $(document).on("mouseover",".tab2_title ul li",function(){
 	    $(this).addClass("slected2").siblings().removeClass("slected2");
 	    var index = $(this).parent().children().index(this)+1;
 	    $(this).parents(".tab2_title").siblings().children().eq(index).show().siblings().hide(); 
     })

    //显示滚动条
     $("#message").niceScroll({cursorborder:"",cursorcolor:"#9e0001",cursoropacitymax:0.5,boxzoom:false});

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
	//点击回车提交
	$("#messCon").keypress(function(event){
		 if (event.keyCode == '13') {
		     event.preventDefault();
			 $("#sendMess").click();
		   }
	})
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

var tipArr=new Array();

//显示提示浮层
function showTip(){
	var url="/QASystem/admin/luceneSearch_searchHis.do"
	var param={"question":$("#messCon").val(),"isParticle":"true","isShould":"true"}
	if($.trim($("#messCon").val()).length>0){
		$.getJSON(url,param,function(data){
			tipArr=new Array()
			var msg=data.msg;
			if(data.sig==true){
				var out="";
				for(var i=0;i<msg.length;i++){
					tipArr.push(msg[i]);
//					out+="<li ID='"+msg[i].ID+"' UPDATETIME='"+msg[i].UPDATETIME+"' SEARCHCONTENT='"+msg[i].SEARCHCONTENT+
//					"' SEARCHTIMES='"+msg[i].SEARCHTIMES+"' CREATETIME='"+msg[i].CREATETIME+"'>"+msg[i].SEARCHCONTENT+"</li>"
					out+="<li>"+msg[i].SEARCHCONTENT+"</li>"
				}
				if(msg.length>0){
					$(".tipDiv ul").html(out);
					$(".tipDiv").css("display","block");
				}
			}
		})
	}
	
}
//获得传入问题相同的提示，并将此提示ID作为参数传入后台
function getSimTip(question){
	for(var i=0;i<tipArr.length;i++){
		if(question==tipArr[i].SEARCHCONTENT){
			return tipArr[i].ID
		}
	}
}


//阻止事件气泡
function stopPropagation(e){
	e=window.event||e;
	if(document.all){
		e.cancelBubble=true;
	}else{
		e.stopPropagation();
	}
}


function qaSearch(question){
	//显示问题并移动到底部
	addQuestion(question);
	scrollToBottom();
	
	var searchHisId=getSimTip(question)+"";
	searchHisId=searchHisId.length==0?"0":searchHisId;
	var qaData={"msg":new Array()};
	var param={
			"question":encodeURIComponent(question),
			"begin":0,
			"end":5,
			"searchHisId":searchHisId,
			"isStorgeHis":"true"};
	$.getJSON("/QASystem/admin/qaSearch.do",param,function(data){
		addAnswer(question,data);
		scrollToBottom();
	})	
}
function qaSearchList(question){
var  url="/QASystem/qa/list.html";
 if(url.indexOf("?")!=-1){
	 window.open(url+"&word="+encodeURIComponent(question));

//window.location.href=url+"&word="+encodeURIComponent(question);
  }else{
		 window.open(url+"?word="+encodeURIComponent(question));

//window.location.href=url+"?word="+encodeURIComponent(question);
 }
}
function scrollToBottom(){
	$("#message").scrollTop(parseInt($("#message")[0].scrollHeight)-parseInt($("#message").css("height")));
}

//追加问题
function addQuestion(question){
	var template="  <ul class='dialog'> <img src='images/left_arrow.gif' class='left_arrow'/> <li class='center'>{question}</li>  </ul>";
	template=template.replace(/\{question\}/g,question);
	$("#message").append(template);
	//移动滚动条
	scrollToBottom();
}
function addAnswer(question,qaMsg){
	var template="<div class='dialog_tab'><div><img src='images/robot.png' class='answer_man' width='40' height='40'/><img src='images/right_arrow.gif' class='right_arrow'/><div class='right_arrow'></div><p>您的问题是：<span>{question}</span>,我为您找到了以下答案:</p><div class='tab2_title'><ul>{categoryLi}</ul></div><div class='tab2_content'>{qaDiv}</div></div></div>";
	
	template=template.replace(/\{question\}/g,question);
	var categoryLi="";
	var qaDiv="";
	
	var msg=qaMsg.msg;
	var index=0;
	if(qaMsg.sig){
		for(i in msg){
			var curQaLi="";
			var curQaDiv="";
			//遍历分类
			if(index==0){
				categoryLi="<li class='slected2'>"+i+"</li>"
				for(var j=0;j<msg[i].length;j++){
					//如果有预设页面html则优先展示
					if(msg[i][j].html!=undefined&&msg[i][j].html!=null&&msg[i][j].html.length>0){
						curQaDiv+=msg[i][j].html;
					}else{
						if(msg[i][j].url.indexOf("http")==-1){
							curQaLi+="<li><a href='http://"+msg[i][j].url+"' target='_blank'>"+msg[i][j].title+"</a></li>"
						}else{
							curQaLi+="<li><a href='"+msg[i][j].url+"' target='_blank'>"+msg[i][j].title+"</a></li>"
						}
					}
				}
				qaDiv="<div class='container'> "+curQaDiv+"<ul class='news'> "+curQaLi+" </ul> </div>";
			}else{
				categoryLi+="<li>"+i+"</li>"
				for(var j=0;j<msg[i].length;j++){
					//如果有预设页面html则优先展示
					if(msg[i][j].html!=undefined&&msg[i][j].html!=null&&msg[i][j].html.length>0){
						curQaDiv+=msg[i][j].html;
					}else{
						if(msg[i][j].url.indexOf("http")==-1){
							curQaLi+="<li><a href='http://"+msg[i][j].url+"' target='_blank'>"+msg[i][j].title+"</a></li>"
						}else{
							curQaLi+="<li><a href='"+msg[i][j].url+"' target='_blank'>"+msg[i][j].title+"</a></li>"
						}
					}
				}
				qaDiv+="<div class='container hide'>"+curQaDiv+" <ul class='news'> "+curQaLi+" </ul> </div>";
			}
			index++;
		}
	}else{
		qaDiv=noResult;
	}
	
	template=template.replace(/\{categoryLi\}/g,categoryLi);
	template=template.replace(/\{qaDiv\}/g,qaDiv);
	$("#message").append(template);
	//移动滚动条
	scrollToBottom();
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


//获取url中的参数
function getUrlParam(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
    var r = window.location.search.substr(1).match(reg);  //匹配目标参数
    if (r != null) return unescape(r[2]); return null; //返回参数值
}

var decToHex = function(str) {
    var res=[];
    for(var i=0;i < str.length;i++)
        res[i]=("00"+str.charCodeAt(i).toString(16)).slice(-4);
    return "\\u"+res.join("\\u");
}
var hexToDec = function(str) {
    str=str.replace(/\\/g,"%");
    return unescape(str);
}