
package com.fspringboot.SpringBootDemo.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


import com.fspringboot.SpringBootDemo.entity.Card;
import com.fspringboot.SpringBootDemo.entity.Pool;
import com.fspringboot.SpringBootDemo.entity.Pool.CardMap;
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
		if(this.mongotemplate.findOne(query, User.class) != null) {
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
	
	/*******************************card*****************************************************/
	@RequestMapping("/cardList")
	public JSONObject cardList(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int limit, @RequestParam(defaultValue = "0") int rare,
			@RequestParam(defaultValue = "lazy") String catagory1, @RequestParam(defaultValue = "lazy") String catagory2,
			@RequestParam(defaultValue = "lazy") String catagory3) {
		
		System.out.println(page);
		System.out.println(limit);
		
		System.out.println("rare: " + rare);
		Criteria criteria = new Criteria();
		Query query = new Query(criteria);	
		if(rare != 0) {
			query.addCriteria(Criteria.where("rare").is(rare));
		}
		if(!catagory1.equals("lazy")) {
			query.addCriteria(Criteria.where("catagory1").is(catagory1));
		}
		if(!catagory2.equals("lazy")) {
			query.addCriteria(Criteria.where("catagory2").is(catagory2));
		}
		if(!catagory3.equals("lazy")) {
			query.addCriteria(Criteria.where("catagory3").is(catagory3));
		}
		
		query.skip((page - 1) * limit);
		query.limit(limit); 
		
		List<Card> cards = this.mongotemplate.find(query, Card.class);
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("resultCode", 1);
		
		map.put("data",cards);
		JSONObject object = (JSONObject) JSONObject.toJSON(map);
		System.out.println("card_List");
		System.out.println(cards);
		return object;
	}
	
	@RequestMapping("/updateCard")
	public JSONObject updateCard(@RequestParam(defaultValue = "") String id, @RequestParam(defaultValue = "") String disc,
								@RequestParam(defaultValue = "") String cardName,@RequestParam(defaultValue = "") int label,
								@RequestParam(defaultValue = "") String pic,@RequestParam(defaultValue = "") int sex,
								@RequestParam(defaultValue = "") int atk, @RequestParam(defaultValue = "") int def,
								@RequestParam(defaultValue = "") String catagory1, @RequestParam(defaultValue = "") String catagory2,
								@RequestParam(defaultValue = "") String catagory3, @RequestParam(defaultValue = "") String auth,
								@RequestParam(defaultValue = "") int rare) {
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		System.out.println("id: " + id);
		System.out.println("label: " + label);
		
		if(id.equals("0")) {
			//注册
			//判断是否存在card
			Query query = new Query();
			query.addCriteria(Criteria.where("cardName").is(cardName));
			if(mongotemplate.findOne(query, User.class) != null) {
				map.put("resultCode", -1);
				map.put("data", "已存在同名卡牌");
				JSONObject object = (JSONObject) JSONObject.toJSON(map);
				return object;
			}
			
			Card card = new Card();
			card.setCardName(cardName);
			card.setDisc(disc);
			card.setPic(pic);
			card.setRare(rare);
			card.setAtk(atk);
			card.setDef(def);
			card.setSex(sex);
			card.setLabel(label);
			card.setCreateTime(System.currentTimeMillis() / 1000);
			card.setCatagory2(catagory2);
			card.setCatagory1(catagory1);
			card.setCatagory3(catagory3);
			card.setAuth(auth);
			card.setIsOpen(true);
			card.setOwnCount(0);
			card.setUpdateTime(System.currentTimeMillis() / 1000);
			
			mongotemplate.insert(card);
		}else {
			//修改
			Update update = new Update();
			update.set("cardName", cardName);
			update.set("disc", disc);
			update.set("pic", pic);
			update.set("atk", atk);
			update.set("def", def);
			update.set("rare", rare);
			update.set("label", label);
			update.set("sex", sex);
			update.set("catagory1", catagory1);
			update.set("catagory2", catagory2);
			update.set("catagory3", catagory3);
			update.set("updateTime", System.currentTimeMillis() / 1000);
			
			Query query = new Query();
			query.addCriteria(Criteria.where("id").is(id));
			if(mongotemplate.findOne(query, Card.class) == null) {
				map.put("resultCode", -1);
				HashMap<String, Object> mapFail = new HashMap<String, Object>();
				mapFail.put("resultMsg","不存在card");
				JSONObject objectFail = (JSONObject) JSONObject.toJSON(mapFail);
				map.put("data", objectFail);
				JSONObject object = (JSONObject) JSONObject.toJSON(map);
				return object;
			}
			mongotemplate.findAndModify(query, update, Card.class);
		}
		
		
		map.put("resultCode", 1);
		JSONObject object = (JSONObject) JSONObject.toJSON(map);
		System.out.println("updateCard");
		return object;
	}
	
	@RequestMapping("/getUpdateCard")
	public JSONObject getUpdateCard(@RequestParam(defaultValue = "") String id) {
		
		System.out.println(id);
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("resultCode", 1);
		
		
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(id));
		if(mongotemplate.findOne(query, Card.class) != null) {
			Card card = mongotemplate.findOne(query, Card.class);
			map.put("data",card);
			
			String cardName = mongotemplate.findOne(query, Card.class).getCardName();
			System.out.println("name: " + cardName);
		}else {
			map.put("data","");
		}
		
		JSONObject object = (JSONObject) JSONObject.toJSON(map);
		return object;
	}
	
	@RequestMapping("/deleteCard")
	public JSONObject deleteCard() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("resultCode", 1);
		
		JSONObject object = (JSONObject) JSONObject.toJSON(map);
		System.out.println("card_deleteCard");
		return object;
	}
	
	
	/*******************************card*****************************************************/
	
	/*******************************pool*****************************************************/
	@RequestMapping("/poolList")
	public JSONObject poolList(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int limit, @RequestParam(defaultValue = "0") int type) {
		
		System.out.println(page);
		System.out.println(limit);
		
		System.out.println("type: " + type);
		Criteria criteria = new Criteria();
		Query query = new Query(criteria);	
		if(type != 0) {
			query.addCriteria(Criteria.where("type").is(type));
		}
		
		query.skip((page - 1) * limit);
		query.limit(limit); 
		
		List<Pool> pools = this.mongotemplate.find(query, Pool.class);
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("resultCode", 1);
		
		map.put("data",pools);
		JSONObject object = (JSONObject) JSONObject.toJSON(map);
		System.out.println("pool_List");
		System.out.println(pools);
		return object;
	}
	
	@RequestMapping("/updatePool")
	public JSONObject updatePool(@RequestParam(defaultValue = "") String id,
								@RequestParam(defaultValue = "") String poolName, @RequestParam(defaultValue = "") int type,
								@RequestParam(defaultValue = "") String pic, @RequestParam(defaultValue = "") int maxCnt) {
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		System.out.println("id: " + id);
		System.out.println("label: " + type);
		
		if(id.equals("0")) {
			//注册
			//判断是否存在card
			Query query = new Query();
			query.addCriteria(Criteria.where("poolName").is(poolName));
			if(mongotemplate.findOne(query, Pool.class) != null) {
				map.put("resultCode", -1);
				map.put("data", "已存在同名卡池");
				JSONObject object = (JSONObject) JSONObject.toJSON(map);
				return object;
			}
			
			Pool pool = new Pool();
			pool.setPoolName(poolName);		
			pool.setPic(pic);
			pool.setMaxCnt(maxCnt);
			pool.setType(type);
			pool.setCnt(0);
			pool.setIsOpen(true);
			pool.setCreateTime(System.currentTimeMillis() / 1000);
			pool.setUpdateTime(System.currentTimeMillis() / 1000);
			
			pool.setRaten(0);
			pool.setRater(0);
			pool.setRatesr(0);
			pool.setRatessr(0);
			pool.setRateblue(0);
			pool.setRategreen(0);
			pool.setRatered(0);
			
			List<CardMap> cardMaps = new ArrayList<CardMap>();
			pool.setCardMap(cardMaps);
			
			
			mongotemplate.insert(pool);
		}else {
			//修改
			Update update = new Update();
			update.set("poolName", poolName);
			update.set("pic", pic);
			update.set("maxCnt", maxCnt);
			update.set("type", type);
			
			update.set("updateTime", System.currentTimeMillis() / 1000);
			
			Query query = new Query();
			query.addCriteria(Criteria.where("id").is(id));
			if(mongotemplate.findOne(query, Pool.class) == null) {
				map.put("resultCode", -1);
				HashMap<String, Object> mapFail = new HashMap<String, Object>();
				mapFail.put("resultMsg","不存在card");
				JSONObject objectFail = (JSONObject) JSONObject.toJSON(mapFail);
				map.put("data", objectFail);
				JSONObject object = (JSONObject) JSONObject.toJSON(map);
				return object;
			}
			mongotemplate.findAndModify(query, update, Pool.class);
		}
		
		
		map.put("resultCode", 1);
		JSONObject object = (JSONObject) JSONObject.toJSON(map);
		System.out.println("updateCard");
		return object;
	}
	
	@RequestMapping("/getUpdatePool")
	public JSONObject getUpdatePool(@RequestParam(defaultValue = "") String id) {
		
		System.out.println(id);
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("resultCode", 1);
		
		
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(id));
		if(mongotemplate.findOne(query, Pool.class) != null) {
			Pool pool = mongotemplate.findOne(query, Pool.class);
			map.put("data",pool);
			
			String poolName = mongotemplate.findOne(query, Pool.class).getPoolName();
			System.out.println("name: " + poolName);
		}else {
			map.put("data","");
		}
		
		JSONObject object = (JSONObject) JSONObject.toJSON(map);
		return object;
	}
	
	@RequestMapping("/cardMap")
	public JSONObject getCardMap(@RequestParam(defaultValue = "") String id,
								  @RequestParam(defaultValue = "0") int page,
								  @RequestParam(defaultValue = "10") int limit) {
		
		System.out.println("id: " + id);
		System.out.println("page: " + page);
		System.out.println("limit: " + limit);
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("resultCode", 1);
		
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(id));
		if(mongotemplate.findOne(query, Pool.class) != null) {
			Pool pool = mongotemplate.findOne(query, Pool.class);
			
			
			List<CardMap> cardMap = mongotemplate.findOne(query, Pool.class).getCardMap();
			map.put("data",cardMap);
			System.out.println("cardMap: " + cardMap);
		}else {
			map.put("data","");
		}
		
		JSONObject object = (JSONObject) JSONObject.toJSON(map);
		System.out.println("card_Map");
		return object;
	}
	
	@RequestMapping("/updatePoolCardMap")
	public JSONObject updatePoolCardMap(@RequestParam(defaultValue = "") String poolId,@RequestParam(defaultValue = "") int type,
								@RequestParam(defaultValue = "") String cardId, @RequestParam(defaultValue = "") int cnt) {
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		System.out.println("poolid: " + poolId);
		System.out.println("cardid: " + cardId);
		
		//找出pool
		Query queryPool = new Query();
		queryPool.addCriteria(Criteria.where("id").is(poolId));
		if(mongotemplate.findOne(queryPool, Pool.class) == null) {
			map.put("resultCode", -1);
			HashMap<String, Object> mapFail = new HashMap<String, Object>();
			mapFail.put("resultMsg","不存在pool");
			JSONObject objectFail = (JSONObject) JSONObject.toJSON(mapFail);
			map.put("data", objectFail);
			JSONObject object = (JSONObject) JSONObject.toJSON(map);
			return object;
		}
		Pool pool = mongotemplate.findOne(queryPool, Pool.class);
		Integer getType = pool.getType();
		if(!getType.equals(type)) {
			//type不统一
			map.put("resultCode", -1);
			HashMap<String, Object> mapFail = new HashMap<String, Object>();
			mapFail.put("resultMsg","池的类型错误");
			JSONObject objectFail = (JSONObject) JSONObject.toJSON(mapFail);
			map.put("data", objectFail);
			JSONObject object = (JSONObject) JSONObject.toJSON(map);
			return object;
		}
		
		List<CardMap> cardMapData = pool.getCardMap();
		
		String targetId = cardId;
		String targetName;
		String targetPic;
		Integer targetRare;
		Integer targetRate = cnt;
		
		//找出card
		Query queryCard = new Query();
		queryCard.addCriteria(Criteria.where("id").is(cardId));
		if(type == 1) {//cardId仍为一个pool的Id,但type为2以上
			if(mongotemplate.findOne(queryCard, Pool.class) == null || mongotemplate.findOne(queryCard, Pool.class).getType() < 2) {
				map.put("resultCode", -1);
				HashMap<String, Object> mapFail = new HashMap<String, Object>();
				mapFail.put("resultMsg","不存在pool(2)");
				JSONObject objectFail = (JSONObject) JSONObject.toJSON(mapFail);
				map.put("data", objectFail);
				JSONObject object = (JSONObject) JSONObject.toJSON(map);
				return object;
			}
			Pool pool2 = mongotemplate.findOne(queryCard, Pool.class);
			
			targetName = pool2.getPoolName();
			targetPic = pool2.getPic();
			targetRare = pool2.getType() - 1; // 1,N,2,R,3,SR,4,SSR
		}else { // cardId为一张卡牌,且稀有度为type-1
			if(mongotemplate.findOne(queryCard, Card.class) == null || mongotemplate.findOne(queryCard, Card.class).getRare() != type -1){
				map.put("resultCode", -1);
				HashMap<String, Object> mapFail = new HashMap<String, Object>();
				mapFail.put("resultMsg","不存在card或稀有度错误");
				JSONObject objectFail = (JSONObject) JSONObject.toJSON(mapFail);
				map.put("data", objectFail);
				JSONObject object = (JSONObject) JSONObject.toJSON(map);
				return object;
			}
			Card card = mongotemplate.findOne(queryCard, Card.class);
			targetName = card.getCardName();
			targetPic = card.getPic();
			targetRare = card.getRare();
			
		}
		
		//新建cardmap
		CardMap targetCardMap = new CardMap();
		targetCardMap.setCardId(targetId);
		targetCardMap.setName(targetName);
		targetCardMap.setCardMapPic(targetPic);
		targetCardMap.setRare(targetRare);
		targetCardMap.setRate(targetRate);
		
		//插入列表
		cardMapData.add(targetCardMap);
		
		
		//修改数据库
		Update update = new Update();
		update.set("cardMap", cardMapData);
		if(type == 1) { // 卡组池，修改ratessr,ratesr,rater,raten
			
			Integer typeP = mongotemplate.findOne(queryCard, Pool.class).getType();
			if(typeP == 2) {
				update.set("raten", mongotemplate.findOne(queryCard, Pool.class).getRaten() + 1);
			}else if(typeP == 3) {
				update.set("rater", mongotemplate.findOne(queryCard, Pool.class).getRater() + 1);
			}else if(typeP == 4) {
				update.set("ratesr", mongotemplate.findOne(queryCard, Pool.class).getRatesr() + 1);
			}else if(typeP == 5) {
				update.set("ratessr", mongotemplate.findOne(queryCard, Pool.class).getRatessr() + 1);
			}
			
			
		}else {// 卡牌池，修改ratered,rateblue,rategreen
			
			String typeP = mongotemplate.findOne(queryCard, Card.class).getCatagory1();
			if(typeP.equals("blue")) {
				update.set("rateblue", mongotemplate.findOne(queryCard, Pool.class).getRateblue() + 1);
			}else if(typeP.equals("red")) {
				update.set("ratered", mongotemplate.findOne(queryCard, Pool.class).getRatered() + 1);
			}else if(typeP.equals("green")) {
				update.set("rategreen", mongotemplate.findOne(queryCard, Pool.class).getRategreen() + 1);
			}
			
		}
		update.set("cnt", pool.getCnt() + 1);	
			
		update.set("updateTime", System.currentTimeMillis() / 1000);
			
			Query query = new Query();
			query.addCriteria(Criteria.where("id").is(id));
			if(mongotemplate.findOne(query, Pool.class) == null) {
				map.put("resultCode", -1);
				HashMap<String, Object> mapFail = new HashMap<String, Object>();
				mapFail.put("resultMsg","不存在pool");
				JSONObject objectFail = (JSONObject) JSONObject.toJSON(mapFail);
				map.put("data", objectFail);
				JSONObject object = (JSONObject) JSONObject.toJSON(map);
				return object;
			}
			mongotemplate.findAndModify(query, update, Pool.class);
		
		
		
		map.put("resultCode", 1);
		JSONObject object = (JSONObject) JSONObject.toJSON(map);
		System.out.println("updateCard");
		return object;
	}
	
	
	@RequestMapping("/deletePool")
	public JSONObject deletePool() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("resultCode", 1);
		
		JSONObject object = (JSONObject) JSONObject.toJSON(map);
		System.out.println("card_deleteCard");
		return object;
	}
	/*******************************pool*****************************************************/
}