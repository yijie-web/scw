package com.atguigu.scw.user.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.atguigu.scw.user.component.SmsTemplate;
import com.atguigu.scw.user.service.TMemberService;
import com.atguigu.scw.user.vo.rep.UserRegistVo;
import com.atguigu.scw.user.vo.resp.UserRespVo;
import com.atguigu.scw.vo.resp.AppResponse;
import com.atguigu.user.enums.AccttypeEnume;
import com.atguigu.user.enums.AuthEnume;
import com.atguigu.user.enums.UserTypeEnume;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;


@Api(tags = "用户登陆注册模块")
@Slf4j
@RequestMapping("/user")
@RestController
public class UserLoginRegistController {
	
	@Autowired
	SmsTemplate smsTemplate;
	
	@Autowired
	StringRedisTemplate stringRedisTemplate;
	
	@Autowired
	TMemberService tMemberService;
	
	@ApiOperation(value="用户登陆") 
	@ApiImplicitParams(value={
			@ApiImplicitParam(value="登陆账号",name="loginacct"),
			@ApiImplicitParam(value="用户密码",name="password")
	})
	@PostMapping("/login")
	public AppResponse<UserRespVo> login(@RequestParam("loginacct") String loginacct,@RequestParam("password") String password){
		
		try {
			UserRespVo vo = tMemberService.getUserByLogin(loginacct,password);
			log.debug("登录成功-{}",loginacct);
			return AppResponse.ok(vo);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.debug("登录失败-{}--{}",loginacct,e.getMessage());
			AppResponse response = AppResponse.fail(null);
			response.setMsg(e.getMessage());
			return response;
		}
	} 
	
	@ApiOperation(value="用户注册") 
	@PostMapping("/register")
	public AppResponse<Object> register(UserRegistVo vo){
		
		String loginacct = vo.getLoginacct();
		if(!StringUtils.isEmpty(loginacct)) {
			String code = stringRedisTemplate.opsForValue().get(loginacct);
			if (!StringUtils.isEmpty(code)) {
				if (code.equals(vo.getCode())) {
					
					//需要校验账号是否唯一
					//需要校验email地址是否被使用
					int i = tMemberService.save(vo);
					if (i==1) {
						stringRedisTemplate.delete(loginacct);//手动清理缓存
						return AppResponse.ok("ok");
					}else {
						return AppResponse.fail(i);
					}
					
				}else {
					AppResponse resp = AppResponse.fail(null);
					resp.setMsg("验证码不一致，请重新输入");
					return resp;
				}
			}else {
				AppResponse resp = AppResponse.fail(null);
				resp.setMsg("验证码已经失效，请重新发送");
				return resp;
			}
		}
		return AppResponse.ok("ok");
	} 
	
	@ApiOperation(value="发送短信验证码") 
	@PostMapping("/sendsms")
	public AppResponse<Object> sendsms(String loginacct){
		
		StringBuilder code = new StringBuilder();
		for(int i = 1; i <= 4; i++) {
			code.append(new Random().nextInt(10));
		}
		Map<String, String> querys = new HashMap<String, String>();
	    querys.put("mobile", loginacct);
	    querys.put("param", "code:"+code.toString());
	    querys.put("tpl_id", "TP1711063");
	    smsTemplate.sendSms(querys);
	    
	    //stringRedisTemplate.opsForValue().set(loginacct, code.toString());
	    stringRedisTemplate.opsForValue().set(loginacct, code.toString(), 5, TimeUnit.MINUTES);;
	    
	    log.debug("发送短信成功-验证码：{}",code.toString());
		return AppResponse.ok("ok");
	} 
	
	@ApiOperation(value="验证短信验证码") 
	@PostMapping("/valide")
	public AppResponse<Object> valide(){
		return AppResponse.ok("ok");
	} 
	
	@ApiOperation(value="重置密码") 
	@PostMapping("/reset")
	public AppResponse<Object> reset(){
		return AppResponse.ok("ok");
	} 
	
	

}
