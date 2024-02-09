package com.ali.springsecurity.service;

import com.ali.springsecurity.entity.User;
import com.ali.springsecurity.entity.VarificationToken;
import com.ali.springsecurity.model.PasswordModel;
import com.ali.springsecurity.model.UserModel;

public interface UserService {

	User registerUser(UserModel userModel);

	VarificationToken saveToken(VarificationToken verifyToken);

	String verifyToken(String token);

	String regenerateToken(String token);

	String resetPassword(PasswordModel passwordModel, String token);

	String changePassword(PasswordModel passwordModel);

	String regeneratePasswordToken(String email);

}
