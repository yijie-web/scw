package com.atguigu.scw.webui.service.exp.handler;

import java.util.List;

import org.springframework.stereotype.Component;

import com.atguigu.scw.vo.resp.AppResponse;
import com.atguigu.scw.webui.service.TProjectServiceFeign;
import com.atguigu.scw.webui.vo.resp.ProjectDetailVo;
import com.atguigu.scw.webui.vo.resp.ProjectVo;
import com.atguigu.scw.webui.vo.resp.ReturnPayConfirmVo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TProjectServiceFeignExceptionHandler implements TProjectServiceFeign {

	@Override
	public AppResponse<List<ProjectVo>> all() {
		
		AppResponse<List<ProjectVo>> response = AppResponse.fail(null);
		response.setMsg("调用远程服务{project}失败");
		log.error("调用远程服务{project}失败");
		return response;
	}

	@Override
	public AppResponse<ProjectDetailVo> detail(Integer projectId) {
		// TODO Auto-generated method stub
		AppResponse<ProjectDetailVo> response = AppResponse.fail(null);
		response.setMsg("调用远程服务{project}失败");
		log.error("调用远程服务{project}失败");
		return response;
	}

	@Override
	public AppResponse<ReturnPayConfirmVo> returnInfo(Integer projectId, Integer returnId) {
		AppResponse<ReturnPayConfirmVo> response = AppResponse.fail(null);
		response.setMsg("调用远程服务{project}失败");
		log.error("调用远程服务{project}失败");
		return response;
	}

}
