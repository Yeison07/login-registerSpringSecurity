package co.edu.ufps.listener;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import co.edu.ufps.entity.User;
import co.edu.ufps.event.RegisterEvent;
import co.edu.ufps.service.UserService;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RegisterEventListener implements ApplicationListener<RegisterEvent>{

	@Autowired
	private UserService userServie;
	
	@Override
	public void onApplicationEvent(RegisterEvent event) {
		// Crear token de verificacion para el usuario con link
		User user= event.getUser();
		
		String token= UUID.randomUUID().toString();
		userServie.saveVerificationTokenUser(token,user);
		//Enviar correo al usuario
		String url= event.getApplicationUrl() + "/verifyRegistrarion?token="+token;
		log.info("Haz click en el siguiente link para verificar tu cuenta: {}",url);
		
		
		
		
	}
 
	
}
