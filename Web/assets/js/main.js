
//显示登录窗口
function displayLoginWindow(){
	layui.use('layer', function(){
		var layer = layui.layer;
		layer.open({
			type:2,
			content:"./loginWindow.html",
			title:"用户登录",
			area:["500px","250px"]
		});
	}); 
};

//显示权限不足窗口
function displayPermissionWindow(){
	layui.use('layer', function(){
		var layer = layui.layer;
		layer.open({
			type:2,
			title:0,
			closeBtn:0,
			content:"./permissionWindow.html",
			area:["300px","400px"],
			resize:0,
		});
	}); 
};

//权限检查
function checkPermission(character){
	if($.cookie("userID") == null){
		displayLoginWindow();
	}else if($.cookie("character")!=character){
		displayPermissionWindow();
	}else{
		window.location.href = "./"+character+".html";
	}
}
