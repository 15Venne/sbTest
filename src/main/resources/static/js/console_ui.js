var page=0;
var sum=0;
var IS_ADMIN=0;
var telephone="";
var UI={
	// 加载页面公共方法
	load_html:function(url){
		var html="<iframe frameborder='0' width='99.9%'' height='99.9%' src='"+url+"'></iframe>";
		$("#index_body").empty();
		$("#index_body").append(html);
		
	},
	// 查询消息列表
	findMsgList:function(e){
		html="";
		if(e==1){
			if(page>0){
				page--;
			}
		}else if(e==2){
			if(sum<10){
				layui.layer.alert("已是最后一页");
			}else{
				page++;
			}
		}
		
		myFn.invoke({
			url:'/console/chat_logs_all',
			data:{
				pageIndex:page,
			},
			success:function(result){
				sum=result.data.pageSize;
				if(result.data.pageSize!=0){
					for(var i=0;i<result.data.pageData.length;i++){
						html+="<tr><td>"+result.data.pageData[i].sender+"</td><td>"+result.data.pageData[i].sender_nickname
						+"</td><td>"+result.data.pageData[i].receiver+"</td><td>"+result.data.pageData[i].receiver_nickname
						+"</td><td>"+UI.getLocalTime(result.data.pageData[i].timeSend)+"</td><td>"+result.data.pageData[i].content+"</td></tr>";
					}
					$("#message_table").empty();
					$("#message_table").append(html);
				}
			}
		});
	},
	// 时间转换
	getLocalTime:function(time){
		 var date = new Date(time * 1000);//时间戳为10位需*1000，时间戳为13位的话不需乘1000
        var Y = date.getFullYear() + '-';
        var M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-';
        var D = date.getDate() + ' ';
        var h = date.getHours() + ':';
        var m = (date.getMinutes()<10?'0'+(date.getMinutes()):date.getMinutes()) + ':';
        var s = (date.getSeconds()<10?'0'+(date.getSeconds()):date.getSeconds());
        return Y+M+D+h+m+s;
	},
	
	
	// 用户列表
	user_list:function(e){
		var html="";
		if(e==1){
			if(page>0){
				page--;
			}
		}else if(e==2){
			if(sum<10){
				layui.layer.alert("已是最后一页");
			}else{
				page++;
			}
		}
		myFn.invoke({
			url:'/venne/userList',
			data:{
				pageIndex:page
			},
			success:function(result){
				if(result.data.pageData.length!=0){
					for(var i=0;i<result.data.pageData.length;i++){
						html+="<tr><td>"+result.data.pageData[i].userId+"</td><td>"
						+result.data.pageData[i].nickname+"</td><td>"+result.data.pageData[i].createTime
						+"</td><td>"+result.data.pageData[i].onlinestate+"</td><td style='width:25%'><button onclick='UI.deleteUser(\""+result.data.pageData[i].userId+"\")' class='layui-btn'>删除</button><button onclick='UI.updateUser(\""+result.data.pageData[i].userId+"\")' class='layui-btn'>修改</button><button class='layui-btn'>重置密码</button></td></tr>";
					}

					$("#userList_table").empty();
					$("#userList_table").append(html);
				}

			}
		})
	},
	// 搜索用户
	findUserByname:function(){
		var html="";
		myFn.invoke({
			url:'/venne/userList',
			data:{
				keyWorld:$("#nickName").val(),
				onlinestate:$("#status").val()
			},
			success:function(result){
				if(result.data.pageData.length!=0){
					$("#userList_table").empty();
					for(var i=0;i<result.data.pageData.length;i++){
						html+="<tr><td>"+result.data.pageData[i].userId+"</td><td>"+result.data.pageData[i].nickname
					+"</td><td>"+result.data.pageData[i].createTime+"</td><td>"+result.data.pageData[i].onlinestate
					+"</td><td style='width:25%'><button class='layui-btn'>删除</button><button onclick='UI.updateUser(\""+result.data.pageData[i].userId+"\")' class='layui-btn'>修改</button><button class='layui-btn'>重置密码</button></td></tr>";
					}
				}
				$("#userList_table").empty();
				$("#userList_table").append(html);
				$("#nickName").val("");
				$("#status").val("");
			}
		});
	},

	
	//  新增用户
	addUser:function(){
		$("#userList").hide();
		$("#addUser").show();
		$("#userId").val(0);
		// $("#new").show();
		// $("#update").hide();
	},
	// 提交新增用户
	commit_addUser:function(){
		if($("#userName").val()==""){

			alert("请输入必填参数");
			return;
		}else if($("#telephone").val()==""){
			alert("请输入必填参数");
			return;
		}else if($("#password").val()==""){
			alert("请输入必填参数");
			return ;
		}
		myFn.invoke({
			url:'/venne/updateUser',
			data:{
				userId:$("#userId").val(),
				nickname:$("#userName").val(),
				telephone:$("#telephone").val(),
				password:$("#password").val(),
				userType:$("#isPublic").val(),
				publicType:$("#publicType").val(),
				level:$("#level").val()
			},
			success:function(result){
				if(result.resultCode){
					layer.alert("修改成功");
				}
				
			}
		})
	},

	
	
	
	
	// 修改用户
	updateUser:function(id){
		myFn.invoke({
			url:'/venne/getUpdateUser',
			data:{
				id:id
			},
			success:function(result){
				if(result.data!=null){
					$("#id").val(result.data.id);
					$("#username").val(result.data.username);
					$("#phone").val(result.data.phone);
					//$("#password").val(result.data.password);
					$("#age").val(result.data.age);
					$("#sex").val(result.data.sex);
					$("#name").val(result.data.name);
				}
				$("#userList").hide();
				$("#addUser").show();
				// $("#new").hide();
				// $("#update").show();
			}
		});
	},
	// 删除用户
	deleteUser:function(userId){
		myFn.invoke({
			url:'/venne/deleteUser',
			data:{
				userId:userId
			},
			success:function(result){
				if(result.resultCode==1){
					layer.alert("删除成功");
					UI.user_list(0);
				}
			}
		})
	}

}