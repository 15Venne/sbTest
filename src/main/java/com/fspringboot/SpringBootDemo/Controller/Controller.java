
package com.fspringboot.SpringBootDemo.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
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
	
	@RequestMapping("/ping")
	public JSONObject ping() {
		
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("resultCode", 1);
		
		JSONObject object = (JSONObject) JSONObject.toJSON(map);
		return object;
	}
	
	
	@RequestMapping("/getUser")
	public JSONObject getUser() {
		
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
	public JSONObject addUser(@RequestParam(defaultValue = "0")String name,
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
	
	@RequestMapping("/play/user/login")
	public JSONObject userLogin() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("resultCode", 1);
		
		JSONObject object = (JSONObject) JSONObject.toJSON(map);
		System.out.println("user_login");
		return object;
	}
	
	
	/*************************user***************************************************/
	@RequestMapping("/userList")
	public JSONObject userList(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int limit) {
		
		System.out.println(page);
		System.out.println(limit);
		
		Criteria criteria = new Criteria();
		
		Query query = new Query(criteria);
		
		query.skip((page - 1) * limit);
		query.limit(limit); 
		
		List<User> users = this.mongotemplate.find(query, User.class);
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("resultCode", 1);
		map.put("data",users);
		JSONObject object = (JSONObject) JSONObject.toJSON(map);
		System.out.println("user_List");
		System.out.println(users);
		return object;
	}
	
	@RequestMapping("/updateUser")
	public JSONObject updateUser(@RequestParam(defaultValue = "") String id, @RequestParam(defaultValue = "") String name,
								@RequestParam(defaultValue = "") String username,@RequestParam(defaultValue = "") int age,
								@RequestParam(defaultValue = "") String password,@RequestParam(defaultValue = "") int sex,
								@RequestParam(defaultValue = "") String phone, @RequestParam(defaultValue = "") String avatar) {
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		System.out.println("id: " + id);
		
		if(id.equals("0")) {
			//注册
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
			user.setCreateTime(System.currentTimeMillis() / 1000);
			user.setExperiences(0);
			user.setGachaponCount(10);
			user.setLevel(1);
			user.setUpdateTime(System.currentTimeMillis() / 1000);
			user.setYonney(0.5);
			user.setAvatarPic(avatar);
			
			mongotemplate.insert(user);
		}else {
			//修改
			Update update = new Update();
			update.set("name", name);
			update.set("age", age);
			update.set("phone", phone);
			update.set("sex", sex);
			update.set("updateTime", System.currentTimeMillis() / 1000);
			
			Query query = new Query();
			query.addCriteria(Criteria.where("id").is(id));
			if(mongotemplate.findOne(query, User.class) == null) {
				map.put("resultCode", -1);
				map.put("data", "不存在用户");
				JSONObject object = (JSONObject) JSONObject.toJSON(map);
				return object;
			}
			mongotemplate.findAndModify(query, update, User.class);
		}
		
		
		map.put("resultCode", 1);
		JSONObject object = (JSONObject) JSONObject.toJSON(map);
		System.out.println("user_updateUser");
		return object;
	}
	
	@RequestMapping("/getUpdateUser")
	public JSONObject getUpdateUser(@RequestParam(defaultValue = "") String id) {
		
		System.out.println(id);
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("resultCode", 1);
		
		
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(id));
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
	
	@RequestMapping("/deleteUser")
	public JSONObject deleteUser() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("resultCode", 1);
		
		JSONObject object = (JSONObject) JSONObject.toJSON(map);
		System.out.println("user_deleteUser");
		return object;
	}
	
	@RequestMapping("/recharge")
	public JSONObject recharge() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("resultCode", 1);
		
		JSONObject object = (JSONObject) JSONObject.toJSON(map);
		System.out.println("user_recharge");
		return object;
	}
	
	/*******************************user*****************************************************/
	
	/*******************************pool*****************************************************/
	
	/*******************************pool*****************************************************/
	
	/*******************************card*****************************************************/
	
	/*******************************card*****************************************************/
}