package co.edu.ufps.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import co.edu.ufps.entity.User;
import co.edu.ufps.entity.VerificationToken;
import co.edu.ufps.model.UserModel;


public interface UserService {

	User registerUser(UserModel userM);

	void saveVerificationTokenUser(String token, User user);

	String validateToken(String token);

	VerificationToken generateNewToken(String oldToken);

	User findByEmail(String email);

	void createPasswordResetTokenForUser(User user, String token);

	String validatePasswordReset(String token);

	Optional<User> getUserByPasswordResetToken(String token);

	void changePassword(User user, String newPassword);

	boolean checkValidOldPassowrd(User user, String oldPassword);

}
