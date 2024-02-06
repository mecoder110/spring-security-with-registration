package com.ali.springsecurity.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ali.springsecurity.entity.User;
import com.ali.springsecurity.entity.VarificationToken;
import com.ali.springsecurity.model.UserModel;
import com.ali.springsecurity.repository.UserRepository;
import com.ali.springsecurity.repository.VarificationTokenRepository;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	UserRepository userRepository;
	@Autowired
	VarificationTokenRepository tokenRepository;
	@Autowired
	PasswordEncoder passwordEncoder;

	@Override
	public User registerUser(UserModel userModel) {

		User user = new User();
		user.setFirstName(userModel.getFirstName());
		user.setLastName(userModel.getLastName());
		user.setEmail(userModel.getEmail());
		user.setPassword(passwordEncoder.encode(userModel.getPassword()));
		user.setEnabled(false);
		user.setRole("user");

		return userRepository.save(user);

	}

	@Override
	public VarificationToken saveToken(VarificationToken verifyToken) {
		return tokenRepository.save(verifyToken);

	}

}
