package com.fspringboot.SpringBootDemo.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebController{
	
	@RequestMapping("/play")
	public String play() {
		return "index";
	}
}