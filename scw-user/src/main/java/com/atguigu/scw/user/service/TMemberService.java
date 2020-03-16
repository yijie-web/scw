package com.atguigu.scw.user.service;

import com.atguigu.scw.user.vo.rep.UserRegistVo;
import com.atguigu.scw.user.vo.resp.UserRespVo;

/**
*@author 
*@version 
*创建时间: 2019年12月14日
*/
public interface TMemberService {

	int save(UserRegistVo vo);

	UserRespVo getUserByLogin(String loginacct, String password);

	

}
