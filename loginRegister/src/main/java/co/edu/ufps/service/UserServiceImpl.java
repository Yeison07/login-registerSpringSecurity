package co.edu.ufps.service;

import java.util.Calendar;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import co.edu.ufps.entity.User;
import co.edu.ufps.entity.VerificationToken;
import co.edu.ufps.model.UserModel;
import co.edu.ufps.repository.UserRepository;
import co.edu.ufps.repository.VerificationTokenRepository;

@Service                                  
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private VerificationTokenRepository verificationRepo;
	
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
	
	
}
