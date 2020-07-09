//加载皮肤
layui.use('layer', function(){
	var layer = layui.layer;
	layer.config({
		extend: 'hide/hide.css' //你的皮肤
	});
});
//显示登录窗口
$("body").append("<div id=\"loginWindow\" class=\"layui-hide\"></div>") 
$("#loginWindow").load("./loginWindow.html");
function displayLoginWindow(){
	layui.use('layer', function(){
		var layer = layui.layer;
		layer.open({
			type:1,
			title:0,
			closeBtn:0,
			resize:0,
			skin:"layer-ext-hide",
			content:$("#loginWindow").html(),
			area:["500px","250px"]
		});
	}); 
};

//显示权限不足窗口
$("body").append("<div id=\"permissionWindow\" class=\"layui-hide\"></div>") 
$("#permissionWindow").load("./permissionWindow.html");
function displayPermissionWindow(){
	layui.use('layer', function(){
		var layer = layui.layer;
		layer.open({
			type:1,
			title:0,
			closeBtn:0,
			resize:0,
			skin:"layer-ext-hide",
			content:$("#permissionWindow").html(),
			area:["300px","400px"],
		});
	}); 
};

//
function doCheck(data){
	checkPermission(data.type);
}
//权限检查
function checkPermission(s){
	if($.cookie("userID") == null){
		displayLoginWindow();
	}else if($.cookie("character")!=s){
		displayPermissionWindow();
	}else{
		window.location.href = "./"+s+".html";
	}
}

//
function myHtml(){
	checkPermission($.cookie("character"));
}

//退出登录
$("#logout").click(function(){
	$.ajax({
		type:"POST",
		url:"/api/logout",
		success:function(resp){
		}
	})
})
//
layui.use('form', function(){
	var form =layui.form;
	form.on('submit(form-submit)', function(){
		var username=$(":text").val();
		var password=$(":password").val();
		//md5加密
		password=$.md5(password);
		var loginCode=-1;
		$.ajax({
			type:"POST",
			url:"/api/login",
			dataType:"json",
			data:{
				"username":username,
				"password":password
			},
			async:false,
			success:function(resp){
				var data=JSON.parse(resp);
				if(data["loginCode"]!=undefined){
					loginCode=data["loginCode"];
				}
			}

		})
		var layer=layui.layer;
		if(loginCode==-1){
			layer.msg("服务器出错");
		}else if(loginCode==0){
			layer.msg("用户名或密码错误");
		}else{
			layer.msg("登录成功");
		}
		return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
	});
});
