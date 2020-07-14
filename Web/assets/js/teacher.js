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
			layer.msg("服务器出错");
		}
	})
	//console.log(data);
	return data;
}
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
function setSelect(data){
	var len = data.classNumber;
	var selectTemplate = $("#selectTemplate");
	var selectClass = $("#selectClass");
	console.log(selectClass.html());
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

