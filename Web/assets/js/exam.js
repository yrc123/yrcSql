

//显示计时器窗口
function showTimeWindow(date){
		layer.open({
			type: 1 ,
			title: 0,				//['考试进行中',"color:white;text-align:center;border-bottom:none;background-color:black;border-radius:10px 10px 0 0;padding-right: 0px;padding-left: 0px;"],
			area: ['180px', '55px'],
			shade: 0,
			//maxmin:true,
			skin:'time-down',
			offset:['230px','1250px'] , 
			content:'<div id="timeBar" class="layui-icon layui-icon-notice"></div>',
			resize:false,
			closeBtn:0,
			move:'#timeBar',
			success:function(){
				setTime(date);
			}
		});

}
//设置倒计时
function setTime(date){
	$('#timeBar').countdown(date, function(event) {
		//$(this).html(event.strftime('%w weeks %d days %H:%M:%S'));
		$(this).html(event.strftime(' %H:%M:%S'));
	}).on('finish.countdown',function(){
		layer.msg("考试结束，自动提交",{
				shade:0.3,
				time:500
		});
		setTimeout(function(){
			submitExam();
		},500)
	})
}
//获取考试信息json
function getExam(){
	var data;
	$.ajax({
		url:"/api/getExam",
		async:false,
		type:"POST",
		data:null,
		dataType:"json",
		success:function(resp){
			data=JSON.parse(resp);
		},
		error:function(){
			layer.msg("服务器出错",{
				shade:0.3,
				time:500
			});
		}
	})
	//console.log(data);
	return data; 
};

//获取考试题目
function getQuestion(){
	var qArr = new Array();
	$(".questions").each(function(){
		if($(this).hasClass("template")==false){
			qArr.push($(this));
		}
	});
	return qArr;
}
//设置考试
function setExam(data){

	if($.cookie("examType")==0){
		$("#examType").text("正式考试");
	}if($.cookie("examType")==1){
		$("#examType").text("模拟考试");
	}
	
	//添加题目模板
	var addExam = $("#addExam");
	var type = [ $("#type0").html(), $("#type1").html(), $("#type2").html()];
	$(".questions").addClass("template");
	var tmp;

	for(i=0;i<3;i++){
		tmp=data.Qtype[i];
		for(j=0;j<tmp;j++){
			addExam.append(type[i]);
			if(i!=2 || j!=data.Qtype[i]-1)addExam.append("<hr class='layui-bg-green'>");
		}
	}

	//获取题目dom
	var qArr = getQuestion();

	//设置题面
	var num=0;
	var s=["单选","多选","判断"]
	for(i=0;i<3;i++){
		tmp=data.Qtype[i];
		for(j=0;j<tmp;j++){
			qArr[num].children(".q-number").text(num+1);
			qArr[num].children(".q-title").children("span").text(s[i]);
			qArr[num].children(".q-title").append(data.data[num].title);
			var qNum=0;
			var node = qArr[num].children(".q-select").children("div");
				node.each(function(){
					if(i<2)
						$(this).children("input").attr("title",data.data[num].choice[qNum++]);
					$(this).children("input").attr("name","q-"+num);
				})
			num++;
		}
	}
	form.render();
}	

//检查考试是否开始
function checkExam(){
	var examType = $.cookie("examType");
	layui.use('layer', function(){
		$.ajax({
			type:"POST",
			url:"/api/examStart",
			dataType:"json",
			data:JSON.stringify({
				"examType":examType
			}),
			async:false,
			success:function(resp){
				var data=JSON.parse(resp);
				if(data["examStart"]==0){
					layer.msg("考试还未开始",{
				shade:0.3,
				time:500
					});
				}
			},
			error:function(){
				layer.msg("服务器出错",{
				shade:0.3,
				time:500
				});
			}
		})
	});
}
//获取input
function getInput(data){

	var qArr = getQuestion();

	var inputArr = new Array();
	var tmp;
	var num=0;
	for(i=0;i<3;i++){
		tmp=data.Qtype[i];
		for(j=0;j<tmp;j++){
			var inputArrTmp = new Array();
			var node = qArr[num].children(".q-select").children("div");
			node.each(function(){
				inputArrTmp.push($(this).children("input"));
			})
			num++;
			inputArr.push(inputArrTmp);
		}
	}
	return inputArr;
}
//获取学生的答案
function getAns(data){

	var ans={} ;
	
	ans.Qtype=data.Qtype;
	
	var inputArr = getInput(data);
	var tmp;
	var Qnum=0;
	var tmpArr = new Array();
	for(i=0;i<3;i++){
		tmp=data.Qtype[i];
		for(j=0;j<tmp;j++){
			var sum = 0;
			var item;
			if(i<2)item=4;
			else item=2;
			for(z=0;z<item;z++){
				if(inputArr[Qnum][z].is(":checked")){
					sum+=parseInt(inputArr[Qnum][z].attr("value"));
				}
			}
			tmpArr.push(sum);
			Qnum++;
		}
	}
	ans.data=tmpArr;
	return ans;
}

//设置正确答案
function setAns(ans,data){

	var inputArr = getInput(data);
	var Qnum=0;
	var tmp;
	//console.log(ans);
	for(i=0;i<3;i++){
		tmp=data.Qtype[i];
		for(j=0;j<tmp;j++){
			//console.log(tmp);
			var item;
			if(i<2)item=4;
			else item=2;
			for(z=0;z<item;z++){
				//console.log(Qnum+" "+inputArr[Qnum][z].attr("value"));
				if(inputArr[Qnum][z].is(":checked")){
					if((ans.data[Qnum]&parseInt(inputArr[Qnum][z].attr("value")))==0){
						inputArr[Qnum][z].parent().css("background-color","#f6bbbb")
					}
				}
				if((ans.data[Qnum]&parseInt(inputArr[Qnum][z].attr("value")))!=0){
					inputArr[Qnum][z].parent().css("background-color","#e8f7d2")
				}
			}	
			Qnum++;
		}
	}
}
//提交考试
function submitExam(){
	var ans=getAns(examInfo);
	console.log(form.val("examForm"));
	$.ajax({
		type:"POST",
		url:"/api/submitExam",
		dataType:"json",
		data:JSON.stringify(ans),
		async:false,
		success:function(resp){
			layer.msg("提交成功",{
				shade:0.3,
				time:500
			});
			if(resp==null){
				setTimeout(function(){
					location.href="./student.html"
				},500);
			}else{
				var ans=JSON.parse(resp);
				$('#timeBar').countdown('stop')
				setTimeout(function(){
					setAns(ans,examInfo);
					layer.closeAll();
					goSide(0);
				},500);
			}
		},
		error:function(){
			layer.msg("服务器出错",{
				shade:0.3,
				time:500
			});
		}
	})
	return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
}

//提交按钮
form.on('submit', function(){
	return submitExam();
});

//保存已选到cookie
function saveCookie(no,value){
	choiceInCookie.examID=examInfo.examID;
	choiceInCookie.choice[no]=value;	
	$.cookie("choice",JSON.stringify(choiceInCookie));
}
//加载cookie中的选项
function loadChoice(){
	if($.cookie("choice")==null)return false;
	console.log(examInfo.examID);
	//console.log(choiceInCookie);
	console.log(JSON.parse($.cookie("choice")));
	choiceInCookie=JSON.parse($.cookie("choice"));
	if(examInfo.examID!=choiceInCookie.examID){
		choiceInCookie.examID=examInfo.examID;
		choiceInCookie.choice=cArr;
		$.cookie("choice",JSON.stringify(choiceInCookie));
		console.log(choiceInCookie);
		return false;
	}
	setChoice();
}
//渲染cookie中的选项
function setChoice(){
	data=examInfo;
	var inputArr = getInput(data);
	var Qnum=0;
	var tmp;
	for(i=0;i<3;i++){
		tmp=data.Qtype[i];
		for(j=0;j<tmp;j++){
			var item;
			if(i<2)item=4;
			else item=2;
			for(z=0;z<item;z++){
				if((choiceInCookie.choice[Qnum]&parseInt(inputArr[Qnum][z].attr("value")))!=0){
					inputArr[Qnum][z].prop('checked',true);
				}
			}	
			Qnum++;
		}
	}
	form.render();
}

//监听radio
form.on('radio', function(data){
	var dom=data.elem;
	var no = Number(dom.name.substr(2));
	var value = dom.value;
	saveCookie(no,value);
});  

//监听checkbox
form.on('checkbox', function(data){
	var dom=data.elem;
	var no = Number(dom.name.substr(2));
	var value = choiceInCookie.choice[no];
	if(dom.checked){
		value+=parseInt(dom.value);
	}else{
		value-=parseInt(dom.value);
	}
	saveCookie(no,value);
});

//获取题目纵坐标
 function getTop(dom){
	var offset=dom.offsetTop;
	return offset;
 }
//滚动到题目
function scrollToExam(dom){
	var pos = getTop(dom);
	$("#main-info").scrollTop(pos);
}
