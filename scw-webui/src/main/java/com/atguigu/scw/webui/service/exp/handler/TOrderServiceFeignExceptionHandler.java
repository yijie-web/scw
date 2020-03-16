package com.atguigu.scw.webui.service.exp.handler;

import java.util.List;

import org.springframework.stereotype.Component;

import com.atguigu.scw.vo.resp.AppResponse;
import com.atguigu.scw.webui.service.TOrderServiceFeign;
import com.atguigu.scw.webui.vo.rep.OrderInfoSubmitVo;
import com.atguigu.scw.webui.vo.resp.ProjectVo;
import com.atguigu.scw.webui.vo.resp.TOrder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TOrderServiceFeignExceptionHandler implements TOrderServiceFeign{

	@Override
	public AppResponse<TOrder> saveOrder(OrderInfoSubmitVo orderInfoSubmitVo) {

		AppResponse<TOrder> response = AppResponse.fail(null);
		response.setMsg("调用远程服务{project}失败");
		log.error("调用远程服务{project}失败");
		return response;
	}

}
