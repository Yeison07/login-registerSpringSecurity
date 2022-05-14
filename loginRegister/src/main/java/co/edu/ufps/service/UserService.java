package co.edu.ufps.service;

import org.springframework.stereotype.Service;

import co.edu.ufps.entity.User;
import co.edu.ufps.entity.VerificationToken;
import co.edu.ufps.model.UserModel;


public interface UserService {

	User registerUser(UserModel userM);

	void saveVerificationTokenUser(String token, User user);

	String validateToken(String token);

	VerificationToken generateNewToken(String oldToken);

}
