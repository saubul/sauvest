package ru.sauvest.auth.service;

import ru.sauvest.auth.entity.VerificationToken;

public interface VerificationTokenService {

	String createForUser(String username);
	
	VerificationToken findByToken(String token);

	void delete(VerificationToken verificationToken);

}
