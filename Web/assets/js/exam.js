function getExam(){
	var data;
	$.ajax({
		url:"/api/getExam",
		async:false,
		success:function(resp){
			data=resp;
		},
		error:function(){
			layer.msg("服务器出错");
		}
	})
	return JSON.parse(data); 
};

function getQuestion(){
	var qArr = new Array();
	$(".questions").each(function(){
		if($(this).hasClass("template")==false){
			qArr.push($(this));
		}
	});
	return qArr;
}
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
}	

function checkExam(){
	var examType = $.cookie("examType");
	$.ajax({
		type:"POST",
		url:"/api/examStart",
		dataType:"json",
		data:{
			"examType":examType
		},
		async:false,
		success:function(resp){
			var data=JSON.parse(resp);
			if(data["examStart"]==0){
				layer.msg("考试还未开始");
				//location.href="./student.html";
			}
		},
		error:function(){
			layer.msg("服务器出错");
		}
	})
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

	var ans= new Array();

	ans.push(data.Qtype);
	
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
	ans.push(tmpArr);
	return ans;
}

//设置正确答案
function setAns(ans,data){

	var inputArr = getInput(data);
	var Qnum=0;
	var tmp;
	console.log(ans);
	for(i=0;i<3;i++){
		tmp=data.Qtype[i];
		for(j=0;j<tmp;j++){
			console.log(tmp);
			var item;
			if(i<2)item=4;
			else item=2;
			for(z=0;z<item;z++){
				console.log(Qnum+" "+inputArr[Qnum][z].attr("value"));
				if(inputArr[Qnum][z].is(":checked")){
					if((ans[Qnum][z]&parseInt(inputArr[Qnum][z].attr("value")))==0){
						inputArr[Qnum][z].parent().css("background-color","#f6bbbb")
					}
				}
				if((ans[1][Qnum]&parseInt(inputArr[Qnum][z].attr("value")))!=0){
					inputArr[Qnum][z].parent().css("background-color","#e8f7d2")
				}
			}	
			Qnum++;
		}
	}
}

//提交按钮
layui.use('form', function(){
	var form =layui.form;
	form.on('submit', function(){
		var ans=getAns(examInfo);
		$.ajax({
			type:"POST",
			url:"/api/submitExam",
			dataType:"json",
			data:JSON.stringify(ans),
			async:false,
			success:function(resp){
				if(resp==null){
					layer.msg("提交成功");
				}else{
					var ans=JSON.parse(resp);
					setAns(ans,examInfo);
				}
			},
			error:function(){
				layer.msg("服务器出错");
			}

		})
		return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
	});
});
