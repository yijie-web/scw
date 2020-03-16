package com.atguigu.scw.webui.service.exp.handler;

import java.util.List;

import org.springframework.stereotype.Component;

import com.atguigu.scw.vo.resp.AppResponse;
import com.atguigu.scw.webui.service.TMberServiceFeign;
import com.atguigu.scw.webui.vo.resp.UserAddressVo;
import com.atguigu.scw.webui.vo.resp.UserRespVo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TMberServiceFeignExceptionHandler implements TMberServiceFeign {

	@Override
	public AppResponse<UserRespVo> login(String loginacct, String password) {
		// TODO Auto-generated method stub
		AppResponse<UserRespVo> response = AppResponse.fail(null);
		
		response.setMsg("调用远程服务【登录失败】");
		log.error("调用远程服务【登录失败】");
		return response;
	}

	@Override
	public AppResponse<List<UserAddressVo>> address(String accessToken) {
		AppResponse<List<UserAddressVo>> response = AppResponse.fail(null);
		
		response.setMsg("调用远程服务【获取地址失败】");
		log.error("调用远程服务【获取地址失败】");
		return response;
	}

}
