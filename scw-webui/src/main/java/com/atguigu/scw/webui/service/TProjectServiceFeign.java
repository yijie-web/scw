package com.atguigu.scw.webui.service;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.atguigu.scw.vo.resp.AppResponse;
import com.atguigu.scw.webui.service.exp.handler.TProjectServiceFeignExceptionHandler;
import com.atguigu.scw.webui.vo.resp.ProjectDetailVo;
import com.atguigu.scw.webui.vo.resp.ProjectVo;
import com.atguigu.scw.webui.vo.resp.ReturnPayConfirmVo;

@FeignClient(value="SCW-PROJECT",fallback=TProjectServiceFeignExceptionHandler.class)
public interface TProjectServiceFeign {

	@GetMapping("/project/index")
	public AppResponse<List<ProjectVo>> all();
	
	@GetMapping("/project/details/info/{projectId}")
	public AppResponse<ProjectDetailVo> detail(@PathVariable("projectId") Integer projectId);
	
	@GetMapping("/project/confirm/returns/{projectId}/{returnId}")
	public AppResponse<ReturnPayConfirmVo> returnInfo(@PathVariable("projectId")Integer projectId,@PathVariable("returnId") Integer returnId);
}
