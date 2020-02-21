package com.fspringboot.SpringBootDemo.entity;

import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "pool")
public class Pool{
	
	@Id
	private String id;
	
	//卡池名字
	@Field("poolName")
	private String poolName="";
	
	private boolean isOpen = true;
	
	private String pic;
			
	private Long createTime;
			
	private Long updateTime;
			
	/*卡池类型
	 * 1，卡组池，cardMap是卡池-概率
	 * 2，N池
	 * 3，R池
	 * 4，SR池
	 * 5，SSR池
	*/
	private Integer type;
	
	
	
	//卡牌/卡池
	//概率：1~1000
	private Map<String, Integer> cardMap;
	
	private Integer maxCnt = 1000;
	
	//卡数/卡组数，修改cardMap后修改
	private Integer cnt = 0;
	
	//必须通过修改cardMap设置
	private Integer ratessr;
	private Integer ratesr;
	private Integer rater;
	private Integer raten;
	
	private Integer ratered;
	private Integer rateblue;
	private Integer rategreen;
	
	/***************************************/
	
	public Integer getRategreen() {
		return rategreen;
	}
	public void setRategreen(Integer rategreen) {
		this.rategreen = rategreen;
	}
	public Integer getRateblue() {
		return rateblue;
	}
	public void setRateblue(Integer rateblue) {
		this.rateblue = rateblue;
	}
	public Integer getRatered(){
		return ratered;
	}
	public void setRatered(Integer ratered) {
		this.ratered = ratered;
	}
	
	public Integer getRaten() {
		return raten;
	}
	public void setRaten(Integer raten) {
		this.raten = raten;
	}
	public Integer getRater() {
		return rater;
	}
	public void setRater(Integer rater) {
		this.rater = rater;
	}
	public Integer getRatesr() {
		return ratesr;
	}
	public void setRatesr(Integer ratesr) {
		this.ratesr = ratesr;
	}
	public Integer getRatessr() {
		return ratessr;
	}
	public void setRatessr(Integer ratessr) {
		this.ratessr = ratessr;
	}
	
	public Integer getType() {
		return type;
	}
	
	public void setType(Integer type) {
		this.type = type;
	}
	
	public Map<String, Integer> getCardMap(){
		return cardMap;
	}
	
	public void setCardMap(Map<String, Integer> cardMap) {
		this.cardMap = cardMap;
	}
	
	public Integer getMaxCnt() {
		return maxCnt;
	}
	
	public void setMaxCnt(Integer maxCnt) {
		this.maxCnt = maxCnt;
	}
	
	public Integer getCnt() {
		return cnt;
	}
	
	public void setCnt(Integer cnt) {
		this.cnt = cnt;
	}
	
	public String getPic() {
		return pic;
	}
	
	public void setPic(String pic) {
		this.pic = pic;
	}
	
	public Long getCreateTime() {
		return createTime;
	}
	
	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}
	
	public Long getUpdateTime() {
		return updateTime;
	}
	
	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}
	
	public boolean getIsOpen() {
		return isOpen;
	}
	
	public void setIsOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getPoolName() {
		return poolName;
	}
	
	public void setPoolName(String poolName) {
		this.poolName = poolName;
	}
	
	
}