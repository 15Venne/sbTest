package com.fspringboot.SpringBootDemo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "user")
public class User{
	
	@Id
	private String id;
	
	@Field("username")
	private String username="";
	
	private String password="vv123456";
	
	private String registerTime="1970-01-01";
	
	private String phone="";
	
	private String name="";
	
	private String avatarPic = "";
	
	private Integer sex=0;
	
	private Integer age=0;
	
	private Double yonney;
	
	private Integer isBan=0;
	
	private Long createTime;
	
	private Long updateTime;
	
	private Long birthday;
	
	private Integer gachaponCount;
	
	private Integer level;
	
	private Integer experiences;
	
	/*************************************/
	
	public String getAvatarPic() {
		return avatarPic;
	}
	
	public void setAvatarPic(String avatarPic) {
		this.avatarPic = avatarPic;
	}
	
	public Integer getLevel() {
		return level;
	}
	
	public void setLevel(Integer level) {
		this.level = level;
	}
	
	public Integer getExperiences() {
		return experiences;
	}
	
	public void setExperiences(Integer experiences) {
		this.experiences = experiences;
	}
	
	public Integer getGachaponCount() {
		return gachaponCount;
	}
	
	public void setGachaponCount(Integer gachaponCount) {
		this.gachaponCount = gachaponCount;
	}
	
	public Long getBirthday() {
		return birthday;
	}
	
	public void setBrithday(Long birthday) {
		this.birthday = birthday;
	}
	
	public Long getUpdateTime() {
		return updateTime;
	}
	
	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}
	
	public Long getCreateTime() {
		return createTime;
	}
	
	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}
	
	public Integer getIsBan() {
		return isBan;
	}
	
	public void setIsBan(Integer isBan) {
		this.isBan = isBan;
	}
	
	public Double getYonney() {
		return yonney;
	}
	
	public void setYonney(Double yonney) {
		this.yonney = yonney;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getUsername(){
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getRegisterTime() {
		return registerTime;
	}
	
	public void setRegisterTime(String registerTime) {
		this.registerTime = registerTime;
	}
	
	public String getPhone() {
		return phone;
	}
	
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getSex() {
		return sex;
	}
	
	public void setSex(int sex) {
		this.sex = sex;
	}
	
	public int getAge() {
		return age;
	}
	
	public void setAge(int age) {
		this.age = age;
	}
}