var page=0;
var lock=0;
var cardIds = new Array();

var cardName;
var id;

var currentPageIndex;// 当前页码数
var currentCount;// 当前总数
layui.use(['form','layer','laydate','table','laytpl'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        laydate = layui.laydate,
        laytpl = layui.laytpl,
        table = layui.table;

    
    //console.log("userList_bigin");

	  //卡牌列表
    var tableInCard = table.render({
      elem: '#card_list'
      ,toolbar: '#toolbarCards'
      ,url:request("/venne/cardList")
      ,id: 'card_list'
      ,page: true
      ,curr: 0
      ,limit:Common.limit
      ,limits:Common.limits
      ,groups: 7
      ,cols: [[ //表头
           {type:'checkbox',fixed:'left'}// 多选
          ,{field: 'id', title: '卡牌id',sort:'true', width:100}
          ,{field: 'cardName', title: '名称',sort:'true', width:145}
          ,{field: 'atk', title: 'ATK',sort:'true', width:100}
          ,{field: 'def', title: 'DEF',sort:'true', width:135}
          ,{field: 'disc', title: '描述',sort:'true', width:100}
          ,{field: 'auth', title: '作者',sort:'true', width:100}
         // ,{field: 'registerTime', title: '注册时间',sort:'true', width:170}
         // ,{field: 'createTime', title: '注册时间',sort:'true', width:170,templet: function(d){
         // 		return UI.getLocalTime(d.createTime);
         // }}
          ,{field: 'sex', title: '性别',sort:'true', width:105,templet: function(d){
          		return (d.sex==0?"女":"男");
          }}

          

          
          ,{fixed: 'right', width: 500,title:"操作", align:'left', toolbar: '#userListBar'}
        ]]
		  ,done:function(res, curr, count){
               if(count==0&&lock==1){
                 layer.msg("暂无数据",{"icon":2});
                 renderTable();
               }
               lock=0;
                $("#cardList").show();
                $("#card_table").show();
                $("#addCard").hide();
               // $("#autoCreateUser").hide();
                //$("#exportUser").hide();
                 
                var pageIndex = tableInCard.config.page.curr;//获取当前页码
                var resCount = res.count;// 获取table总条数
                currentCount = resCount;
                currentPageIndex = pageIndex;
		  }

    });
    $(".cardName").val('');

  //列表操作
  table.on('tool(card_list)', function(obj){
      var layEvent = obj.event,
            data = obj.data;
             
      if(layEvent === 'delete'){ //删除  
             
          Card.checkDeleteCardsImpl(data.id,1);
      }else if(layEvent === 'update'){// 修改卡牌
          
          Card.updateCard(obj.data,obj.data.id);
      }

  });

    //搜索用户
    $(".search_card").on("click",function(){
        // 关闭超出宽度的弹窗
        $(".layui-layer-content").remove();

        table.reload("card_list",{
            page: {
                curr: 1 //重新从第 1 页开始
            },
            where: {
                onlinestate:$("#status").val(),// 在线状态
                keyWorld : $(".cardName").val()  //搜索的关键字
            }
        })
        lock=1;
        $(".cardName").val('');
        $("#myFriends").hide();
    });


})

	 //layui END


//重新渲染表单
function renderTable(){
  layui.use('table', function(){
   var table = layui.table;//高版本建议把括号去掉，有的低版本，需要加()
   
   table.reload("card_list",{
        page: {
            curr: 1 //重新从第 1 页开始
        },
        where: {
            onlinestate:$("#status").val(),// 在线状态
            keyWorld : $(".cardName").val()  //搜索的关键字
        }
    })
  });
 }


var loginTime="";
var offlineTime="";

var Card={
	card_list:function(e,pageSize){
		var html="";
		if(e==undefined){
			e=0;
		}else if(pageSize==undefined){
			pageSize=10;
		}
		$.ajax({
			type:'POST',
			url:request('/venne/cardList'),
			data:{
				pageIndex:(e==0?"0":e-1),
				pageSize:pageSize
			},
			dataType:'json',
			async:false,
			success:function(result){
				if(result.data.pageData.length!=0){
					console.log(result.data.allPageCount);
					$("#pageCount").val(result.data.allPageCount);
					for(var i=0;i<result.data.pageData.length;i++){
						
						html+="<tr><td>"+result.data.pageData[i].userId+"</td><td style='width:100px'>"
						+result.data.pageData[i].nickname+"</td><td>"+result.data.pageData[i].telephone+"</td><td>"+UI.getLocalTime(result.data.pageData[i].createTime)
						+"</td><td>"+(result.data.pageData[i].onlinestate==0?"离线":"在线")+"</td><td>"+loginTime+"</td><td>"+offlineTime+"</td>";


						html+="<td><button onclick='User.deleteUser(\""+result.data.pageData[i].userId
						+"\")' class='layui-btn layui-btn-danger layui-btn-xs'>删除</button><button onclick='User.updateUser(\""
						+result.data.pageData[i].userId+"\")' class='layui-btn layui-btn-primary layui-btn-xs'>修改</button><button onclick='User.ResetPassword(\""
						+result.data.pageData[i].userId+"\")' class='layui-btn layui-btn-primary layui-btn-xs'>重置密码</button></td>";


						html+="</tr>";
					}

					$("#cardList_table").empty();
					$("#cardList_table").append(html);
					$("#cardList").show();
					$("#card_table").show();
					$("#addCard").hide();
					
				}

			}
		})
	},


	//  新增用户
	addCard:function(){
        $(".password").show();
		$("#userList").hide();
		$("#addUser").show();
         $("#id").val("0");
        $("#username").val("");
        $("#phone").val("");
        $("#password").val("");
        $("#sex").val("");
        $("#name").val("");
        $("#age").val("");
        //$("#level").val("");
        // 重新渲染
        layui.form.render();
		$("#addUserTitle").empty();
		$("#addUserTitle").append("新增用户");
        
	},
	// 提交新增用户
	commit_addCard:function(){
		
		if($("#username").val()==""){
			layui.layer.alert("请输入昵称");
			return;
		}
		if($("#phone").val()==""){
			layui.layer.alert("请输入手机号码");
			return;
		}
		if("0" == $("#id").val() && $("#password").val()==""){
            layui.layer.alert("请输入密码");
            return;
        }
        
		if($("#sex").val()==""){
            layui.layer.alert("请选择性别");
            return;
        }
        
       
        if($("#age").val()==""){
			layui.layer.alert("请输入年龄");
            return;
        }

		$.ajax({
			url:request('/venne/updateUser'),
			data:{
				id:$("#id").val(),
				username:$("#username").val(),
				name:$("#name").val(),
				phone:$("#phone").val(),
				password:$("#password").val(),
				age:$("#age").val(),
				sex:$("#sex").val()		       
			},
			dataType:'json',
			async:false,
			success:function(result){
				if(result.resultCode==1){
					if($("#id").val()==0){
						layer.alert("添加成功");
                        $("#userList").show();
                        $("#addUser").hide();
                        layui.table.reload("user_list",{
                            page: {
                                curr: 1 //重新从第 1 页开始
                            },
                            where: {

                            }
                        })

					}else{
						layer.alert("修改成功");
                        $("#userList").show();
                        $("#addUser").hide();
                        renderTable();
					}

				}else{
					layer.alert(result.data.resultMsg);
				}

			},
			error:function(result){
				if(result.resultCode==0){
					layer.alert(result.resultMsg);
				}
			}
		})
	},
	// 修改用户
	updateCard:function(data,id){
        $(".password").hide();
        //$("#birthday").val("");
		myFn.invoke({
			url:request('/venne/getUpdateUser'),
			data:{
				id:data.id
			},
			success:function(result){
				if(result.data!=null){
					$("#id").val(result.data.id);
					$("#username").val(result.data.username);
					$("#phone").val(result.data.phone);
					$("#sex").val(result.data.sex);
					$("#name").val(result.data.name);
					$('#age').val(result.data.age);
				}
				$("#addUserTitle").empty();
				$("#addUserTitle").append("修改用户");
				$("#userList").hide();
				$("#addUser").show();
                layui.form.render();
			}
		});

	},

        // 多选删除用户
        checkDeleteUsers:function(){
            // 多选操作
            var checkStatus = layui.table.checkStatus('user_list'); //idTest 即为基础参数 id 对应的值
            console.log("新版："+checkStatus.data) //获取选中行的数据
            console.log("新版："+checkStatus.data.length) //获取选中行数量，可作为是否有选中行的条件
            console.log("新版："+checkStatus.isAll ) //表格是否全选
		for (var i = 0; i < checkStatus.data.length; i++){
			userIds.push(checkStatus.data[i].userId);
		}
		console.log(userIds);
		if(0 == checkStatus.data.length){
			layer.msg("请勾选要删除的行");
			return;
		}
		User.checkDeleteUsersImpl(userIds.join(","),checkStatus.data.length);
	},

    checkDeleteCardsImpl:function(userId,checkLength){
        layer.confirm('确定删除指定用户',{icon:3, title:'提示消息',yes:function () {
                myFn.invoke({
                    url:request('/console/deleteUser'),
                    data:{
                        userId:userId
                    },
                    success:function(result){
                        if(result.resultCode==1){
                            layer.msg("删除成功",{"icon":1});
                            userIds = [];
                            // renderTable();
                            Common.tableReload(currentCount,currentPageIndex,checkLength,"user_list");
                        }
                    }
                })
            },btn2:function () {
                userIds = [];
            },cancel:function () {
                userIds = [];
            }});
    },


	// 重置密码
	ResetPassword:function(userId){

        // $(".randUser").on("click",function(){

            layui.layer.open({
                title:"重置密码",
                type: 1,
                btn:["确定","取消"],
                area: ['300px'],
                content: '<div id="mdifyPassword" class="layui-form" style="margin:20px 40px 10px 40px;;">'
                +   '<div class="layui-form-item">'
                +      '<div class="layui-input-block" style="margin: 0 auto;">'
                +        '<input type="password" required  lay-verify="required" placeholder="新的密码" autocomplete="off" class="layui-input admin_passwd">'
                +      '</div>'
                +    '</div>'
                +   '<div class="layui-form-item">'
                +      '<div class="layui-input-block" style="margin: 0 auto;">'
                +        '<input type="password" required  lay-verify="required" placeholder="确认密码" autocomplete="off" class="layui-input admin_rePasswd">'
                +      '</div>'
                +    '</div>'
                +'</div>'

                ,yes: function(index, layero){ //确定按钮的回调

                    var newPasswd = $("#mdifyPassword .admin_passwd").val();
                    var reNewPasswd = $("#mdifyPassword .admin_rePasswd").val();
                    if(newPasswd!=reNewPasswd){
                        layui.layer.msg("两次密码输入不一致",{"icon":2});
                        return;
                    }

                    Common.invoke({
                        path : '/venne/updatePassword',
                        data : {
                            "userId" : userId,
                            "password": $.md5(newPasswd)
                        },
                        successMsg : "重置密码成功",
                        errorMsg :  "重置密码失败，请稍后重试",
                        successCb : function(result) {
                            layui.layer.close(index); //关闭弹框
                            // location.replace("/pages/console/login.html");
                        },
                        errorCb : function(result) {

                        }
                    });

                }

            });

        // });
	},
	
	
	
	
	// 提交
	commit_exportUser:function(){
		myFn.invoke({
			url:request('/console/exportData'),
			data:{
				userType:$("#userType").val()
			},
			success:function(result){
				if(result.resultCode==1){
					layer.alert("导出成功");
				}
			}
		})
	},

    

   
    button_back:function(){

  		$("#userList").show();
  		$("#user_table").show();
      $(".user_btn_div").show();
  		$("#autoCreateUser").hide();
  		$("#exportUser").hide();
  		$("#addUser").hide();
      $("#myFriends").hide();
      $("#myInviteCode").hide();
	},
 

}