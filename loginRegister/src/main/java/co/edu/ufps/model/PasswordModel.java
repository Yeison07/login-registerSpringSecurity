package co.edu.ufps.model;

import lombok.Data;

@Data
public class PasswordModel {

	private String email;
	private String oldPassword;
	private String newPassword;
	
}
