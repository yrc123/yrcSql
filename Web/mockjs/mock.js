Mock.mock("/api/login",function(Jdata){
	console.log(Jdata);
	var data = Mock.mock({
		"loginCode|0-2":1,
	})
	if(data["loginCode"]!=0){
		$.cookie("userID","test");
		$.cookie("username","学生A");
		$.cookie("character","student");
		if(data["loginCode"]==1)location.href="./"+$.cookie("character")+".html";
	}
	return JSON.stringify(data);
});

Mock.mock("/api/logout",function(){
	$.removeCookie("userID");
	$.removeCookie("username");
	$.removeCookie("character");
	location.href="./index.html";
});

Mock.mock("/api/examStart",function(){
	var data = Mock.mock({
		"examStart|1":1,
	});
	return JSON.stringify(data);
});

Mock.mock("/api/changePassword",function(){
	location.href="./"+$.cookie("character")+".html";
	return true;
});

Mock.mock("/api/submitExam",function(Jans){
	var ans = JSON.parse(Jans.body);
	ans[1]=[1,1,1,1,1,1];
	var type = Mock.mock({
		"examType|1":1,
	});

	if(type["examType"]==0){
		return null;
	}else{
		return JSON.stringify(ans);
	}
});

Mock.mock("/api/getExam",function(){
	var data = Mock.mock({
		"time|60":1,
		"Qtype":[1,2,3],
		"data":[
			{
				"title":"test",
				"choice":["ans1","ans2","ans3","ans4"]

			},
			{
				"title":"Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.",
				"choice":["ans1","ans2","ans3","ans4"]

			},
			{
				"title":"Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.",
				"choice":["ans1","ans2","ans3","ans4"]

			},
			{
				"title":"Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.",
			},
			{
				"title":"Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.",
			},
			{
				"title":"Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.",

			}
		]

	})
	return data;
});
