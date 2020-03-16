package com.atguigu.scw.user.exception;

import com.atguigu.scw.user.enums.UserExceptionEnum;

public class UserException extends RuntimeException {
	
	public UserException() {
		
	}
	
	public UserException(UserExceptionEnum e) {
		super(e.getMsg());
	}

}
