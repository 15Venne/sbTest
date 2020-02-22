var page=0;
var lock=0;
var poolIds = new Array();

var poolName;
var id;

var currentPageIndex;// 当前页码数
var currentCount;// 当前总数

document.getElementById("divImage").style.display = "none";

layui.use(['form','layer','laydate','table','laytpl'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        laydate = layui.laydate,
        laytpl = layui.laytpl,
        table = layui.table;

    
    //console.log("userList_bigin");

	  //卡池列表
    var tableInPool = table.render({
      elem: '#pool_list'
      ,toolbar: '#toolbarPools'
      ,url:request("/venne/poolList")
      ,id: 'pool_list'
      ,page: true
      ,curr: 0
      ,limit:Common.limit
      ,limits:Common.limits
      ,groups: 7,
      style:''
      ,cols: [[ //表头
           {type:'checkbox',fixed:'left'}// 多选
          ,{field: 'id', title: '卡池id',sort:'true', width:100}
          ,{field: 'pic', title: '封面',sort:'true', width:70, templet:'<div ><img src="{{d.pic}}" id="{{d.id}}"  onmouseover="over(id,divImage,imgbig)" onmouseout="out()"  alt="" style="max-width:100%;height:auto;"></div>'}
          ,{field: 'poolName', title: '名称',sort:'true', width:145}
          ,{field: 'type', title: '类型',sort:'true', width:100,templet: function(d){
      		if(d.type == 1){
      			return "卡组池";
      		}else if(d.type == 2){
      			return "N池";
      		}else if(d.type == 3){
      			return "R池";
      		}else if(d.type == 4){
      			return "SR池";
      		}else if(d.type == 5){
      			return "SSR池";
      		}else{
      			return "unknow";
      		}
          }}
          ,{field: 'cnt', title: '当前卡牌/池数量',sort:'true', width:100}
          ,{field: 'maxCnt', title: '最大卡牌/池数量',sort:'true', width:100}
          ,{field: 'ratered', title: '红概率',sort:'true', width:100}
          ,{field: 'rateblue', title: '蓝概率',sort:'true', width:100}
          ,{field: 'rategreen', title: '绿概率',sort:'true', width:100}
          ,{field: 'ratessr', title: 'SSR概率',sort:'true', width:100}
          ,{field: 'ratesr', title: 'SR概率',sort:'true', width:100}
          ,{field: 'rater', title: 'R概率',sort:'true', width:100}
          ,{field: 'raten', title: 'N概率',sort:'true', width:100}
          
          ,{fixed: 'right', width: 200,title:"操作", align:'left', toolbar: '#poolListBar'}
        ]]
		  ,done:function(res, curr, count){
               if(count==0&&lock==1){
                 layer.msg("暂无数据",{"icon":2});
                 renderTable();
               }
               lock=0;
                $("#poolList").show();
                $("#pool_table").show();
                $("#addPool").hide();
               // $("#autoCreateUser").hide();
                //$("#exportUser").hide();
                 
                var pageIndex = tableInPool.config.page.curr;//获取当前页码
                var resCount = res.count;// 获取table总条数
                currentCount = resCount;
                currentPageIndex = pageIndex;
		  }

    });
    $(".poolName").val('');

  //列表操作
  table.on('tool(pool_list)', function(obj){
      var layEvent = obj.event,
            data = obj.data;
             
      if(layEvent === 'delete'){ //删除  
             
          Pool.checkDeletePoolsImpl(data.id,1);
      }else if(layEvent === 'update'){// 修改卡牌
          
          Pool.updatePool(obj.data,obj.data.id);
      }else if(layEvent==='updateMap'){ //池管理
    	  poolName = data.poolName;
     	  id = data.id; //给当前操作的那一行数据对应的用户的userId 记录，后面使用
        $(".disUserName").val(data.poolName);
		      $(".disUserName").empty();
        $(".disUserName").append($(".disUserName").val());// 好友管理中的小标题用户昵称
        var tableInsFriends = table.render({
             elem: '#myPools_table'
             ,toolbar: '#toolbarPoolsCards'
             ,url:request("/venne/cardMap")+"&id="+data.id
             ,id: 'myPools_table'
             ,page: true
             ,curr: 0
             ,limit:Common.limit
             ,limits:Common.limits
             ,groups: 7
             ,cols: [[ //表头
                  {type:'checkbox',fixed:'left'}// 多选
                 ,{field: 'toUserId', title: '好友Id', width:120,sort: true}
                 ,{field: 'toNickname', title: '好友昵称', width:150,sort: true}
                 ,{field: 'status', title: '状态',sort: true, width:120,templet : function (d) {
                 		if(d.status == -1)
                            return "黑名单";
                 		else if(d.status == 1)
                            return "关注"
                        else if(d.status == 2)
                            return "好友";
				           }}
                 ,{field: 'blacklist', title: '是否拉黑', width:120,sort: true,templet : function (d) {
                         if(d.blacklist == 1)
                             return "是";
                         else
                             return "否";
                     }}
                 ,{field: 'isBeenBlack', title: '是否被拉黑', width:120,sort: true,templet : function (d) {
                         if(d.isBeenBlack == 1)
                             return "是";
                         else
                             return "否";
                     }}
                 ,{field: 'offlineNoPushMsg', title: '是否消息免打扰',width: 150,sort: true,templet : function (d) {
                         if(d.offlineNoPushMsg == 1)
                             return "是";
                         else
                             return "否";
                     }}
                 ,{field: 'createTime', title: '成为好友的时间',width: 170,sort: true,templet: function(d){
                         return UI.getLocalTime(d.createTime);}}
                 ,{fixed: 'right', width: 250,title:"操作", align:'left', toolbar: '#delFriends'}
              ]]
             ,done:function(res, curr, count){
                 $("#user_table").hide();
                 $("#myFriends").show();
                 $(".user_btn_div").hide();
                 if(localStorage.getItem("role")!=6){
                     $(".friendsInfo").hide();
                 }
                var pageIndex = tableInsFriends.config.page.curr;//获取当前页码
                var resCount = res.count;// 获取table总条数
                currentCount = resCount;
                currentPageIndex = pageIndex;
             }
        });

  
      }

  });

    //搜索用户
    $(".search_pool").on("click",function(){
        // 关闭超出宽度的弹窗
        $(".layui-layer-content").remove();

        table.reload("pool_list",{
            page: {
                curr: 1 //重新从第 1 页开始
            },
            where: {      
                keyWorld : $(".poolName").val()  //搜索的关键字
            }
        })
        lock=1;
        $(".poolName").val('');
        
    });
    
    var rare1pre = "";
    
  //按rare筛选pool
    $(".search_pool1").on("click",function(){
        // 关闭超出宽度的弹窗
        $(".layui-layer-content").remove();
        if($("#rare1").val() == rare1pre){
        	return ;
        }
        rare1pre = $("#rare1").val();
        table.reload("pool_list",{
            page: {
                curr: 1 //重新从第 1 页开始
            },
            where: {
                type:$("#rare1").val(),// type筛选
               
            }
        })
        lock=1;
        $(".poolName").val('');
        
    });

})

	 //layui END



function over(imgid,obj,imgbig){
	var imgsrc = document.getElementById(imgid);	
	maxwidth=400;
	maxheight=300;
	document.getElementById('divImage').style.display="";
    imgbig.src=imgsrc.src;        
}

function out()
{
	document.getElementById('divImage').style.display="none";
}

function adapt(imgid){
  var tableWidth = $("#imgTable").width(); //表格宽度
  var tableHeight = $("#imgTable").height(); //表格高度
  var img = new Image();
  img.src =$(imgid).attr("src") ;
  var imgWidth = img.width; //图片实际宽度
  var imgHeight = img.height;//图片实际高度
  if(imgWidth<tableWidth){
	  $(imgid).attr("style","width: auto");
  }else{
	  $(imgid).attr("style","width: 100%");
  }
}


//重新渲染表单
function renderTable(){
  layui.use('table', function(){
   var table = layui.table;//高版本建议把括号去掉，有的低版本，需要加()
   
   table.reload("pool_list",{
        page: {
            curr: 1 //重新从第 1 页开始
        },
        where: {
            keyWorld : $(".poolName").val()  //搜索的关键字
        }
    })
  });
 }



var Pool={
	pool_list:function(e,pageSize){
		var html="";
		if(e==undefined){
			e=0;
		}else if(pageSize==undefined){
			pageSize=10;
		}
		$.ajax({
			type:'POST',
			url:request('/venne/poolList'),
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

					$("#poolList_table").empty();
					$("#poolList_table").append(html);
					$("#poolList").show();
					$("#pool_table").show();
					$("#addPool").hide();
					
				}

			}
		})
	},


	
	
	//  新增卡池
	addPool:function(){  
		$("#poolList").hide();
		$("#addPool").show();	
		
        $("#id").val("0");
        $("#poolName").val("");             
        $("#pic").val("");                   
        $("#type").val("");
        $("#maxCnt").val("");
                          
        // 重新渲染
        layui.form.render();
		$("#addPoolTitle").empty();
		$("#addPoolTitle").append("新增用户");
        
	},
	// 提交新增卡池
	commit_addPool:function(){
		
		if($("#poolName").val()==""){
			layui.layer.alert("请输入名称");
			return;
		}		
        
        if($("#type").val()==""){
			layui.layer.alert("请选择池类型");
            return;
        }
        if($("#maxCnt").val()==""){
			layui.layer.alert("请输入上限");
            return;
        }
        
        

		$.ajax({
			url:request('/venne/updatePool'),
			data:{
				id:$("#id").val(),
				poolName:$("#poolName").val(),
				pic:$("#pic").val(),							
				type:$("#type").val(),
				maxCnt:$("#maxCnt").val()       
			},
			dataType:'json',
			async:false,
			success:function(result){
				if(result.resultCode==1){
					if($("#id").val()==0){
						layer.alert("添加成功");
                        $("#poolList").show();
                        $("#addPool").hide();
                        layui.table.reload("pool_list",{
                            page: {
                                curr: 1 //重新从第 1 页开始
                            },
                            where: {
                            }
                        })

					}else{
						layer.alert("修改成功");
                        $("#poolList").show();
                        $("#addPool").hide();
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
	// 修改卡池
	updatePool:function(data,id){
        
        //$("#birthday").val("");
		myFn.invoke({
			url:request('/venne/getUpdatePool'),
			data:{
				id:data.id
			},
			success:function(result){
				if(result.data!=null){
					$("#id").val(result.data.id);
					$("#poolName").val(result.data.poolName);				
					$('#pic').val(result.data.pic);
					$('#type').val(result.data.type);
					$('#maxCnt').val(result.data.maxCnt);					
				}
				$("#addPoolTitle").empty();
				$("#addPoolTitle").append("修改卡牌");
				$("#poolList").hide();
				$("#addPool").show();
                layui.form.render();
			}
		});

	},

        // 多选删除卡池
        checkDeletePools:function(){
            // 多选操作
            var checkStatus = layui.table.checkStatus('pool_list'); //idTest 即为基础参数 id 对应的值
            
            
            
		for (var i = 0; i < checkStatus.data.length; i++){
			poolIds.push(checkStatus.data[i].id);
		}
		
		if(0 == checkStatus.data.length){
			layer.msg("请勾选要删除的行");
			return;
		}
		Pool.checkDeletePoolsImpl(poolIds.join(","),checkStatus.data.length);
	},

    checkDeletePoolsImpl:function(id,checkLength){
        layer.confirm('确定删除指定卡池',{icon:3, title:'提示消息',yes:function () {
                myFn.invoke({
                    url:request('/venne/deletePool'),
                    data:{
                        id:id
                    },
                    success:function(result){
                        if(result.resultCode==1){
                            layer.msg("删除成功",{"icon":1});
                            poolIds = [];
                            // renderTable();
                            Common.tableReload(currentCount,currentPageIndex,checkLength,"pool_list");
                        }
                    }
                })
            },btn2:function () {
                poolIds = [];
            },cancel:function () {
                poolIds = [];
            }});
    },


    

   
    button_back:function(){

  		$("#poolList").show();
  		$("#pool_table").show();
      $(".pool_btn_div").show();
  		
  		
  		$("#addPool").hide();
      
	},
 

}

