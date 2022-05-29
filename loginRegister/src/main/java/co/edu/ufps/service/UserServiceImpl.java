package co.edu.ufps.service;

import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import co.edu.ufps.entity.PasswordResetToken;
import co.edu.ufps.entity.User;
import co.edu.ufps.entity.VerificationToken;
import co.edu.ufps.model.UserModel;
import co.edu.ufps.repository.PasswordResetTokenRepository;
import co.edu.ufps.repository.UserRepository;
import co.edu.ufps.repository.VerificationTokenRepository;

@Service                                  
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private VerificationTokenRepository verificationRepo;
	
	@Autowired
	private PasswordResetTokenRepository passResetRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public User registerUser(UserModel userM) {
		User user= new User();
		user.setNombre(userM.getNombre());
		user.setApellidos(userM.getApellidos());
		user.setDocumento(userM.getDocumento());
		user.setEmail(userM.getEmail());
		user.setClave(passwordEncoder.encode(userM.getClave()));
		user.setRol("USER");
		userRepo.save(user);
		return user;
	}

	@Override
	public void saveVerificationTokenUser(String token, User user) {
		VerificationToken veriToken= new VerificationToken(user,token);
		verificationRepo.save(veriToken);
	}

	@Override
	public String validateToken(String token) {
		VerificationToken verficationToken= verificationRepo.findByToken(token);
		if(verficationToken==null) {return "invalido";}
		
		User user= verficationToken.getUser();
		Calendar cal= Calendar.getInstance();
		
		if (verficationToken.getExpirationTime().getTime() - cal.getTime().getTime()<=0) {
			verificationRepo.delete(verficationToken);
			return "expirado";
		}
		
		user.setActivo(true);
		userRepo.save(user);
		
		return "valido";
	}

	@Override
	public VerificationToken generateNewToken(String oldToken) {
		VerificationToken token= verificationRepo.findByToken(oldToken);
		token.setToken(UUID.randomUUID().toString());
		verificationRepo.save(token);
		return token;
	}

	@Override
	public User findByEmail(String email) {
		return userRepo.findByEmail(email);
	}

	@Override
	public void createPasswordResetTokenForUser(User user, String token) {
		PasswordResetToken passwordResetToken = new PasswordResetToken(user,token);	
		passResetRepo.save(passwordResetToken);
	}

	@Override
	public String validatePasswordReset(String token) {
	
		PasswordResetToken passwordResetToken= passResetRepo.findByToken(token);
		if(passwordResetToken==null) {return "invalido";}
		
		User user= passwordResetToken.getUser();
		Calendar cal= Calendar.getInstance();
		
		if (passwordResetToken.getExpirationTime().getTime() - cal.getTime().getTime()<=0) {
			passResetRepo.delete(passwordResetToken);
			return "expirado";
		}
		
		
		return "valido";
	}

	@Override
	public Optional<User> getUserByPasswordResetToken(String token) {
		return Optional.ofNullable(passResetRepo.findByToken(token).getUser());
	}

	@Override
	public void changePassword(User user, String newPassword) {
		user.setClave(passwordEncoder.encode(newPassword));
		userRepo.save(user);
		
	}

	@Override
	public boolean checkValidOldPassowrd(User user, String oldPassword) {
		
		return passwordEncoder.matches(oldPassword, user.getClave());
	}
	
	
}
