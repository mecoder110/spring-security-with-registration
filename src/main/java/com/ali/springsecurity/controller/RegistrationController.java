package com.ali.springsecurity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ali.springsecurity.entity.User;
import com.ali.springsecurity.event.UserRegistrationCompleteEvent;
import com.ali.springsecurity.model.PasswordModel;
import com.ali.springsecurity.model.UserModel;
import com.ali.springsecurity.service.UserService;
import com.ali.springsecurity.utlity.MailUtility;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/v1")
@Slf4j
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
	@GetMapping("/registrationVarify")
	public String varification(@RequestParam("token") String token) {
		String verifyToken = userService.verifyToken(token);
		if(verifyToken.equalsIgnoreCase("validated")) {
			return "Varification Success";
		}
		else {
			return "Bad User";
		}
	}
	@GetMapping("/regenerateToken/{token}")
	public String regenerateToken(@PathVariable String token, HttpServletRequest request) {
		String reToken = userService.regenerateToken(token);
		if(reToken.equalsIgnoreCase("invalid")) {
			return "Bad Token";
		}
		// mail utility call /v1/registrationVarify?token=" + token;
		String url = applicationUrl(request) + "/v1/registrationVarify?token=" + reToken;
		log.info("click link to validate : {} ",url);
		return "token regenerated success";}

	//GENERATE TOKEN to reset password
	@GetMapping("/resetPwdToken/{email}")
	public String resetPasswordToken(@PathVariable String email, HttpServletRequest request) {
		String pwdToken = userService.regeneratePasswordToken(email);
		if(pwdToken.equalsIgnoreCase("invalid")) {
			return "Bad User";
		}
		String url= applicationUrl(request) +"/v1/resetPwd?token="+ pwdToken;
		log.info("link to reset password {}", url);
		return "link sent to reset password";
		
	}
	// RESET PASSWORD IF Without current password
	@PutMapping("/resetPwd")
	public String resetPassword(@RequestParam("token") String token,
			@RequestBody PasswordModel passwordModel) {
		String pwdStatus = userService.resetPassword(passwordModel, token);
		if(pwdStatus.equalsIgnoreCase("invalid")) {
			return "Bad User";
		}
		return "password reset success!";
		
	}
	// UPDATE PASSWORD with current password
	@PutMapping("/changePwd")
	public String changePassword(@RequestBody PasswordModel passwordModel) {
		String pwdStatus =userService.changePassword(passwordModel);
		if(pwdStatus.equalsIgnoreCase("invalid")) {
			return "Bad user or password";
		}
		return "password change success!";
		
	}
	private String applicationUrl(HttpServletRequest request) {
		String url = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
		return url;

	}
}
