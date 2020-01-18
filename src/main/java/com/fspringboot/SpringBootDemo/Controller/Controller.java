
package com.fspringboot.SpringBootDemo.Controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.fspringboot.SpringBootDemo.entity.User;

@RestController
@EnableAutoConfiguration
@RequestMapping("/venne")
public class Controller{
	
	@Autowired
	MongoTemplate mongotemplate;
	
	/**
     * 首页
     *
     * @return
     */
    @GetMapping({"/", "/index", "index.html"})
    public String index(HttpServletRequest request) {
    	return "web/index";
    }
	
	
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
	
	@RequestMapping("/addUser")
	public JSONObject helloWorld(@RequestParam(defaultValue = "0")String name,
								  @RequestParam(defaultValue = "0")int  age,
								  @RequestParam String password,
								  @RequestParam(defaultValue = "0")String phone,
								  @RequestParam(defaultValue = "0")int sex,
								  @RequestParam(defaultValue = "0")String username) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		//判断是否存在user
		Query query = new Query();
		query.addCriteria(Criteria.where("username").is(username));
		if(mongotemplate.findOne(query, User.class) != null) {
			map.put("resultCode", -1);
			map.put("data", "已存在用户");
			JSONObject object = (JSONObject) JSONObject.toJSON(map);
			return object;
		}
		
		User user = new User();
		user.setName(name);
		user.setAge(age);
		user.setPassword(password);
		user.setPhone(phone);
		user.setSex(sex);
		user.setUsername(username);
		mongotemplate.insert(user);
		
		map.put("resultCode", 1);
		JSONObject object = (JSONObject) JSONObject.toJSON(map);
		return object;
	}
	
	
}