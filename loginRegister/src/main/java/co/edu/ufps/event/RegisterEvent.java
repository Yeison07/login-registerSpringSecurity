package co.edu.ufps.event;

import org.springframework.context.ApplicationEvent;

import co.edu.ufps.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterEvent extends ApplicationEvent{
	
	private User user;
	private String applicationUrl;

	public RegisterEvent(User user, String applicationUrl) {
		super(user);
		this.user=user;
		this.applicationUrl=applicationUrl;
	}

	
}
