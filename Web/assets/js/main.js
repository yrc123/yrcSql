var form =layui.form;
var layer =layui.layer;
var element =layui.element;
var laydate = layui.laydate;
var transfer = layui.transfer;
var laytpl = layui.laytpl;
//加载皮肤
var layer = layui.layer;
layer.config({
	extend:'skin/LoginPopup/style.css',
});
//监听侧边栏
	element.on('nav(left-nav)', function(elem){
		var sideArr = document.querySelectorAll(".side-item");
		var len = sideArr.length;
		for(i=0;i<len;i++){
			sideArr[i].classList.add("layui-hide");
		}
		sideArr[elem[0].type].classList.remove("layui-hide");
	});

//修改登录和个人中心
function checkUserID(){
	if($.cookie('userID')!=null){
		$("#user-info").removeClass("layui-hide");
		$("#user-name").text($.cookie('username'));
	}else{
		$("#login").removeClass("layui-hide");
	}
}

//显示顶端导航栏
function loadHeader(s){
	$(".layui-layout-admin").prepend('<div class="layui-header"></div>') 
	$(".layui-header").load("./layuiHeader.html",function(){

		checkUserID();

		//退出登录
		$("#logout").click(function(){
			$.ajax({
				type:"POST",
				url:"/api/logout",
				success:function(){
					location.href="./index.html";
				}
			})
		})
		var aArr = document.querySelectorAll("#header li>a");
		var len = aArr.length;
		for(i=0;i<len;i++){
			if(aArr[i].type==s){
				aArr[i].parentNode.classList.add("layui-this");
			}
		}
		element.render('nav');
	});
}

//显示登录窗口
$("body").append("<div id='loginWindow' class='layui-hide'></div>") 
$("#loginWindow").load("./loginWindow.html");
function displayLoginWindow(){
	var thisWindow = $("#loginWindow");
		layer.open({
			type:1,
			title:0,
			closeBtn:0,
			resize:0,
			skin:'login-popup',
			content:thisWindow,
			area:["500px","399px"],
			shadeClose:1,
			success:function(){
				thisWindow.removeClass("layui-hide");
			},
			cancel: function(){ 
				thisWindow.addClass("layui-hide");
			}    
	});
};

//显示修改密码窗口
$("body").append("<div id='changePasswordWindow' class='layui-hide'></div>") 
$("#changePasswordWindow").load("./changePasswordWindow.html");
function displayChangePasswordWindow(flag = 1){
	var thisWindow = $("#changePasswordWindow");
		layer.open({
			type:1,
			title:0,
			closeBtn:0,
			resize:0,
			skin:'login-popup',
			content:thisWindow,
			area:["500px","399px"],
			shadeClose:flag,
			success:function(){
				thisWindow.removeClass("layui-hide");
			},
			cancel: function(){ 
				thisWindow.addClass("layui-hide");
			}    
		});
};

//显示权限不足窗口
$("body").append("<div id='permissionWindow' class='layui-hide'></div>") 
$("#permissionWindow").load("./permissionWindow.html");
function displayPermissionWindow(){
	var thisWindow = $("#permissionWindow");

	layui.use('layer', function(){
		layer.open({
			type:1,
			title:0,
			closeBtn:0,
			resize:0,
			skin:"layer-ext-hide",
			content:thisWindow,
			area:["300px","400px"],
			shadeClose:0,
			success:function(){
				thisWindow.removeClass("layui-hide");
			},
			cancel: function(){ 
				thisWindow.addClass("layui-hide");
			}    
		});
	});
};

//
function doCheck(data){
	var flag=checkPermission(data.type);
	if(flag){
		window.location.href = "./"+$.cookie("character")+".html";
	}
}
//权限检查
function checkPermission(s){
	if($.cookie("hasNotChangePassword")!=null){
		$.removeCookie("userID");
		$.removeCookie("username");
		$.removeCookie("character");
		$.removeCookie("hasNotChangePassword");
	}
	if(s=="index"){
		return true;
	}
	if($.cookie("userID") == null){
		displayLoginWindow();
		return false;
	}else if($.cookie("character")!=s){
		displayPermissionWindow();
		return false;
	}
	return true;
}

//
function myHtml(){
	var flag=checkPermission($.cookie("character"));
	if(flag){
		window.location.href = "./"+$.cookie("character")+".html";
	}
}


//登录表单
form.on('submit(login)', function(){
	var username=$("#username").val();
	var password=$("#password").val();
	//md5加密
	password=$.md5(password);
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
			console.log(data)
			if(data["loginCode"]==0){
				layer.msg("用户名或密码错误",{
					shade:0.3,
					time:500
				});
			}else if(data["statusCode"]==1){
				layer.msg("登录成功",{
					shade:0.3,
					time:500
				});
				setTimeout(function(){
					location.href="./"+$.cookie("character")+".html";
				},500);
			}else if(data["statusCode"]==2){
				$.cookie("hasNotChangePassword","ture");
				displayChangePasswordWindow(0);
			}	
		},
		error:function(){
			layer.msg("服务器出错",{
				shade:0.3,
				time:500
			});
		}

	})
	return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
});

//修改密码
form.on('submit(changePassword)', function(){
	var newPassword=$("#newPassword").val();
	var newPasswordTwins=$("#newPasswordTwins").val();
	//md5加密
	if(newPassword!=newPasswordTwins){
		layer.msg("两次输入的密码不一致",{
			shade:0.3,
			time:500
		})
		return false;
	}
	newPassword=$.md5(newPassword);
	$.ajax({
		type:"POST",
		url:"/api/changePassword",
		dataType:"json",
		data:{
			"newPassword":newPassword,
		},
		async:false,
		success:function(){
			$.removeCookie("hasNotChangePassword");
			layer.msg("密码修改成功",{
				shade:0.3,
				time:500
			});
			setTimeout(function(){
				location.href="./"+$.cookie("character")+".html";
			},500);
		},
		error:function(){
			layer.msg("服务器出错",{
				shade:0.3,
				time:500
			});
		}
	})
	return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
});
