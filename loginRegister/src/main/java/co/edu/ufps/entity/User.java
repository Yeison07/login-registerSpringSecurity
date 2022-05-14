package co.edu.ufps.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Data
@Getter
@Setter
public class User {
	
	@Id
	private int documento;
	private String nombre, apellidos,rol,email;
	@Column(length = 60)
	private String clave;
	private boolean activo=false;
	
	
}
