package com.ali.springsecurity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ali.springsecurity.entity.User;
import com.ali.springsecurity.event.UserRegistrationCompleteEvent;
import com.ali.springsecurity.model.UserModel;
import com.ali.springsecurity.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/v1")
public class RegistrationController {

	@Autowired
	UserService userService;
	@Autowired
	ApplicationEventPublisher publisher;

	@PostMapping("/register")
	public String newRegister(@RequestBody UserModel userModel, HttpServletRequest request) {

		User user = userService.registerUser(userModel);
		// Event RegistrationCompleteEvent
		publisher.publishEvent(new UserRegistrationCompleteEvent(user, applicationUrl(request)));
		return "Success";

	}

	private String applicationUrl(HttpServletRequest request) {
		String url = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
		return url;

	}
}
