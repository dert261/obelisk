package ru.obelisk.monitor.web.controllers;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.asteriskjava.manager.AuthenticationFailedException;
import org.asteriskjava.manager.TimeoutException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.security.access.annotation.Secured;

import ru.obelisk.monitor.database.models.entity.User;
import ru.obelisk.monitor.database.models.entity.enums.UserRole;
import ru.obelisk.monitor.database.models.entity.enums.UserStatus;
import ru.obelisk.monitor.database.models.entity.enums.UserType;
import ru.obelisk.monitor.database.models.services.UserService;


@Controller
@RequestMapping("/users")
public class UsersController {
	
	@Autowired
    private UserService userService;
	
	private static Logger logger = LogManager.getLogger(UsersController.class);
	
	@ModelAttribute("userStatus")
	public List<UserStatus> userStatus() {
	    return Arrays.asList(UserStatus.values());
	}
	
	@ModelAttribute("userType")
	public List<UserType> userType() {
	    return Arrays.asList(UserType.values());
	}
	
	@ModelAttribute("userRole")
	public List<UserRole> userRole() {
	    return Arrays.asList(UserRole.values());
	}
	
	@RequestMapping(value = {"/", "/index.html"}, method = RequestMethod.GET)
	@Secured("ROLE_ADMIN")
	public String indexPage(Model model) throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting users page");
		List<User> users = userService.getAllUsers();
		model.addAttribute("users", users);
        /*User user = new User();
        user.setName("Боженков Владимир Петрович");
        user.setRole(UserRole.ADMIN);
        userService.addUser(user);*/
		
		return "/users/index";
	}
	
	@RequestMapping(value = {"/create"}, method = RequestMethod.GET)
	@Secured("ROLE_ADMIN")
	public String createPage(Model model) throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting create user page");
		User user = new User();
		model.addAttribute("user", user);
        return "/users/create";
	}
	
	@RequestMapping(value = {"/create"}, method = RequestMethod.POST)
	@Secured("ROLE_ADMIN")
	public String addUserPage(final ModelMap model, @Valid final User user, final BindingResult bindingResult ) throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting add user method");
		if(bindingResult.hasErrors()){
			return "/users/create";
		}
		user.setName(user.getLname()+" "+user.getFname()+" "+user.getMname());
		userService.addUser(user);
		model.clear();
        return "redirect:/users/create";
	}
}
