Mock.mock("/api/login",function(){
	var data = Mock.mock({
		"loginCode|2":1,
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
		"examStart|0-1":1,
	})
	if(data["examStart"]===1){
		location.href="./exam.html";
	}
	return JSON.stringify(data);
});

Mock.mock("/api/changePassword",function(){
	location.href="./"+$.cookie("character")+".html";
	return true;
});
