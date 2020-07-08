Mock.mock("/api/login",function(){
	var data = Mock.mock({
		"loginCode|0-1":1,
	})
	if(data["loginCode"]==1){
		$.cookie("userID","test");
		$.cookie("username","学生A");
		$.cookie("character","student");
		location.href="./index.html";
	}
	return JSON.stringify(data);

});
Mock.mock("/api/logout",function(){
	$.removeCookie("userID");
	$.removeCookie("username");
	$.removeCookie("character");
	location.href="./index.html";
});
