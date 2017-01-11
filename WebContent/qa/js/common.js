
$(function(){
	
	  $(document).on("mouseover",".tipDiv li",function(){
		  $(this).addClass("tipLiActive").siblings("li").removeClass("tipLiActive")
	  })
	
	  $(document).on("click",".tipDiv li",function(){
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
		qaSearch($(this).text());
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
	//发起N次请求，N为config.js中分类的个数
	var qaData={"msg":new Array()};
	//记录当前Ajax执行的个数
	var ajaxPos=0;
	for(i=0;i<categoryArray.length;i++){
		var param=null;
		//最后一次请求才记录检索历史
		if((i+1)==categoryArray.length){
			param={
					"question":encodeURIComponent(question),
					"category":categoryArray[i],
					"begin":0,
					"end":5,
					"searchHisId":searchHisId,
					"isStorgeHis":"true"};
			
		}else{
			param={
					"question":encodeURIComponent(question),
					"category":categoryArray[i],
					"begin":0,
					"end":5};
		}
		
		$.getJSON("/QASystem/admin/qaSearch.do",param,function(data){
			ajaxPos++;
			qaData.msg=qaData.msg.concat(data.msg);
			if(ajaxPos==categoryArray.length){
				//最后一个ajax请求返回，显示答案，并移动到底部
				addAnswer(question,qaData);
				scrollToBottom();
			}
		})
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
function addAnswer(question,qalist){
	var template="<div class='dialog_tab'><div><img src='images/robot.png' class='answer_man' width='40' height='40'/><img src='images/right_arrow.gif' class='right_arrow'/><div class='right_arrow'></div><p>您的问题是：<span>{question}</span>,我为您找到了以下答案:</p><div class='tab2_title'><ul>{categoryLi}</ul></div><div class='tab2_content'>{qaDiv}</div></div></div>";
	
	template=template.replace(/\{question\}/g,question);
	
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
			if(answerArray[i].qa[j].url.indexOf("http")==-1){
				curQa+="<li><a href='http://"+answerArray[i].qa[j].url+"' target='_blank'>"+answerArray[i].qa[j].title+"</a></li>"
			}else{
				curQa+="<li><a href='"+answerArray[i].qa[j].url+"' target='_blank'>"+answerArray[i].qa[j].title+"</a></li>"
			}
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
	
    //获取url中的参数
    function getUrlParam(name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
        var r = window.location.search.substr(1).match(reg);  //匹配目标参数
        if (r != null) return unescape(r[2]); return null; //返回参数值
    }
}  