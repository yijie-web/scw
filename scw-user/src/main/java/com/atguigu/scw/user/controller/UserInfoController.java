package com.atguigu.scw.user.controller;

import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.internal.util.StringUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.atguigu.scw.bean.TMember;
import com.atguigu.scw.bean.TMemberAddress;
import com.atguigu.scw.user.service.UserService;
import com.atguigu.scw.user.vo.resp.UserAddressAddVo;
import com.atguigu.scw.user.vo.resp.UserAddressDeleteVo;
import com.atguigu.scw.user.vo.resp.UserAddressUpdateVo;
import com.atguigu.scw.user.vo.resp.UserAddressVo;
import com.atguigu.scw.user.vo.resp.UserDetailVo;
import com.atguigu.scw.user.vo.resp.UserProjectVo;
import com.atguigu.scw.user.vo.resp.UserUpdateVo;
import com.atguigu.scw.vo.resp.AppResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Api(tags = "用户信息模块(个人中心维护信息)") // 描述当前类是做什么的
@RequestMapping("/user/info")
@RestController
@Slf4j
public class UserInfoController {

	@Autowired
	StringRedisTemplate redisTemplate;

	@Autowired
	UserService userService;

//	@ApiOperation("[+]获取用户地址")
//	@GetMapping("/address")
//	public AppResponse<List<TMemberAddress>> addresses(String accessToken) {
//		String string = redisTemplate.opsForValue().get(accessToken);
//		Integer memberId = Integer.parseInt(string);
//
//		List<TMemberAddress> address = userService.getUserAddress(memberId);
//		return AppResponse.ok(address);
//	}

	@ApiOperation("[+]获取会员信息")
	@GetMapping("/{id}") 
	public AppResponse<TMember> getMemberInfo(@PathVariable("id") Integer memberid) {
		TMember member = userService.getUserById(memberid);
		return AppResponse.ok(member);
	};
	@ApiOperation(value="获取个人信息") 
	@ApiImplicitParams(value={
			@ApiImplicitParam(value="访问令牌",name="accessToken",required=true)
	})
	@GetMapping("/")
	public AppResponse<UserDetailVo> info(String accessToken){
		UserDetailVo detailVo = new UserDetailVo();

		return AppResponse.ok(detailVo);
	} 
	
	
	@ApiOperation(value="更新个人信息") 
	@PostMapping("/")
	public AppResponse<Object> updateInfo(UserUpdateVo updateVo){
		return AppResponse.ok(null);
	}

	
	
	@ApiOperation(value="获取我支持的项目") 
	@ApiImplicitParams(value={
			@ApiImplicitParam(value="访问令牌",name="accessToken",required=true)
	})
	@GetMapping("/support/project")
	public AppResponse<List<UserProjectVo>> support(String accessToken){
		UserProjectVo vo = new UserProjectVo();
		vo.setId(789);vo.setName("BAVOSN便携折叠移动电源台灯");
		UserProjectVo vo2 = new UserProjectVo();
		List<UserProjectVo> list = new ArrayList<>();
		list.add(vo2);list.add(vo);
	
		return AppResponse.ok(list);
	} 
	
	@ApiOperation(value="获取我关注的项目") 
	@ApiImplicitParams(value={
			@ApiImplicitParam(value="访问令牌",name="accessToken",required=true)
	})
	@GetMapping("/star/project")
	public AppResponse<List<UserProjectVo>> star(String accessToken){
		UserProjectVo vo = new UserProjectVo();
		vo.setId(789);vo.setName("BAVOSN便携折叠移动电源台灯");
		List<UserProjectVo> list = new ArrayList<>();
		list.add(vo);
		return AppResponse.ok(list);
	} 
	
	@ApiOperation(value="获取我发起的项目") 
	@ApiImplicitParams(value={
			@ApiImplicitParam(value="访问令牌",name="accessToken",required=true)
	})
	@GetMapping("/create/project")
	public AppResponse<List<UserProjectVo>> create(String accessToken){
		
		UserProjectVo vo = new UserProjectVo();
		vo.setId(789);vo.setName("BAVOSN便携折叠移动电源台灯");
		List<UserProjectVo> list = new ArrayList<>();
		list.add(vo);
		
		return AppResponse.ok(list);
	} 
	
	@ApiOperation(value="获取用户收货地址") 
	@ApiImplicitParams(value={
			@ApiImplicitParam(value="访问令牌",name="accessToken",required=true)
	})
	@GetMapping("/address")
	public AppResponse<List<UserAddressVo>> address(String accessToken){
		UserAddressVo vo = new UserAddressVo();
		String string = redisTemplate.opsForValue().get(accessToken);
		if (StringUtils.isEmpty(string)) {
			return AppResponse.fail(null);
		}
		//Integer memberId = Integer.parseInt(string);
		List<TMemberAddress> addressList = userService.getUserAddress(Integer.parseInt(string));
		List<UserAddressVo> addressVolist = new ArrayList<UserAddressVo>();
		for (TMemberAddress userAddress : addressList) {
			UserAddressVo userAddressVo = new UserAddressVo();
			userAddressVo.setId(userAddress.getId());
			userAddressVo.setAddress(userAddress.getAddress());
			addressVolist.add(userAddressVo);
		}
		return AppResponse.ok(addressVolist);
	} 
	
	
	@ApiOperation(value="新增用户收货地址") 
	@PostMapping("/address")
	public AppResponse<Object> addAddress(UserAddressAddVo addressAddVo){
		return AppResponse.ok("ok");
	} 
	
	
	@ApiOperation(value="修改用户收货地址") 
	@PutMapping("/address")
	public AppResponse<Object> updateAddress(UserAddressUpdateVo updateVo){
		return AppResponse.ok("ok");
	} 
	
	
	@ApiOperation(value="删除用户收货地址") 
	@DeleteMapping("/address")
	public AppResponse<Object> deleteAddress(UserAddressDeleteVo vo){
		return AppResponse.ok("ok");
	} 
	
	@ApiOperation(value="查看我的订单") 
	@GetMapping("/order")
	public AppResponse<Object> order(){
		return AppResponse.ok("ok");
	}
	@ApiOperation(value="删除我的订单") 
	@DeleteMapping("/order")
	public AppResponse<Object> deleteOrder(){
		return AppResponse.ok("ok");
	}
	@ApiOperation(value="获取我的系统消息") 
	@GetMapping("/message")
	public AppResponse<Object> message(){
		return AppResponse.ok("ok");
	}

}
