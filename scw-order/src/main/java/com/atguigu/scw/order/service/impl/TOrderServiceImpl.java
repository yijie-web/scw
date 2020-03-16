package com.atguigu.scw.order.service.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.atguigu.scw.order.bean.AppDateUtils;
import com.atguigu.scw.order.bean.TOrder;
import com.atguigu.scw.order.mapper.TOrderMapper;
import com.atguigu.scw.order.service.TOrderService;
import com.atguigu.scw.order.service.TProjectServiceFeign;
import com.atguigu.scw.order.vo.req.OrderInfoSubmitVo;
import com.atguigu.scw.order.vo.resp.TReturn;
import com.atguigu.scw.vo.resp.AppResponse;
import com.atguigu.user.enums.OrderStatusEnumes;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TOrderServiceImpl implements TOrderService {

	@Autowired
	StringRedisTemplate stringRedisTemplate;
	
	@Autowired
	TProjectServiceFeign projectServiceFeign;
	
	@Autowired
	TOrderMapper orderMapper;
	
	@Override
	public TOrder saveOrder(OrderInfoSubmitVo orderInfoSubmitVo) {
	
		TOrder order = new TOrder();
		String accessToken = orderInfoSubmitVo.getAccessToken();
		String meberId = stringRedisTemplate.opsForValue().get(accessToken);
		order.setMemberid(Integer.parseInt(meberId));
		order.setProjectid(orderInfoSubmitVo.getProjectid());
		order.setReturnid(orderInfoSubmitVo.getReturnid());
		
		String ordernum = UUID.randomUUID().toString().replaceAll("-", "");
		order.setOrdernum(ordernum);
		order.setCreatedate(AppDateUtils.getFormatTime());
		AppResponse<TReturn> respTreturn = projectServiceFeign.returnInfo(orderInfoSubmitVo.getReturnid());
		TReturn returnObj = respTreturn.getData();
		Integer totalMoney = orderInfoSubmitVo.getRtncount()*returnObj.getSupportmoney()+returnObj.getFreight();
		order.setMoney(totalMoney);
		order.setRtncount(orderInfoSubmitVo.getRtncount());
		order.setStatus(OrderStatusEnumes.UNPAY.getCode()+"");
		order.setAddress(orderInfoSubmitVo.getAddress());
		order.setInvoice(orderInfoSubmitVo.getInvoice().toString());
		order.setInvoictitle(orderInfoSubmitVo.getInvoictitle());
		order.setRemark(orderInfoSubmitVo.getRemark());
		orderMapper.insertSelective(order);
		log.debug("插入信息时->{}",order);
		return order;
	}

}
