package ru.obelisk.monitor.web.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.asteriskjava.manager.AuthenticationFailedException;
import org.asteriskjava.manager.TimeoutException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.security.access.annotation.Secured;

import ru.obelisk.monitor.annotations.DatatableCriterias;
import ru.obelisk.monitor.database.models.entity.User;
import ru.obelisk.monitor.database.models.entity.UserRole;
import ru.obelisk.monitor.database.models.entity.enums.UserStatus;
import ru.obelisk.monitor.database.models.entity.enums.UserType;
import ru.obelisk.monitor.database.models.services.UserRoleService;
import ru.obelisk.monitor.database.models.services.UserService;
import ru.obelisk.monitor.web.ui.datatables.DataSet;
import ru.obelisk.monitor.web.ui.datatables.DatatablesCriterias;
import ru.obelisk.monitor.web.ui.datatables.DatatablesResponse;
import ru.obelisk.monitor.web.ui.select2.Select2Result;

@Controller
@RequestMapping("/users")
public class UsersController {
	
	@Autowired
    private MessageSource messageSource;
	
	@Autowired
    private UserService userService;
	
	@Autowired
    private UserRoleService userRoleService;
	
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
	public List<UserRole> userRoleEnum() {
		//return Arrays.asList(UserRoleEnum.values());//userRoleService.getAllUserRoles();
		return userRoleService.getAllUserRoles();
	}
	
	@ModelAttribute("userAll")
	public List<User> userAll() {
		List<User> users = new ArrayList<User>();
		users.addAll(userService.getAllUsers());		
		return users;
	}
	
	@RequestMapping(value = {"/search/users"}, method = RequestMethod.GET)
	@Secured("ROLE_ADMIN")
	public @ResponseBody List<Select2Result> searchUser(@RequestParam String searchString) throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting search users with term: {}",searchString);
		return userService.findUserByTerm(searchString);
	}
	
	@RequestMapping(value = {"/", "/index.html"}, method = RequestMethod.GET)
	@Secured("ROLE_ADMIN")
	public String indexPage(Model model) throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting users page");
		User user = new User();
		model.addAttribute("user", user);
        return "users/index";
	}
	
	@RequestMapping(value = {"/ajax/serverside/userdata.json"}, method = RequestMethod.GET)
	@Secured("ROLE_ADMIN")
	public @ResponseBody DatatablesResponse<User> usersDatatables(
			@DatatableCriterias DatatablesCriterias criterias, 
			Model model, 
			Locale locale) 
					throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting users data for table on index page");
			
		List<User> users = i18nData(userService.findUserWithDatatablesCriterias(criterias), locale);
		Long count = userService.getTotalCount();
		Long countFiltered = userService.getFilteredCount(criterias);
	    return DatatablesResponse.build(new DataSet<User>(users,count,countFiltered), criterias);
	}
	
	@RequestMapping(value = {"/ajax/clientside/userdata.json"}, method = RequestMethod.GET)
	@Secured("ROLE_ADMIN")
	public @ResponseBody DatatablesResponse<User> usersDataClientSide(Model model, Locale locale) throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting users data for table on index page");
		List<User> users = i18nData(userService.getAllUsers(), locale);
		return DatatablesResponse.clientSideBuild(users);
	}
	
	private List<User> i18nData(List<User> users, Locale locale){
		List<User> result = new ArrayList<User>();
		for(User user : users){
			user.setStatusLocalized(messageSource.getMessage(user.getStatus().toString(),null, locale));
			user.setLocalUserFlagLocalized(messageSource.getMessage(user.getLocalUserFlag().toString(),null, locale));
			
			StringBuilder roleString = new StringBuilder();
			List<UserRole> roles = new ArrayList<UserRole>(user.getRoles());
			for(UserRole role : roles){
				roleString.append(messageSource.getMessage(role.getRoleName(),null, locale));
				roleString.append(";<br>");
			}
			user.setRoleLocalized(roleString.toString());
			result.add(user);
		}
		return result;
	}
	
	
	@RequestMapping(value = {"/create"}, method = RequestMethod.GET)
	@Secured("ROLE_ADMIN")
	public String viewCreatePage(Model model) throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting create user page");
		User user = new User();
		model.addAttribute("user", user);
        return "users/create";
	}
		
	@RequestMapping(value = {"/create"}, method = RequestMethod.POST)
	@Secured("ROLE_ADMIN")
	public String saveCreatePage(	final ModelMap model, 
									@Valid @ModelAttribute("user") final User user, 
									final BindingResult bindingResult ) 
					throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting add user method");
		if(bindingResult.hasErrors()){
			return "users/create";
		}
		user.setName(user.getLname()+" "+user.getFname()+" "+user.getMname());
		user.setSigninDate(new Date());
		userService.addUser(user);
		model.clear();
        return "redirect:/users/index.html";
	}
	
	@RequestMapping(value = {"/update/{id}"}, method = RequestMethod.GET)
	@Secured("ROLE_ADMIN")
	public String viewUpdatePage(ModelMap model, @PathVariable(value = "id") int id) throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting update user page");
		User user = userService.getUserById(id);
		model.addAttribute("user", user);
		return "users/update";
	}
	
	@RequestMapping(value = {"/update"}, method = RequestMethod.PUT)
	@Secured("ROLE_ADMIN")
	public String saveUpdatePage(final ModelMap model, 
									@Valid @ModelAttribute("user") final User formUser, 
									final BindingResult bindingResult,
									SessionStatus status) 
			throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting save update user method");
		if(bindingResult.hasErrors()){
			return "users/update";
		}
		userService.editUser(formUser);
		status.setComplete();
		return "redirect:/users/index.html";
	}
	
	@RequestMapping(value = {"/delete"}, method = RequestMethod.DELETE)
	@Secured("ROLE_ADMIN")
	public String deleteUser(int id, SessionStatus status) throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting delete user");
		userService.deleteUser(id);
		status.setComplete();
		return "redirect:/users/index.html";
	}
}
