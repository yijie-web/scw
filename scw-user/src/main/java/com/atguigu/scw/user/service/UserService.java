package com.atguigu.scw.user.service;

import java.util.List;

import com.atguigu.scw.bean.TMember;
import com.atguigu.scw.bean.TMemberAddress;
import com.atguigu.scw.user.vo.resp.UserAddressVo;

public interface UserService {

	

	/**
	 * 获取会员地址
	 * @param userId
	 * @return
	 */
	List<TMemberAddress> getUserAddress(Integer userId);

	TMember getUserById(Integer memberid);

}
