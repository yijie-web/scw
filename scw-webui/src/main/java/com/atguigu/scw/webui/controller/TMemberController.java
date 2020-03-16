package com.atguigu.scw.webui.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TMemberController {

	@RequestMapping("/member/minecrowdfunding")
	public String myOrderList() {
		return "member/minecrowdfunding";
	}
}
