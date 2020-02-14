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
	 * 2，
	 * 3，
	 * 4，
	 * 5，
	*/
	private Integer type;
	
	public Integer getType() {
		return type;
	}
	
	public void setType(Integer type) {
		this.type = type;
	}
	
	//卡牌/卡池
	//概率：1~1000
	private Map<String, Integer> cardMap;
	
	private Integer maxCnt = 1000;
	
	private Integer cnt = 0;
	
	/***************************************/
	
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