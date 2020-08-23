var selectSetExamData;
var getClassID="ALL";


//设置教师名单
table.render({
    elem:"#setTeacherTable",
	cols:[[
		{type: 'checkbox', fixed: 'left'},
		{field:"teacherId",title:"教师账号",width:"20%",sort:true},
		{field:"teacherName",title:"教师姓名",width:"50%",sort:true},
		{fixed: 'right', title: '重置密码', toolbar: '#teacherListToolBar', width:"25%" ,unresize:true}
	]],
	url:"/api/getTeacherList",
	method:"POST",
	contentType:'application/json',
	parseData: function(res){ //res 即为原始返回的数据
		//console.log(res);
		res =(res);
		var data = {
			"code": 0, //解析接口状态
			"msg": res.message, //解析提示文本
			"count": res.total, //解析数据长度
		};
		data.data=res;
		//console.log(data);
		return data; 
	},
	toolbar:  '#teacherListBar' , //开启头部工具栏，并为其绑定左侧模板
    defaultToolbar: ['filter', 'exports', 'print'],
	title:"班级考表",
	height: 'full-240',
});

//监听教师名单的表头的按钮
table.on('toolbar(teacherTable)', function(obj){
	//console.log("test111");
	var checkStatus = table.checkStatus(obj.config.id);
	var data =checkStatus.data;

	var layEvent = obj.event;
	if(layEvent=="teacherSub"){
		layer.confirm('确认删除教师?', {icon: 3, title:'提示'}, function(index){
			var len = data.length;
			for(i=0;i<len;i++){
				data[i].teacherStatus=false;
			}
			sendData("/api/setTeacherList", data);
			layer.close(index);
		});
	}else if(layEvent=="teacherAdd"){
		displayUploadWindow();
	}
});

//监听教师名单的每行的按钮
table.on("tool(teacherTable)",function(obj){
	var data =[obj.data];

	var layEvent = obj.event;
	if(layEvent=="teacherReset"){
		layer.confirm('确认重置为初始密码?', {icon: 3, title:'提示'}, function(index){
			var len = data.length;

			sendData("/api/resetPassword",data);			//
			layer.close(index);
		});
	}
})

//提交
function sendData(url,data){
	data = JSON.stringify(data);
	console.log(url);
	$.ajax({
		type:"POST",
		url:url,
		dataType:"json",
		contentType: "application/json; charset=utf-8",
		data:data,
		async:false,
		success:function(Jresp){
			var resp = (Jresp);
			//console.log(resp);
			if(resp.status==0){
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

//教师名单上传窗口
function displayUploadWindow(){
	var thisWindow = $("#uploadWindow");
	layer.open({
		type:1,
		title:"上传教师名单文件",
		content:thisWindow,
		area:["400px","320px"],
		resize:false,
		success:function(){
			thisWindow.removeClass("layui-hide");
		},
		cancel: function(){ 
			thisWindow.addClass("layui-hide");
		}    
	})
}

//上传教师名单文件
upload.render({
	elem: '#uploadSpace',
	url: "/api/uploadStudentList", //改成您自己的上传接口
	auto: false,
	accept:"file",
	acceptMime:"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet,application/vnd.ms-excel",
	exts:"xls|xlsx",
	field:"file",
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
		if(res.status==0){
			layer.msg("上传失败",{
				shade:0.3,
				time:500
			});
		}else if(res.status==1){
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

//设置学生名单
table.render({
    elem:"#studentTable",
	cols:[[
		// {type: 'checkbox', fixed: 'left'},
		{field:"studentNo",title:"学生账号",width:"20%",sort:true},
		{field:"studentName",title:"学生姓名",width:"50%",sort:true},
		{fixed: 'right', title: '重置密码', toolbar: '#studentResetToolBar', width:"25%" ,unresize:true}
	]],
	url:"/api/getStudentInfo",
	method:"POST",
	contentType:'application/json',
	parseData: function(res){ //res 即为原始返回的数据
		//console.log(res);
		res =(res);
		var data = {
			"code": 0, //解析接口状态
			"msg": res.message, //解析提示文本
			"count": res.total, //解析数据长度
		};
		data.data=res;
		//console.log(data);
		return data; 
	},
	//toolbar:  '#teacherListBar' , //开启头部工具栏，并为其绑定左侧模板
    //defaultToolbar: ['filter', 'exports', 'print'],
	title:"班级考表",
	height: 'full-240',
});

//监听学生名单的每行的按钮
table.on("tool(studentTable)",function(obj){
	var data =[obj.data];

	var layEvent = obj.event;
	if(layEvent=="studentReset"){
		layer.confirm('确认重置为初始密码?', {icon: 3, title:'提示'}, function(index){
			var len = data.length;

			sendData("/api/resetPassword",data);			//
			layer.close(index);
		});
	}
})

//题库上传窗口
function displayQuestionUploadWindow(){
	var thisWindow = $("#uploadWindow");
	layer.open({
		type:1,
		title:"上传题库文件",
		content:thisWindow,
		area:["400px","320px"],
		resize:false,
		success:function(){
			thisWindow.removeClass("layui-hide");
		},
		cancel: function(){ 
			thisWindow.addClass("layui-hide");
		}    
	})
}

//上传题库文件文件
upload.render({
	elem: '#uploadSpace',
	url: "/api/uploadQuestionBank", //改成您自己的上传接口
	auto: false,
	accept:"file",
	acceptMime:"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet,application/vnd.ms-excel",
	exts:"xls|xlsx",
	field:"file",
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
		if(res.status==0){
			layer.msg("上传失败",{
				shade:0.3,
				time:500
			});
		}else if(res.status==1){
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

//显示选择正式考试时间的窗口
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
