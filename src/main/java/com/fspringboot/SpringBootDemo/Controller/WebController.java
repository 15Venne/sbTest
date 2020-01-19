package com.fspringboot.SpringBootDemo.Controller;

import java.util.HashMap;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;

@Controller
public class WebController{
	
	@RequestMapping("/play")
	public String play() {
		return "index";
	}
	
	@RequestMapping("/play/login")
	public String login() {
		return "login";
	}
	
	
	
}