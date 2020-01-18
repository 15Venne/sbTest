package com.fspringboot.SpringBootDemo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "card")
public class Card{
	
	@Id
	private String id;
	
	//卡牌名字
	@Field("cardName")
	private String cardName="";
	
	//类型1
	private String catagory1="";
	
	//类型2
	private String catagory2="";
	
	//类型3
	private String catagory3="";
	
	//描述
	private String disc = "";
	
	//ATK
	private Integer atk=0;
	
	//DEF
	private Integer def=0;
	
	//图片链接
	private String pic="";
	
	//
	private String turl="";
	
	//创建时间
	private Long createTime= 0L;
	
	private Integer pool= 2;
	
	//作者
	private String auth="";
	
	//是否启用
	private boolean isOpen=true;
	
	//拥有人数
	private Integer ownCount=0;
	
	//性别？
	private Integer sex=0;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getCardName(){
		return cardName;
	}
	
	public void setCardName(String cardName) {
		this.cardName = cardName;
	}
	
	public String getCatagory1() {
		return catagory1;
	}
	
	public void setCatagory1(String catagory1) {
		this.catagory1 = catagory1;
	}
	
	public String getCatagory2() {
		return catagory2;
	}
	
	public void setCatagory2(String catagory2) {
		this.catagory2 = catagory2;
	}
	
	public String getCatagory3() {
		return catagory3;
	}
	
	public void setCatagory3(String catagory3) {
		this.catagory3 = catagory3;
	}
	
	public String getDisc() {
		return disc;
	}
	
	public void setDisc(String disc) {
		this.disc = disc;
	}
	
	public int getSex() {
		return sex;
	}
	
	public void setSex(int sex) {
		this.sex = sex;
	}
	
	public int getAtk() {
		return atk;
	}
	
	public void setAtk(int atk) {
		this.atk = atk;
	}
	
	public int getDef() {
		return def;
	}
	
	public void setDef(int def) {
		this.def = def;
	}
	
	public String getPic() {
		return pic;
	}
	
	public void setPic(String pic) {
		this.pic = pic;
	}
	
	public String getTurl() {
		return turl;
	}
	
	public void setTurl(String turl) {
		this.turl = turl;
	}
	
	public Long getCreateTime() {
		return createTime;
	}
	
	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}
	
	public String getAuth() {
		return auth;
	}
	
	public void setAuth(String auth) {
		this.auth = auth;
	}
	
	
	public boolean getIsOpen() {
		return isOpen;
	}
	public void setIsOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}
	
	public Integer getOwnCount() {
		return ownCount;
	}
	
	public void setOwnCount(Integer ownCount) {
		this.ownCount = ownCount;
	}
	
}