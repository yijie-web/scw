package com.atguigu.scw.webui.controller;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.atguigu.scw.vo.resp.AppResponse;
import com.atguigu.scw.webui.service.TMberServiceFeign;
import com.atguigu.scw.webui.service.TProjectServiceFeign;
import com.atguigu.scw.webui.vo.resp.ProjectDetailVo;
import com.atguigu.scw.webui.vo.resp.ReturnPayConfirmVo;
import com.atguigu.scw.webui.vo.resp.UserAddressVo;
import com.atguigu.scw.webui.vo.resp.UserRespVo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class TProjectController {

	@Autowired
	TProjectServiceFeign projectServiceFeign;
	
	@Autowired
	TMberServiceFeign mberServiceFeign;
	
	@RequestMapping("/project/projectInfo")
	public String index(Integer id,Model model) {
		
		log.debug("22222222222-{}",id);
		AppResponse<ProjectDetailVo> resp = projectServiceFeign.detail(id);
		ProjectDetailVo vo = resp.getData();
		model.addAttribute("projectDetailVo",vo);
		log.debug("22222222222-{}",vo);
		return "project/project";
	}
	
	@RequestMapping("/project/support/{projectId}/{returnId}")
	public String support(@PathVariable("projectId")Integer projectId,
			@PathVariable("returnId")Integer returnId,Model model,HttpSession session) {
		AppResponse<ReturnPayConfirmVo> response = projectServiceFeign.returnInfo(projectId, returnId);
		ReturnPayConfirmVo payConfirm = response.getData();
		model.addAttribute("payConfirm",payConfirm);
		session.setAttribute("payConfirmSession", payConfirm);
		log.debug("00000-{}",projectId);
		return "project/pay-step-1";
	}
	
	@RequestMapping("/project/confirm/order/{num}")
	public String confirmOreder(@PathVariable Integer num,Model model,HttpSession session) {
		
		UserRespVo userRespVo = (UserRespVo) session.getAttribute("loginMember");
		
		if (userRespVo == null) {
			session.setAttribute("preUrl", "/project/confirm/order/"+num);
			return "redirect:/login";
		}
		String accessToken = userRespVo.getAccessToken();
		AppResponse<List<UserAddressVo>> appList = mberServiceFeign.address(accessToken);
		List<UserAddressVo> addressList = appList.getData();
		model.addAttribute("addressList",addressList);
		ReturnPayConfirmVo payConfirm = (ReturnPayConfirmVo) session.getAttribute("payConfirmSession");
		payConfirm.setNum(num);
		payConfirm.setTotalPrice(new BigDecimal(num*payConfirm.getPrice()+payConfirm.getFreight()));
		session.setAttribute("payConfirmSession", payConfirm);
		return "project/pay-step-2";
	}
}
