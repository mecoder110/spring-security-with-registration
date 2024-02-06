package com.ali.springsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ali.springsecurity.entity.VarificationToken;

public interface VarificationTokenRepository extends JpaRepository<VarificationToken, Long> {
}