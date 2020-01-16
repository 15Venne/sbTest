
package com.fspringboot.SpringBootDemo.Controller;

import java.util.HashMap;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

@RestController
@RequestMapping("/hello")
public class Controller{
	@RequestMapping("")
	public JSONObject hello() {
		
		JSON json;
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("resultCode", 1);
		map.put("data", "hello" +'\n' +"avc"+ '\n' + "world");
		JSONObject object = (JSONObject) JSONObject.toJSON(map);
		return object;
	}
	
	@RequestMapping("/world")
	public String helloWorld() {
		return "11223344\n444";
	}
}