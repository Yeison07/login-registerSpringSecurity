package co.edu.ufps.controller;

import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.ufps.entity.User;
import co.edu.ufps.entity.VerificationToken;
import co.edu.ufps.event.RegisterEvent;
import co.edu.ufps.model.PasswordModel;
import co.edu.ufps.model.UserModel;
import co.edu.ufps.service.UserService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class Controller {

	@Autowired
	private UserService userServi;

	@Autowired
	private ApplicationEventPublisher aep;

	@PostMapping("/register")
	public String registerUser(@RequestBody UserModel userM, final HttpServletRequest request) {
		User user = userServi.registerUser(userM);
		aep.publishEvent(new RegisterEvent(user, applicationUrl(request)));
		return "Exitoso";
	}

	private String applicationUrl(HttpServletRequest request) {
		// TODO Auto-generated method stub

		return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
	}

	@GetMapping("/verifyRegistrarion")
	public String verifyRegister(@RequestParam("token") String token) {
		String result = userServi.validateToken(token);
		if (result.equalsIgnoreCase("valido")) {
			return "Verificacion exitosa";
		}
		return "Hubo un problem en la verificacion, genera otro codigo";
	}

	@GetMapping("/resendVerifyEmail")
	public String resendEmail(@RequestParam("token") String oldToken, HttpServletRequest request) {
		VerificationToken vt = userServi.generateNewToken(oldToken);
		User user = vt.getUser();
		resendEmailMssg(user, applicationUrl(request), vt);
		return "Link de verificacion reenviado";
	}

	private void resendEmailMssg(User user, String applicationUrl, VerificationToken token) {
		String url = applicationUrl + "/verifyRegistrarion?token=" + token.getToken();
		log.info("Haz click en el siguiente link para verificar tu cuenta: {}", url);

	}

	@PostMapping("/resetPassword")
	public String resetPassword(@RequestBody PasswordModel passwordmodel, HttpServletRequest request) {
		User user = userServi.findByEmail(passwordmodel.getEmail());
		String url = "";
		if (user != null) {
			String token = UUID.randomUUID().toString();
			userServi.createPasswordResetTokenForUser(user, token);
			url = passwordResetTokenMail(user, applicationUrl(request), token);
		}
		return url;
	}

	private String passwordResetTokenMail(User user, String applicationUrl, String token) {
		String url = applicationUrl + "/savePassword?token=" + token;
		log.info("Haz click en el siguiente link para reiniciar tu contraseña: {}", url);

		return url;
	}

	@PostMapping("/savePassword")
	public String savePassword(@RequestParam("token") String token, @RequestBody PasswordModel passwordModel) {
		String result = userServi.validatePasswordReset(token);
		if (result.equalsIgnoreCase("valid")) {
			return "token invalido";
		}
		Optional<User> user = userServi.getUserByPasswordResetToken(token);
		if (user.isPresent()) {
			userServi.changePassword(user.get(), passwordModel.getNewPassword());
			return "Contraseña reiniciada con exito";
		} else {
			return "Token invalido";
		}

	}
	
	@PostMapping("/changePassword")
	public String changePassword(@RequestBody PasswordModel passwordModel) {
		User user= userServi.findByEmail(passwordModel.getEmail());
		if (!userServi.checkValidOldPassowrd(user,passwordModel.getOldPassword())) {
			return "La contraseña antigua no coincide";
		}
		userServi.changePassword(user, passwordModel.getNewPassword());
		return "Contraseña actualizada correctamente";
	}
}
