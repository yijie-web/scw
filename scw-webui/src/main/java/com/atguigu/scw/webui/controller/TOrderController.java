package com.atguigu.scw.webui.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.atguigu.scw.vo.resp.AppResponse;
import com.atguigu.scw.webui.config.AlipayConfig;
import com.atguigu.scw.webui.service.TOrderServiceFeign;
import com.atguigu.scw.webui.vo.rep.OrderFormInfoSubmitVo;
import com.atguigu.scw.webui.vo.rep.OrderInfoSubmitVo;
import com.atguigu.scw.webui.vo.resp.ReturnPayConfirmVo;
import com.atguigu.scw.webui.vo.resp.TOrder;
import com.atguigu.scw.webui.vo.resp.UserRespVo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class TOrderController {

	@Autowired
	TOrderServiceFeign orderServiceFeign;
	
	@SuppressWarnings("null")
	@ResponseBody
	@RequestMapping("/order/pay")
	public String payOrder(OrderFormInfoSubmitVo vo/*接收表单信息*/,HttpSession session) {
		
		log.debug("立即支付=OrderFormInfoSubmitVo={}",vo);
		//1.下单
		OrderInfoSubmitVo orderInfoSubmitVo = new OrderInfoSubmitVo();
		BeanUtils.copyProperties(vo, orderInfoSubmitVo);
		UserRespVo userRespVo = (UserRespVo)session.getAttribute("loginMember");
		if (userRespVo == null) {
			return "redirect:/login";
		}
		
		String accessToken = userRespVo.getAccessToken();
		orderInfoSubmitVo.setAccessToken(accessToken);
		ReturnPayConfirmVo returnPayConfirmVo = (ReturnPayConfirmVo)session.getAttribute("payConfirmSession");
		if (returnPayConfirmVo == null) {
			return "redirect:/login";
		}
		orderInfoSubmitVo.setProjectid(returnPayConfirmVo.getProjectId());
		orderInfoSubmitVo.setReturnid(returnPayConfirmVo.getReturnId());
		orderInfoSubmitVo.setRemark(returnPayConfirmVo.getProjectRemark());
		orderInfoSubmitVo.setRtncount(returnPayConfirmVo.getNum());
		log.debug("orderInfoSubmitVo数据->{}",orderInfoSubmitVo);
		AppResponse<TOrder> resp = orderServiceFeign.saveOrder(orderInfoSubmitVo);
		TOrder order = resp.getData();
		log.debug("order数据->{}",order);
		
		//2.支付
		String orderName = returnPayConfirmVo.getProjectName();
		String result = payOrder(order.getOrdernum(), order.getMoney(),orderName,orderInfoSubmitVo.getRemark());
		return result;//这是一个表单，返回给浏览器，并且立即提交表单，出现二维码
		
		//return "redirect:/member/minecrowdfunding";
		
	}
	private String payOrder(String ordernum,Integer money,String orderName,String remark) {
		//获得初始化的AlipayClient
		try {
			AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id, AlipayConfig.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.alipay_public_key, AlipayConfig.sign_type);
			
			//设置请求参数
			AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
			alipayRequest.setReturnUrl(AlipayConfig.return_url);
			alipayRequest.setNotifyUrl(AlipayConfig.notify_url);
			
			
			alipayRequest.setBizContent("{\"out_trade_no\":\""+ ordernum +"\"," 
					+ "\"total_amount\":\""+ money +"\"," 
					+ "\"subject\":\""+ orderName +"\"," 
					+ "\"body\":\""+ remark +"\"," 
					+ "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
			
			//若想给BizContent增加其他可选请求参数，以增加自定义超时时间参数timeout_express来举例说明
			//alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\"," 
			//		+ "\"total_amount\":\""+ total_amount +"\"," 
			//		+ "\"subject\":\""+ subject +"\"," 
			//		+ "\"body\":\""+ body +"\"," 
			//		+ "\"timeout_express\":\"10m\"," 
			//		+ "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
			//请求参数可查阅【电脑网站支付的API文档-alipay.trade.page.pay-请求参数】章节
			
			//请求
			String result = alipayClient.pageExecute(alipayRequest).getBody();
			return result;
		} catch (AlipayApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	//临时写在客户端，正常应该写在订单服务系统中。需要内网穿透
	@ResponseBody
	@RequestMapping("/order/updateOrderStatus")
	public String updateOrderStatus() {
		
		log.debug("支付宝支付完成后，异步通知结果......");
		return "success";//业务完成，必须返回success字符串给支付宝服务器，表示交易完成
	}
	
}
