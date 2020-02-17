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
          ,{field: 'label', title: 'label',sort:'true', width:100}
          ,{field: 'catagory1', title: '分类1',sort:'true', width:100}
          ,{field: 'catagory2', title: '分类2',sort:'true', width:100}
          ,{field: 'catagory2', title: '分类3',sort:'true', width:100}
         // ,{field: 'registerTime', title: '注册时间',sort:'true', width:170}
         // ,{field: 'createTime', title: '注册时间',sort:'true', width:170,templet: function(d){
         // 		return UI.getLocalTime(d.createTime);
         // }}
          ,{field: 'sex', title: '性别',sort:'true', width:105,templet: function(d){
          		return (d.sex==0?"女":"男");
          }}

          ,{field: 'auth', title: '作者',sort:'true', width:100}

          
          ,{fixed: 'right', width: 200,title:"操作", align:'left', toolbar: '#userListBar'}
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


	//  新增卡牌
	addCard:function(){
        $(".password").show();
		$("#userList").hide();
		$("#addUser").show();
		
         $("#id").val("0");
        $("#cardName").val("");
        $("#disc").val("");
        $("#pic").val("");
        $("#sex").val("");
        $("#atk").val("");
        $("#def").val("");
        $("#label").val("");
        $("#catagory1").val("0");
        $("#catagory2").val("0");
        $("#catagory3").val("0");
        $("#auth").val("");
        
        //$("#level").val("");
        // 重新渲染
        layui.form.render();
		$("#addCardTitle").empty();
		$("#addCardTitle").append("新增用户");
        
	},
	// 提交新增用户
	commit_addCard:function(){
		
		if($("#cardName").val()==""){
			layui.layer.alert("请输入名称");
			return;
		}
		if($("#disc").val()==""){
			layui.layer.alert("请输入描述");
			return;
		}
		//if("0" == $("#id").val() && $("#password").val()==""){
         //   layui.layer.alert("请输入密码");
        //    return;
        //}
		
		if($("#sex").val()==""){
            layui.layer.alert("请选择性别");
            return;
        }
        
       
        if($("#atk").val()==""){
			layui.layer.alert("请输入ATK");
            return;
        }
        
        if($("#def").val()==""){
			layui.layer.alert("请输入DEF");
            return;
        }
        if($("#label").val()==""){
			layui.layer.alert("请输入label");
            return;
        }
        if($("#auth").val()==""){
			layui.layer.alert("请输入作者作者");
            return;
        }
        
        

		$.ajax({
			url:request('/venne/updateCard'),
			data:{
				id:$("#id").val(),
				cardName:$("#cardName").val(),
				disc:$("#disc").val(),
				pic:$("#pic").val(),
				atk:$("#atk").val(),
				def:$("#def").val(),
				label:$("#label").val(),
				catagory1:$("#catagory1").val(),
				catagory2:$("#catagory2").val(),
				catagory3:$("#catagory3").val(),
				sex:$("#sex").val(),
				auth:$("#auth").val()		       
			},
			dataType:'json',
			async:false,
			success:function(result){
				if(result.resultCode==1){
					if($("#id").val()==0){
						layer.alert("添加成功");
                        $("#cardList").show();
                        $("#addCard").hide();
                        layui.table.reload("user_list",{
                            page: {
                                curr: 1 //重新从第 1 页开始
                            },
                            where: {
                            }
                        })

					}else{
						layer.alert("修改成功");
                        $("#cardList").show();
                        $("#addCard").hide();
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
        
        //$("#birthday").val("");
		myFn.invoke({
			url:request('/venne/getUpdateCard'),
			data:{
				id:data.id
			},
			success:function(result){
				if(result.data!=null){
					$("#id").val(result.data.id);
					$("#cardName").val(result.data.cardName);
					$("#disc").val(result.data.disc);
					$("#sex").val(result.data.sex);
					$("#atk").val(result.data.atk);
					$('#def').val(result.data.def);
					$('#pic').val(result.data.pic);
					$('#label').val(result.data.label);
					$('#catagory1').val(result.data.catagory1);
					$('#catagory2').val(result.data.catagory2);
					$('#catagory3').val(result.data.catagory3);
					$('#auth').val(result.data.auth);
				}
				$("#addCardTitle").empty();
				$("#addCardTitle").append("修改卡牌");
				$("#cardList").hide();
				$("#addCard").show();
                layui.form.render();
			}
		});

	},

        // 多选删除用户
        checkDeleteCards:function(){
            // 多选操作
            var checkStatus = layui.table.checkStatus('card_list'); //idTest 即为基础参数 id 对应的值
            
            
            
		for (var i = 0; i < checkStatus.data.length; i++){
			cardIds.push(checkStatus.data[i].id);
		}
		
		if(0 == checkStatus.data.length){
			layer.msg("请勾选要删除的行");
			return;
		}
		Card.checkDeleteCardsImpl(cardIds.join(","),checkStatus.data.length);
	},

    checkDeleteCardsImpl:function(id,checkLength){
        layer.confirm('确定删除指定卡牌',{icon:3, title:'提示消息',yes:function () {
                myFn.invoke({
                    url:request('/console/deleteCard'),
                    data:{
                        id:id
                    },
                    success:function(result){
                        if(result.resultCode==1){
                            layer.msg("删除成功",{"icon":1});
                            cardIds = [];
                            // renderTable();
                            Common.tableReload(currentCount,currentPageIndex,checkLength,"card_list");
                        }
                    }
                })
            },btn2:function () {
                cardIds = [];
            },cancel:function () {
                cardIds = [];
            }});
    },


    

   
    button_back:function(){

  		$("#cardList").show();
  		$("#card_table").show();
      $(".card_btn_div").show();
  		$("#autoCreateCard").hide();
  		
  		$("#addCard").hide();
      
	},
 

}