package com.atguigu.scw.webui.controller;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.atguigu.scw.vo.resp.AppResponse;
import com.atguigu.scw.webui.service.TMberServiceFeign;
import com.atguigu.scw.webui.service.TProjectServiceFeign;
import com.atguigu.scw.webui.vo.resp.ProjectVo;
import com.atguigu.scw.webui.vo.resp.UserRespVo;



@Controller
public class DispatcherController {

	@Autowired
	TMberServiceFeign mberServiceFeign;
	
	@Autowired
	TProjectServiceFeign projectServiceFeign;
	
	@Autowired
	RedisTemplate redisTemplate;
	
	@RequestMapping("/index")
	public String index(Model model) {
		
		List<ProjectVo> data = (List<ProjectVo>) redisTemplate.opsForValue().get("projectInfo");
		
		if (data==null) {
			AppResponse<List<ProjectVo>> response = projectServiceFeign.all();
			data = response.getData();
			redisTemplate.opsForValue().set("projectInfo", data,1,TimeUnit.HOURS);
		}
		model.addAttribute("projectVoLsit", data);
		return "index";
	}
	
	@RequestMapping("/login")
	public String login() {
		return "login";
	}
	
	@RequestMapping("/doLogin")
	public String doLogin(String loginacct,String password,HttpSession session) {
		
		AppResponse<UserRespVo> response = mberServiceFeign.login(loginacct, password);
		UserRespVo data = response.getData();
		if (data==null) {
			return "login";
		}
		session.setAttribute("loginMember", data);
		String preUrl = (String) session.getAttribute("preUrl");
		if (StringUtils.isEmpty(preUrl)) {
			return "redirect:index";
		}else {
			return "redirect:"+preUrl;
		}
		
	}
	
	@RequestMapping("/loginOut")
	public String loginOut(HttpSession session ) {
		
		if (session!=null) {
			session.removeAttribute("loginMember");
			session.invalidate();
		}
		
		return "redirect:index";
	}
}
