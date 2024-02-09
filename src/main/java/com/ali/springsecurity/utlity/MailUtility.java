package com.ali.springsecurity.utlity;

import com.ali.springsecurity.entity.User;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MailUtility
{

	public boolean sendMail(User user, String url) {
		
		if(user.getEmail()!=null) {
			log.info(url);
			return true;
		}
		else {
			return false;
		}
		
	}
}
