package com.atguigu.scw.user.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.atguigu.scw.bean.TMember;
import com.atguigu.scw.bean.TMemberExample;
import com.atguigu.scw.user.enums.UserExceptionEnum;
import com.atguigu.scw.user.exception.UserException;
import com.atguigu.scw.user.mapper.TMemberMapper;
import com.atguigu.scw.user.service.TMemberService;
import com.atguigu.scw.user.vo.rep.UserRegistVo;
import com.atguigu.scw.user.vo.resp.UserRespVo;

import lombok.extern.slf4j.Slf4j;


/**
 * @author 杰杰
 * @date 2019年12月14日 下午5:01:17
 */
@Slf4j
@Transactional(readOnly=true)
@Service
public class TMemberServiceImpl implements TMemberService{

	@Autowired
	TMemberMapper tMemberMapper;
	
	@Autowired
	StringRedisTemplate template;

	//@Transactional(propagation=Propagation.REQUIRED,isolation=Isolation.REPEATABLE_READ)
	@Transactional
	@Override
	public int save(UserRegistVo vo) {
		
		try {
			TMemberExample example = new TMemberExample();
			//判断用户是否存在
			example.createCriteria().andLoginacctEqualTo(vo.getLoginacct());
			long l = tMemberMapper.countByExample(example);
			if (l > 0) {
				throw new UserException(UserExceptionEnum.LOGINACCT_EXIST);
			}
			//判断邮箱是否存在
			example.createCriteria().andEmailEqualTo(vo.getEmail());
			long a = tMemberMapper.countByExample(example);
			if (a > 0) {
				throw new UserException(UserExceptionEnum.EMAIL_EXIST);
			}
			//将vo属性对拷到do对象中
			TMember member = new TMember();
			BeanUtils.copyProperties(vo, member);
			member.setUsername(vo.getLoginacct());

			//密码加密
			String pswd = vo.getLoginacct();
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			member.setUserpswd(encoder.encode(pswd));
			
			int i = tMemberMapper.insertSelective(member);
			log.debug("注册会员成功->{}",member);
			return i;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("注册会员失败->{}",e.getMessage());
			e.printStackTrace();
			//throw new RuntimeException("保存会员业务逻辑失败");
			throw new UserException(UserExceptionEnum.LOGINACCT_EXIST_EROOR);
		}
		
	}

	@Override
	public UserRespVo getUserByLogin(String loginacct, String password) {
		// TODO Auto-generated method stub
		UserRespVo vo = new UserRespVo();
		
		TMemberExample example = new TMemberExample();
		example.createCriteria().andLoginacctEqualTo(loginacct);
		
		List<TMember> list = tMemberMapper.selectByExample(example);
		if (list==null || list.size()==0) {
			throw new UserException(UserExceptionEnum.LOGINACCT_UNEXIST);
		}
		TMember member = list.get(0);
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		if (encoder.matches(password, member.getUserpswd())) {
			throw new UserException(UserExceptionEnum.PASSWORD_ERROR);
		}
		BeanUtils.copyProperties(member, vo);
		String accessToken = UUID.randomUUID().toString().replaceAll("-", "");
		vo.setAccessToken(accessToken);
		
		template.opsForValue().set(accessToken, member.getId().toString());
		return vo;
	}
	
}
