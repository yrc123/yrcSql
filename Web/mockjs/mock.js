
Mock.mock("/api/logout",function(){
	return "logout";
});
/*
console.log($("#login a").text());
$("#login a").click(function(){
	$.ajax({
		url:"/api/login",
		dataType:"Text",
		data:{},
		type:"GET",
		beforeSend:function(){

		},
		success:function(req){
			$("#login a").text(req);
		},
		error:function(){
			$("#login a").text("error");
		}
	})
})*/
