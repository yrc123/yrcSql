var selectSetExamData;
//获取班级信息
//function getClassInExam(){
	//var data;
	//$.ajax({
		//type:"POST",
		//url:"/api/getClassInExam",
		//dataType:"json",
		//async:false,
		//success:function(resp){
			//data = JSON.parse(resp);
		//},
		//error:function(){
			//layer.msg("服务器出错",{
					//shade:0.3,
					//time:500
			//});
		//}
	//})
	////console.log(data);
	//return data;
//}
//设置班级表

table.render({
	elem:"#setExamTable",
	cols:[[
		{type: 'checkbox', fixed: 'left'},
		{field:"className",title:"班级名",width:"20%"},
		{field:"classStatusText",title:"考试状态",width:"20%",sort:true},
		{field:"examTimeText",title:"考试时间",width:"50%"},
		{fixed: 'right', title:'操作', toolbar: '#examBar', width:"100" ,unresize:true}
	]],
	url:"/api/getClassInExam",
	method:"POST",
	contentType:'application/json',
	parseData: function(res){ //res 即为原始返回的数据
		//console.log(res);
		res = JSON.parse(res);
		var data = {
			"code": 0, //解析接口状态
			"msg": res.message, //解析提示文本
			"count": res.total, //解析数据长度
		};
		var len = res.classNumber;
		for(i=0;i<len;i++){
			if(res.data[i].classStatus==true){
				res.data[i].classStatusText="考试已设定";
				res.data[i].examTimeText=res.data[i].examTime;
			}else{
				res.data[i].classStatusText="考试未设定";
				res.data[i].examTimeText="考试时间未定";
			}
		}
		data.data=res.data;
		//console.log(data);
		return data; 
	},
	toolbar:  '#examToolbar' , //开启头部工具栏，并为其绑定左侧模板
    defaultToolbar: ['filter', 'exports', 'print'],
	title:"班级考表",
	height: 'full-240',
});
//function setTable(data){
	//var len = data.classNumber;
	//var tableTemplate = $("#tableTemplate");
	//var classInfo = $("#classInfo");
	//classInfo.html("");
	//for(i=0;i<len;i++){
		//var tdata={
			//className:data.data[i].className,
		//};
		//if(data.data[i].classStatus==true){
			//tdata.classStatus="考试已设定";
			//tdata.examTime=data.data[i].examTime;
		//}else{
			//tdata.classStatus="考试未设定";
			//tdata.examTime="考试时间未定";
		//}
		//laytpl(tableTemplate[0].innerHTML).render(tdata,function(html){
			//classInfo.append(html);
		//})
	//}
//}
////设置select选项
//function setSelect(data){
	//var len = data.classNumber;
	//var selectTemplate = $("#selectTemplate");
	//var selectClass = $("#selectClass");
	////console.log(selectClass.html());
	//selectClass.html('<option value="" >请选择一个班级</option>');
	//for(i=0;i<len;i++){
		//var tdata={
			//className:data.data[i].className,
			//classID:data.data[i].classID,
		//};
		////console.log(tdata);
		//laytpl(selectTemplate[0].innerHTML).render(tdata,function(html){
			////console.log(html);
			//selectClass.append(html);
		//})
	//}
	//form.render('select');
//}
////监听选择班级
//form.on('select(selectInSetExam)', function(data){
	////console.log(form.val("setExam"));
	//var dom = data;
	////console.log(dom.value);
	//var classData = findClassById(classInfo,dom.value);
	//var radio = $("input[name='classStatus']");
	////console.log(classData);
	//if(classData.classStatus==false){
		//radio.each(function(){
			//var $this=$(this);
			////console.log($this.val());
			//if($this.val()=="true"){
				//$this.removeAttr("disabled");
				//$this.next().removeClass('layui-radio-disbaled layui-disabled');
				//$this.prop("checked","true")
			//}else{
				//$this.attr("disabled","disabled");
				//$this.next().addClass('layui-radio-disbaled layui-disabled');
			//}
			//form.render('radio');
		//})
		//$("#selectTime").removeAttr("disabled");
		//$("#selectTime").removeClass("ext-disabled");
		//$("#selectTime").attr("lay-verify","required");
		//$("#selectTimeLabel").removeClass("ext-disabled");
	//}else{
		//radio.each(function(){
			//var $this=$(this);
			////console.log($this.val());
			//if($this.val()=="false"){
				//$this.removeAttr("disabled");
				//$this.next().removeClass('layui-radio-disbaled layui-disabled');
				//$this.prop("checked","true")
			//}else{
				//$this.attr("disabled","disabled");
				//$this.next().addClass('layui-radio-disbaled layui-disabled');
			//}
			//form.render('radio');
		//})
		//$("#selectTime").attr("disabled","disabled");
		//$("#selectTime").addClass("ext-disabled");
		//$("#selectTime").removeAttr("lay-verify");
		//$("#selectTimeLabel").addClass("ext-disabled");
	//}
//});

////用id搜索班级
//function findClassById(data,classID){
	//var classData = data.data;
	////console.log(data);
	//var len = classData.length;
	////console.log(len);
	//for(i=0;i<len;i++){
		//if(classData[i].classID==classID){
		////console.log(classData[i].classID);
			//return classData[i];
		//}
	//}
	//return null;
//}
////监听设置考试提交
//form.on('submit(submitSetExam)', function(data){
	//var classData = data.field;
	//if(classData.classStatus=="true"){
		//classData.classStatus=true;
	//}else{
		//classData.classStatus=false;
		//classData.examTime=null;
	//}
	////console.log(classData);
	//$.ajax({
		//type:"POST",
		//url:"/api/setClassInExam",
		//dataType:"json",
		//data:classData,
		//async:false,
		//success:function(Jresp){
			//var resp = JSON.parse(Jresp);
			//console.log(resp);
			//if(resp.statusCode==0){
				//layer.msg("设置失败",{
					//shade:0.3,
					//time:500
				//});
			//}else{
				//layer.msg("设置成功",{
					//shade:0.3,
					//time:500
				//});
				//setTimeout(function(){
					//location.reload();
				//},500);
			//}
		//},
		//error:function(){
			//layer.msg("服务器出错",{
				//shade:0.3,
				//time:500
			//});
		//}
	//})
	////console.log(classData);
  //return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
//});

//提交setClassInExam
function setExam(data){
	data = JSON.stringify(data);
	$.ajax({
		type:"POST",
		url:"/api/setClassInExam",
		dataType:"json",
		data:data,
		async:false,
		success:function(Jresp){
			var resp = JSON.parse(Jresp);
			//console.log(resp);
			if(resp.statusCode==0){
				layer.msg("设置失败",{
					shade:0.3,
					time:500
				});
			}else{
				layer.msg("设置成功",{
					shade:0.3,
					time:500
				});
				setTimeout(function(){
					location.reload();
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
}

//将table内的数据格式转换为提交格式，data为要转换的数组
function toSetExamFormat(data){
	var len = data.length;
	var res = {
		classNumber:len,
		data:new Array()
	};
	for(i=0;i<len;i++){
		res.data.push({
			classID:data[i].classID,
			classStatus:data[i].classStatus,
			examTime:data[i].examTime,
		});
	}
	return res;
}
//检查是否设置已设置考试或取消未设置考试
function checkClassStatus( data, flag ){
	var len = data.classNumber;
	var thisFlag = true;
	for(i=0;i<len;i++){
		//console.log(data.data[i].classStatus);
		//console.log(flag);
		if(data.data[i].classStatus!=flag){
			thisFlag=false;
			break;
		}
	}
	if(thisFlag==false){
		if(flag==true){
			layer.msg("未有预设的考试",{
					shade:0.3,
					time:500
			});
		}else{
			layer.msg("已有预设的考试",{
					shade:0.3,
					time:500
			});
		}
	}
	return thisFlag;
}
//监听考试管理的每行的按钮
table.on("tool(examTable)",function(obj){
	var data =[obj.data];
	data = toSetExamFormat(data);
	selectSetExamData=data;
	//console.log(data);


	var layEvent = obj.event;
	if(layEvent=="examAdd"){
		if(checkClassStatus(selectSetExamData,false)==false)
			return false;
		displaySelectExamWindow();
	}else if(layEvent=="examSub"){
		if(checkClassStatus(selectSetExamData,true)==false)
			return false;
		var len = selectSetExamData.classNumber;
		for(i=0;i<len;i++){
			selectSetExamData.data[i].classStatus=false;
			selectSetExamData.data[i].examTime=null;
		}
		setExam(selectSetExamData);
	}
})

//监听考试管理的表头的按钮
table.on('toolbar(examTable)', function(obj){
	var checkStatus = table.checkStatus(obj.config.id);
	var data =checkStatus.data;
	data = toSetExamFormat(data);
	selectSetExamData=data;
	//console.log(selectSetExamData);

	var layEvent = obj.event;
	if(layEvent=="examAddMulti"){
		if(checkClassStatus(selectSetExamData,false)==false)
			return false;
		displaySelectExamWindow();
	}else if(layEvent=="examSubMulti"){
		if(checkClassStatus(selectSetExamData,true)==false)
			return false;
		var len = selectSetExamData.classNumber;
		for(i=0;i<len;i++){
			selectSetExamData.data[i].classStatus=false;
			selectSetExamData.data[i].examTime=null;
		}
		setExam(selectSetExamData);
	}
});

//显示选择时间的窗口
function displaySelectExamWindow(){
	var thisWindow = $("#selectExamWindow");
	layer.open({
		type:1,
		title:"设置考试时间",
		content:thisWindow,
		area:["500px","200px"],
		success:function(){
			thisWindow.removeClass("layui-hide");
		},
		cancel: function(){ 
			thisWindow.addClass("layui-hide");
		}    
	})
}

//监听设置考试提交
form.on('submit(submitSetExam)', function(data){
	var examTime = data.field.examTime;
	var len = selectSetExamData.classNumber;
	for(i=0;i<len;i++){
		selectSetExamData.data[i].classStatus=true;
		selectSetExamData.data[i].examTime=examTime;
	}
	setExam(selectSetExamData);
	return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
});
