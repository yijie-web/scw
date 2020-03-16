package com.atguigu.scw.project.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


import com.atguigu.scw.project.service.exp.handler.MeberServiceFeignExceptionHandler;
import com.atguigu.scw.project.vo.resp.TMember;
import com.atguigu.scw.vo.resp.AppResponse;

@FeignClient(value="SCW-USER",fallback=MeberServiceFeignExceptionHandler.class)
public interface MeberServiceFeign {
	
	@GetMapping("/user/info/{id}")
	public AppResponse<TMember> getMemberInfo(@PathVariable("id") Integer memberid);

}
