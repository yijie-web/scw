package com.atguigu.scw.project.service.exp.handler;

import org.springframework.stereotype.Component;

import com.atguigu.scw.project.service.MeberServiceFeign;
import com.atguigu.scw.project.vo.resp.TMember;
import com.atguigu.scw.vo.resp.AppResponse;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
public class MeberServiceFeignExceptionHandler implements MeberServiceFeign {

	@Override
	public AppResponse<TMember> getMemberInfo(Integer memberid) {
		AppResponse<TMember> response = AppResponse.fail(null);
		response.setMsg("调用远程服务{project}失败");
		log.error("调用远程服务{project}失败");
		return response;
	}

	
}
