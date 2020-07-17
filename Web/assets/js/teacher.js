var selectSetExamData;

//获取班级信息
function getClassInExam(){
	var data;
	$.ajax({
		type:"POST",
		url:"/api/getClassInExam",
		dataType:"json",
		async:false,
		success:function(resp){
			data = JSON.parse(resp);
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
}

//设置考试的班级表
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

//提交setClassInExam
function sendData(url,data){
	data = JSON.stringify(data);
	console.log(url);
	$.ajax({
		type:"POST",
		url:url,
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
		layer.confirm('确认删除考试?', {icon: 3, title:'提示'}, function(index){
			var len = selectSetExamData.classNumber;
			for(i=0;i<len;i++){
				selectSetExamData.data[i].classStatus=false;
				selectSetExamData.data[i].examTime=null;
			}
			sendData("/api/setClassInExam",selectSetExamData);
			layer.close(index);
		});
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
		layer.confirm('确认删除考试?', {icon: 3, title:'提示'}, function(index){
			var len = selectSetExamData.classNumber;
			for(i=0;i<len;i++){
				selectSetExamData.data[i].classStatus=false;
				selectSetExamData.data[i].examTime=null;
			}
			sendData("/api/setClassInExam", selectSetExamData);
			layer.close(index);
		});
	}
});


//显示上传窗口
function displayUploadWindow(){
	var thisWindow = $("#uploadWindow");
	layer.open({
		type:1,
		title:"上传班级学生文件",
		content:thisWindow,
		area:["400px","350px"],
		resize:false,
		success:function(){
			thisWindow.removeClass("layui-hide");
		},
		cancel: function(){ 
			thisWindow.addClass("layui-hide");
		}    
	})
}

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
	sendData("/api/setClassInExam",selectSetExamData);
	return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
});

//上传班级学生文件
upload.render({
	elem: '#uploadSpace',
	url: "/api/uploadStudentList", //改成您自己的上传接口
	auto: false,
	accept:"file",
	acceptMime:"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet,application/vnd.ms-excel",
	exts:"xls|xlsx",
	field:"studentForm",
	size:10000,
	bindAction:"#startUpload",
	choose:function(obj){
		obj.preview(function(index,file,result){
			$("#uploadSpace i").removeClass("layui-icon-upload-drag");
			$("#uploadSpace i").addClass("layui-icon-file");
			$("#uploadFileName").removeClass("layui-hide");
			$("#uploadFileName p").text(file.name);
		})
	},
	before: function(obj){
		var className=$("#inputClassName").val();
		//console.log(obj);
		this.data.className=className;
		layer.load(1);
	},
	done: function(res, index, upload){
		layer.closeAll('loading');
		//console.log(res);
		if(res.statusCode==0){
			layer.msg("上传失败",{
				shade:0.3,
				time:500
			});
		}else if(res.statusCode==1){
			layer.msg("上传成功",{
				shade:0.3,
				time:500
			});
		}
	},
	error:function(index,upload){
		layer.closeAll('loading');
		layer.msg("服务器出错",{
			shade:0.3,
			time:500
		});
	},
});

//设置班级管理的table
table.render({
	elem:"#setClassTable",
	cols:[[
		{field:"className",title:"班级名",width:"22%"},
		{field:"classStatusText",title:"考试状态",width:"23%",sort:true},
		{field:"examTimeText",title:"考试时间",width:"50%"},
		{fixed: 'right', title:'操作', toolbar: '#classBar', width:"60" ,unresize:true}
	]],
	url:"/api/getClassInExam",
	method:"POST",
	contentType:'application/json',
	parseData: function(res){ //res 即为原始返回的数据
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
		return data; 
	},
	toolbar:"#classToolbar", //开启头部工具栏，并为其绑定左侧模板
    defaultToolbar: ['filter', 'exports', 'print'],
	title:"班级信息",
	height: 'full-240',
});

//监听班级管理的每行的按钮
table.on("tool(classTable)",function(obj){
	var data ={
		classNumber:1,
		data:[obj.data.classID]
	};
	var layEvent = obj.event;
	if(layEvent=="classSub"){
		layer.confirm('确认删除班级?', {icon: 3, title:'提示'}, function(index){
			sendData("/api/delectClass",data);
			layer.close(index);
		});
	}
})

//监听班级管理的表头的按钮
table.on('toolbar(classTable)', function(obj){
	var layEvent = obj.event;
	console.log(layEvent);
	if(layEvent=="classAdd"){
		displayUploadWindow();
	}
});


