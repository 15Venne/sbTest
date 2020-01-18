
package com.fspringboot.SpringBootDemo.Controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.fspringboot.SpringBootDemo.entity.User;

@RestController
@EnableAutoConfiguration
@RequestMapping("/hello")
public class Controller{
	
	@Autowired
	MongoTemplate mongotemplate;
	
	
	@RequestMapping("/getUser")
	public JSONObject hello() {
		
		JSON json = new JSONObject();
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("resultCode", 1);
		
		
		Query query = new Query();
		query.addCriteria(Criteria.where("name").is("15venne"));
		if(mongotemplate.findOne(query, User.class) != null) {
			User user = mongotemplate.findOne(query, User.class);
			map.put("data",user);
			
			String name = mongotemplate.findOne(query, User.class).getName();
			System.out.println("name: " + name);
		}else {
			map.put("data","");
		}
		
		JSONObject object = (JSONObject) JSONObject.toJSON(map);
		return object;
	}
	
	@RequestMapping("/insert")
	public JSONObject helloWorld(@RequestParam(defaultValue = "0")String name) {
		User user = new User();
		
		user.setName(name);
		user.setAge(22);
		user.setPassword("456789");
		user.setPhone("13999999999");
		user.setSex(1);
		user.setUsername("v314652328");
		mongotemplate.insert(user);
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		map.put("resultCode", 1);
		JSONObject object = (JSONObject) JSONObject.toJSON(map);
		return object;
		
	}
}