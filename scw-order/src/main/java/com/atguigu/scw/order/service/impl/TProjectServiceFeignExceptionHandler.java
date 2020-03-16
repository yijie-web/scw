package com.atguigu.scw.order.service.impl;

import org.springframework.stereotype.Component;

import com.atguigu.scw.order.service.TProjectServiceFeign;
import com.atguigu.scw.order.vo.resp.TReturn;
import com.atguigu.scw.vo.resp.AppResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TProjectServiceFeignExceptionHandler implements TProjectServiceFeign {

	@Override
	public AppResponse<TReturn> returnInfo(Integer returnId) {
		AppResponse<TReturn> response = AppResponse.fail(null);
		response.setMsg("调用远程服务{project}失败");
		log.error("调用远程服务{project}失败");
		return response;
	}

}
