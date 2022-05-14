package co.edu.ufps.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserModel {
	
	private int documento;
	private String nombre, apellidos,email;
	private String clave;
	private String validatePassword;

}
