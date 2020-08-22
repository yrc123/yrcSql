Mock.mock("/api/login",function(Jreq){
	var req = JSON.parse(Jreq.body);
	console.log(req);
	var data = Mock.mock({
		"status|0-2":1,
	})
	if(data["status"]!=0){
		$.cookie("userId",Mock.Random.string(16));
		$.cookie("username",Mock.Random.cname());
		$.cookie("character","student");
	}
	return (data);
});

Mock.mock("/api/logout",function(Jreq){
	var req = JSON.parse(Jreq.body);
	console.log(req);
	$.removeCookie("userId");
	$.removeCookie("username");
	$.removeCookie("character");
});

Mock.mock("/api/examStatus",function(Jreq){
	var req = JSON.parse(Jreq.body);
	console.log(req);
	var data = Mock.mock({
		"examStart|1":1,
	});
	return (data);
});

Mock.mock("/api/changePassword",function(Jreq){
	var req = JSON.parse(Jreq.body);
	console.log(req);
	return true;
});

Mock.mock("/api/submitExam",function(Jans){
	var ans = JSON.parse(Jans.body);
	//console.log((Jans.body));
	console.log(ans);
	ans[1]=new Array();
	for(i=0;i<3;i++){
		for(j=0;j<ans[0][i];j++){
			if(i==0){
				var num=[1,2,4,8];
				ans[1].push(num[Mock.Random.integer(0,3)]);
			}else if(i==1){
				ans[1].push(Mock.Random.integer(1,15));
			}else{
				ans[1].push(num[Mock.Random.integer(0,1)]);
			}
		}
	}

	if($.cookie("examType")==0){
		return null;
	}else{
		return (ans);
	}
});

Mock.mock("/api/getExam",function(Jreq){
	var req = JSON.parse(Jreq.body);
	console.log(req);
	var date = new Date();
	date.setSeconds(date.getSeconds()+15*60);

	var ts= date.getFullYear()+'/'+(date.getMonth()+1)+'/'+date.getDate()+' ';
	ts+=date.getHours()+':'+date.getMinutes()+':'+date.getSeconds();

	var id =Mock.Random.string(16);
	var Qnum = [10,20,20]
	id="same";
	var data = {
		paperId:id,
		date:ts,
		Qtype:Qnum
	}
	var Darr = new Array();
	for(i=0;i<3;i++){
		for(j=0;j<Qnum[i];j++){
			var sDataTmp={};
			sDataTmp["title"]=Mock.Random.cparagraph();
			var tip = new Array();
			if(i<2){
				for(z=0;z<4;z++){
					tip.push(Mock.Random.csentence());
				}
				sDataTmp["choice"]=tip;
			}
			Darr.push(sDataTmp);
		}
	}
	data["data"]=Darr;
	
	return (data);
});

Mock.mock("/api/getClassInExam",function(Jreq){
	var req = JSON.parse(Jreq.body);
	console.log(req);
	var num=30;
	var data=new Array();
	for(i=0;i<num;i++){
		var Tdata=Mock.mock({
			"classStatus|0-2":1
		})
		Tdata.className = "班级"+Mock.Random.cword('一二三四五六七八九十',1,2);
		Tdata.classId = Mock.Random.string(16);
		if(Tdata.classStatus){
			Tdata.examTime = Mock.Random.date('yyyy/MM/dd')+" "+Mock.Random.time('HH:mm:ss')+" ~ "+Mock.Random.date('yyyy/MM/dd')+" "+Mock.Random.time('HH:mm:ss');
		}
		data.push(Tdata);
	}
	res=data;
	return (res);
});

Mock.mock("/api/setClassInExam",function(Jreq){
	var req = JSON.parse(Jreq.body);
	console.log(req);
	var data = Mock.mock({
		"status|0":1,
	})
	return (data);
});

Mock.mock("/api/uploadStudentList",function(req){
	//console.log(req.body.get("className"));
	console.log(req);
	console.log(req.body.get("className"));
	console.log(req.body.get("studentForm"));
	var data = Mock.mock({
		"status|0-1":1,
	})
	//console.log(JSON.stringify(data));
	return (data);
});

Mock.mock("/api/delectClass",function(Jreq){
	req = JSON.parse(Jreq.body);
	console.log(req);
	var data = Mock.mock({
		"status|0":1,
	})
	//console.log(JSON.stringify(data));
	return (data);
});

Mock.mock("/api/getStudentInfo",function(Jreq){
	var res;
	req = JSON.parse(Jreq.body);
	console.log(req);
	var num;
	if(req.classId=="ALL"){
		num=300;
	}else{
		num=50;
	}
	console.log(num);
	var data=new Array();
	for(i=0;i<num;i++){
		var Tdata={};
		Tdata.studentName = Mock.Random.cname();
		Tdata.studentNo = Mock.Random.integer(100000000,999999999);
		Tdata.studentScore = Mock.Random.integer(30,100);
		data.push(Tdata);
	}
	res=data;
	return (res);
});
