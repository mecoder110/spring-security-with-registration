package com.ali.springsecurity.event.listner;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.ali.springsecurity.entity.User;
import com.ali.springsecurity.entity.VarificationToken;
import com.ali.springsecurity.event.UserRegistrationCompleteEvent;
import com.ali.springsecurity.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class UserRegistrationCompleteEventListner implements ApplicationListener<UserRegistrationCompleteEvent> {

	@Autowired
	private UserService userService;

	@Override
	public void onApplicationEvent(UserRegistrationCompleteEvent event) {
		User user = event.getUser();
		String token = UUID.randomUUID().toString();

		VarificationToken verify = new VarificationToken(token, user);
		userService.saveToken(verify);

		String url = event.getVarificationUrl() + "/varifivation?token=" + token;
		// mail utility

		log.info("Please click on link{}",url);
	}

}
