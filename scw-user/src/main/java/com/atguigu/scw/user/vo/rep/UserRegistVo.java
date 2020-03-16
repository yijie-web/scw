package com.atguigu.scw.user.vo.rep;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
*@author 
*@version 
*创建时间: 2019年12月11日
*/
@ApiModel
@Data
public class UserRegistVo implements Serializable {
	
	@ApiModelProperty("手机号")
	private String loginacct;
	@ApiModelProperty("密码")
	private String userpswd;
	@ApiModelProperty("邮箱")
	private String email;
	@ApiModelProperty("验证码")
	private String code;
	@ApiModelProperty("用户类型：0-个人，1-企业")
	 private String usertype;
	
}

