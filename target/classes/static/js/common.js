/**
 * 公共的 js
 */
var ivKey=[1,2,3,4,5,6,7,8];
var iv=getStrFromBytes(ivKey);
var ConfigData=null;
function getStrFromBytes (arr) {
    var r = "";
    for(var i=0;i<arr.length;i++){
        r += String.fromCharCode(arr[i]);
    }
    return r;
}
function request(url){
	// localStorage.getItem("access_token");
	// localStorage.getItem("apiKey");
	// var myDate = new Date();//获取系统当前时间
	var time=new Date().getTime();
	/*console.log(time);
	console.log("userId:"+localStorage.getItem("userId"));
	console.log("apiKey:"+localStorage.getItem("apiKey"));*/
	/*console.log(localStorage.getItem("access_Token"));*/
	var obj=localStorage.getItem("apiKey")+time+localStorage.getItem("userId")+localStorage.getItem("access_Token");
	url=url+"?secret="+$.md5(obj)+"&time="+time;
	return url;
}
var Config={
	getConfig:function(){
		if(ConfigData==null){
			$.ajax({
				type:'POST',
				url:'/config',
				data:{},
				async:false,
				success:function(result){
					ConfigData=result.data;
				}
			})
		}
		console.log(ConfigData);
		return ConfigData;
	}
}

var Common = {
	// layui table重载刷新 解决零界值删除不刷新到上页的问题(支持删除，多选删除)
	// 	currentCount ：当前table总数量, currentPageIndex ： 当前页数, checkLength ：选中的个数, tableId : table的ID){
	tableReload : function(currentCount,currentPageIndex,checkLength,tableId){
		var remainderIndex = (currentCount - checkLength) % Common.limit;
        console.log("currentCount : "+currentCount+"  checkLength : "+checkLength+"  Common.limit : "+Common.limit+"  currentPageIndex : "+currentPageIndex+"  remainderIndex : "+remainderIndex);
        if(0 == remainderIndex)
            currentPageIndex = currentPageIndex - 1;
        layui.table.reload(tableId,{
            page: {
                curr: currentPageIndex, //重新从当前页开始
            }
		})
	},

	// 分页公共参数
    limit:15,
    limits:[15,50,100,1000,10000],

	/** 调用接口通用方法  */
	invoke : function(obj){
		jQuery.support.cors = true;
		layer.load(1); //显示等待框
		var params = {
			type : "POST",
			url : obj.path,
			data : obj.data,
			contentType : 'application/x-www-form-urlencoded; charset=UTF-8',
			dataType : 'JSON',
			success : function(result) {
				layer.closeAll('loading');
				if(1==result.resultCode){
					if(obj.successMsg!=false)
						layer.msg(obj.successMsg,{icon: 1});
					obj.successCb(result);//执行成功的回调函数					
				}else if(-1==result.resultCode){
					//缺少访问令牌
					layer.msg("缺少访问令牌",{icon: 3});
					window.location.href = "/html/login.html";
				}else{
					if(!Common.isNil(result.resultMsg))
						layer.msg(result.resultMsg,{icon: 2,time: 2000});
					else
						layer.msg(obj.errorMsg,{icon: 2,time: 2000});

					obj.errorCb(result);
				}
				return;
					
			},
			error : function(result) {
				layer.closeAll('loading');
				if(!Common.isNil(result.resultMsg)){
					layer.msg(result.resultMsg,{icon: 2});
				}else{
					layer.msg(obj.errorMsg,{icon: 2});
				}
				obj.errorCb(result);//执行失败的回调函数
				return;
			},
			complete : function(result) {
				layer.closeAll('loading');
			}
		}
		// params.data["access_token"] = myData.access_token;
		$.ajax(params);
	}
	,isNil : function(s) {
		return undefined == s || null == s || $.trim(s) == "" || $.trim(s) == "null";
	},
	formatDate : function (time,fmt,type) { //type : 类型 0:时间为秒  1:时间为毫秒
		var date = new Date((type==0?(time * 1000):time));
	    var o = {
	        "M+": date.getMonth() + 1, //月份 
	        "d+": date.getDate(), //日 
	        "h+": date.getHours(), //小时 
	        "m+": date.getMinutes(), //分 
	        "s+": date.getSeconds(), //秒 
	        "q+": Math.floor((date.getMonth() + 3) / 3), //季度 
	        "S": date.getMilliseconds() //毫秒 
	    };
	    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
	    for (var k in o)
	    	if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
	    return fmt;
	},

}; /*End Common*/