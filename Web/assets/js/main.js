//加载皮肤
layui.use('layer', function(){
	var layer = layui.layer;
	layer.config({
		extend:'skin/LoginPopup/style.css'
	});
});
//使用layer
var layer = layui.layer;

//监听侧边栏
layui.use('element', function(){
	//使用element
	var element = layui.element;
	//一些事件监听
	element.on('nav(left-nav)', function(elem){
		var sideArr = document.querySelectorAll(".side-item");
		var len = sideArr.length;
		for(i=0;i<len;i++){
			sideArr[i].classList.add("layui-hide");
		}
		sideArr[elem[0].type].classList.remove("layui-hide");
	});
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
	$(".layui-header").load("./layuiHeader.html",function(response,status,xhr){

		checkUserID();

		//退出登录
		$("#logout").click(function(){
			$.ajax({
				type:"POST",
				url:"/api/logout",
			})
		})
		var aArr = document.querySelectorAll("#header li>a");
		var len = aArr.length;
		for(i=0;i<len;i++){
			if(aArr[i].type==s){
				aArr[i].parentNode.classList.add("layui-this");
			}
		}
	});
}

//显示登录窗口
$("body").append("<div id='loginWindow' class='layui-hide'></div>") 
$("#loginWindow").load("./loginWindow.html");
function displayLoginWindow(){
	layui.use('layer', function(){
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
	}); 
};

//显示修改密码窗口
$("body").append("<div id='changePasswordWindow' class='layui-hide'></div>") 
$("#changePasswordWindow").load("./changePasswordWindow.html");
function displayChangePasswordWindow(flag = 1){
	layui.use('layer', function(){
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
	}); 
};

//显示权限不足窗口
$("body").append("<div id='permissionWindow' class='layui-hide'></div>") 
$("#permissionWindow").load("./permissionWindow.html");
function displayPermissionWindow(){
	layui.use('layer', function(){
		var thisWindow = $("#permissionWindow");
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
layui.use('form', function(){
	var form =layui.form;
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
				if(data["loginCode"]==0){
					layer.msg("用户名或密码错误");
				}else if(data["loginCode"]==1){
					layer.msg("登录成功");
				}else if(data["loginCode"]==2){
					$.cookie("hasNotChangePassword","ture");
					displayChangePasswordWindow(0);
				}	
			},
			error:function(){
				layer.msg("服务器出错");
			}

		})
		return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
	});
});

//修改密码
layui.use('form', function(){
	var form =layui.form;
	form.on('submit(changePassword)', function(){
		var newPassword=$("#newPassword").val();
		var newPasswordTwins=$("#newPasswordTwins").val();
		//md5加密
		if(newPassword!=newPasswordTwins){
			layer.msg("两次输入的密码不一致")
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
			},
			error:function(){
				layer.msg("服务器出错");
			}
		})
		return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
	});
});
