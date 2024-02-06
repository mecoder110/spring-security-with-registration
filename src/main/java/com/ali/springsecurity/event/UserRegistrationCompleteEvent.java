package com.ali.springsecurity.event;

import org.springframework.context.ApplicationEvent;

import com.ali.springsecurity.entity.User;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserRegistrationCompleteEvent extends ApplicationEvent {

	private User user;
	private String varificationUrl;

	public UserRegistrationCompleteEvent(User user, String varificationUrl) {
		super(user);

		this.user = user;
		this.varificationUrl = varificationUrl;
	}

}
