var form =layui.form;
var layer =layui.layer;
var element =layui.element;
var laydate = layui.laydate;
var transfer = layui.transfer;
var laytpl = layui.laytpl;
var table = layui.table;
var upload = layui.upload;
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
function displayPermissionWindow(s){
	if($("#permissionTitle").length==0){
		$("#permissionWindow").load("./permissionWindow.html",function(){
			insertTitile(s);
		});
	}else{
		insertTitile(s);
	}
	var thisWindow = $("#permissionWindow");
			layer.open({
			type:1,
			title:0,
			closeBtn:0,
			resize:0,
			skin:'login-popup',
			content:thisWindow,
			area:["400px","420px"],
			success:function(){
				thisWindow.removeClass("layui-hide");
			},
			cancel: function(){ 
				thisWindow.addClass("layui-hide");
			}    
	});
};
function insertTitile(s){
	document.getElementById("permissionTitle").innerHTML=s;
}

//是否第一次更改密码
function checkChangePassword(){
	if($.cookie("hasNotChangePassword")!=null){
		$.removeCookie("userID");
		$.removeCookie("username");
		$.removeCookie("character");
		$.removeCookie("hasNotChangePassword");
	}
}
//权限检查
//返回0-2
//0代表未登录
//1代表权限正常
//2代表权限不足
function checkPermission(s){
	if(s=="index"){
		return 1;
	}
	if($.cookie("userID") == null){
		return 0;
	}
	if($.cookie("character")!=s){
		return 2;
	}
	return 1;
}


//检查当前页面的权限
//jump为true时自动跳转，否则不跳转
function checkPagePermission(s, jump=true){
	var flag = checkPermission(s);
	if(flag==0){
		displayLoginWindow();
	}else if(flag==1){
		if(jump){
			window.location.href = "./"+$.cookie("character")+".html";
		}
	}else if(flag==2){
		displayPermissionWindow("不要往这里看啊!");
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

//显示第no个侧边栏的内容
function goSide(no){
	var sideArr = document.querySelectorAll(".side-item");
	var len = sideArr.length;
	for(i=0;i<len;i++){
		sideArr[i].classList.add("layui-hide");
	}
	sideArr[no].classList.remove("layui-hide");
}
