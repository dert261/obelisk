package ru.obelisk.monitor.config;

import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import ru.obelisk.monitor.database.models.entity.User;
import ru.obelisk.monitor.database.models.services.UserService;

@Component
public class LocalPrincipal {
	
	@Autowired private UserService userService;
	private User user = null;
	private String sessId = null;
	
	public User getUser(Authentication auth, HttpSession session){
		if(sessId==null || !sessId.equals(session.getId())){
			init(auth.getName());
			sessId=session.getId();
		}	
		return this.user;
	}
	
	private void init(String username){
		user = userService.getUserByUsername(username);
	}
}
