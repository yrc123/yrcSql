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
//设置班级表
function setTable(data){
	var len = data.classNumber;
	var tableTemplate = $("#tableTemplate");
	var classInfo = $("#classInfo");
	classInfo.html("");
	for(i=0;i<len;i++){
		var tdata={
			className:data.data[i].className,
		};
		if(data.data[i].classStatus==true){
			tdata.classStatus="考试已设定";
			tdata.examTime=data.data[i].examTime;
		}else{
			tdata.classStatus="考试未设定";
			tdata.examTime="考试时间未定";
		}
		laytpl(tableTemplate[0].innerHTML).render(tdata,function(html){
			classInfo.append(html);
		})
	}
}
//设置select选项
function setSelect(data){
	var len = data.classNumber;
	var selectTemplate = $("#selectTemplate");
	var selectClass = $("#selectClass");
	//console.log(selectClass.html());
	selectClass.html('<option value="" >请选择一个班级</option>');
	for(i=0;i<len;i++){
		var tdata={
			className:data.data[i].className,
			classID:data.data[i].classID,
		};
		//console.log(tdata);
		laytpl(selectTemplate[0].innerHTML).render(tdata,function(html){
			//console.log(html);
			selectClass.append(html);
		})
	}
	form.render('select');
}
//监听选择班级
form.on('select(selectInSetExam)', function(data){
	//console.log(form.val("setExam"));
	var dom = data;
	//console.log(dom.value);
	var classData = findClassById(classInfo,dom.value);
	var radio = $("input[name='classStatus']");
	//console.log(classData);
	if(classData.classStatus==false){
		radio.each(function(){
			var $this=$(this);
			//console.log($this.val());
			if($this.val()=="true"){
				$this.removeAttr("disabled");
				$this.next().removeClass('layui-radio-disbaled layui-disabled');
				$this.prop("checked","true")
			}else{
				$this.attr("disabled","disabled");
				$this.next().addClass('layui-radio-disbaled layui-disabled');
			}
			form.render('radio');
		})
		$("#selectTime").removeAttr("disabled");
		$("#selectTime").removeClass("ext-disabled");
		$("#selectTime").attr("lay-verify","required");
		$("#selectTimeLabel").removeClass("ext-disabled");
	}else{
		radio.each(function(){
			var $this=$(this);
			//console.log($this.val());
			if($this.val()=="false"){
				$this.removeAttr("disabled");
				$this.next().removeClass('layui-radio-disbaled layui-disabled');
				$this.prop("checked","true")
			}else{
				$this.attr("disabled","disabled");
				$this.next().addClass('layui-radio-disbaled layui-disabled');
			}
			form.render('radio');
		})
		$("#selectTime").attr("disabled","disabled");
		$("#selectTime").addClass("ext-disabled");
		$("#selectTime").removeAttr("lay-verify");
		$("#selectTimeLabel").addClass("ext-disabled");
	}
});

//用id搜索班级
function findClassById(data,classID){
	var classData = data.data;
	//console.log(data);
	var len = classData.length;
	//console.log(len);
	for(i=0;i<len;i++){
		if(classData[i].classID==classID){
		//console.log(classData[i].classID);
			return classData[i];
		}
	}
	return null;
}
//监听设置考试提交
form.on('submit(submitSetExam)', function(data){
	var classData = data.field;
	if(classData.classStatus=="true"){
		classData.classStatus=true;
	}else{
		classData.classStatus=false;
		classData.examTime=null;
	}
	//console.log(classData);
	$.ajax({
		type:"POST",
		url:"/api/setClassInExam",
		dataType:"json",
		data:classData,
		async:false,
		success:function(Jresp){
			var resp = JSON.parse(Jresp);
			console.log(resp);
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
	//console.log(classData);
  return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
});
