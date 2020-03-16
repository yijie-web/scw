package com.atguigu.scw.user.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.atguigu.scw.bean.TMember;
import com.atguigu.scw.bean.TMemberAddress;
import com.atguigu.scw.bean.TMemberAddressExample;
import com.atguigu.scw.bean.TMemberExample;
import com.atguigu.scw.user.enums.UserExceptionEnum;
import com.atguigu.scw.user.exception.UserException;
import com.atguigu.scw.user.mapper.TMemberAddressMapper;
import com.atguigu.scw.user.mapper.TMemberMapper;
import com.atguigu.scw.user.service.UserService;
import com.atguigu.scw.user.vo.resp.UserAddressVo;
import com.atguigu.user.enums.AccttypeEnume;
import com.atguigu.user.enums.AuthEnume;
import com.atguigu.user.enums.UserTypeEnume;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	TMemberMapper memberMapper;
	
	@Autowired
	TMemberAddressMapper memberAddressMapper;

	
	@Override
	public List<TMemberAddress> getUserAddress(Integer userId) {
		TMemberAddressExample example = new TMemberAddressExample();
		example.createCriteria().andMemberidEqualTo(userId);
		List<TMemberAddress> list = memberAddressMapper.selectByExample(example);
		return list;
	}

	@Override
	public TMember getUserById(Integer memberid) {
		return memberMapper.selectByPrimaryKey(memberid);
	}

}
