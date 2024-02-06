package com.ali.springsecurity.entity;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VarificationToken {

	private static final int EXPIRATION_TIME = 10;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String token;

	private LocalDateTime experiationTime;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "FK_USER_VERIFY_TOKEN"))
	private User user;

	public VarificationToken(String token, User user) {
		super();
		this.token = token;
		this.user = user;
		this.experiationTime = calculateExpirationTime(EXPIRATION_TIME);
	}

	public VarificationToken(String token) {
		super();
		this.token = token;
		this.experiationTime = calculateExpirationTime(EXPIRATION_TIME);
	}

	private LocalDateTime calculateExpirationTime(int expirationTime) {
		LocalDateTime currentTime = LocalDateTime.now();
		// set the exp time 10 min
		LocalDateTime expireTime = currentTime.plus(EXPIRATION_TIME, ChronoUnit.MINUTES);
		return expireTime;
	}

}
