package com.ali.springsecurity.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ali.springsecurity.entity.User;
import com.ali.springsecurity.entity.VarificationToken;
import com.ali.springsecurity.model.PasswordModel;
import com.ali.springsecurity.model.UserModel;
import com.ali.springsecurity.repository.UserRepository;
import com.ali.springsecurity.repository.VarificationTokenRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
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

	@Override
	public String verifyToken(String token) {
		VarificationToken varificationToken = tokenRepository.findByToken(token);

		if (varificationToken == null) {
			return "invalid";
		}
		LocalDateTime expirationTime = varificationToken.getExperiationTime();
		LocalDateTime currentTime = LocalDateTime.now();

		// Check if the current time has reached the expiration time
		if (currentTime.isAfter(expirationTime)) {
			log.info("token expire {}", token);
			//tokenRepository.delete(varificationToken);
			return "expired";

		} else {
			User user = varificationToken.getUser();
			user.setEnabled(true);
			userRepository.save(user);
			tokenRepository.delete(varificationToken);
			return "validated";
		}
	}

	@Override
	public String regenerateToken(String token) {
		VarificationToken tokenEntity = tokenRepository.findByToken(token);
		if(tokenEntity == null) {
			return "invalid";
		}
		User user = tokenEntity.getUser();
		tokenRepository.delete(tokenEntity);
		String reToken = UUID.randomUUID().toString();
		VarificationToken reTokenEntity = new VarificationToken(reToken, user);
		VarificationToken save = tokenRepository.save(reTokenEntity);
		return save.getToken();
	}

	@Override
	public String resetPassword(PasswordModel passwordModel, String token) {
		Optional<VarificationToken> tokenEntity = Optional.ofNullable(tokenRepository.findByToken(token));
		if(tokenEntity.isEmpty()) {
			return "invalid";
		}			
		User user = tokenEntity.get().getUser();
		
		user.setPassword(passwordEncoder.encode(passwordModel.getNewPassword()));
		userRepository.save(user);
		tokenRepository.delete(tokenEntity.get());
		return "password changed";
	}

	@Override
	public String changePassword(PasswordModel passwordModel) {
		User user = userRepository.findByEmail(passwordModel.getEmail());
		if(user==null) {
			return "invalid";
		}
		boolean matches = passwordEncoder.matches(passwordModel.getCurrentPassword(), user.getPassword());
		if(matches) {
			user.setPassword(passwordEncoder.encode(passwordModel.getNewPassword()));
			userRepository.save(user);
			return "password updated";
		}
		return "invalid";
	}

	@Override
	public String regeneratePasswordToken(String email) {
		Optional<User> user = Optional.ofNullable(userRepository.findByEmail(email));
		
		if(user.isEmpty()) {
			return "invalid";
		}
		String newToken = UUID.randomUUID().toString();
		VarificationToken pwdToken = new VarificationToken(newToken, user.get());
		tokenRepository.save(pwdToken);
		return newToken;
	}
}


