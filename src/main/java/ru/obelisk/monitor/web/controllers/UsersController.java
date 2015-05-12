package ru.obelisk.monitor.web.controllers;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.asteriskjava.manager.AuthenticationFailedException;
import org.asteriskjava.manager.TimeoutException;
import org.neo4j.cypher.internal.compiler.v2_1.planner.logical.findShortestPaths;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.security.access.annotation.Secured;

import com.github.dandelion.datatables.core.ajax.DataSet;
import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;
import com.github.dandelion.datatables.core.ajax.DatatablesResponse;
import com.github.dandelion.datatables.extras.spring3.ajax.DatatablesParams;

import ru.obelisk.monitor.database.models.entity.User;
import ru.obelisk.monitor.database.models.entity.enums.UserRole;
import ru.obelisk.monitor.database.models.entity.enums.UserStatus;
import ru.obelisk.monitor.database.models.entity.enums.UserType;
import ru.obelisk.monitor.database.models.services.UserService;
import ru.obelisk.monitor.web.utils.conversion.DataTablesResponse;


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
        return "/users/index";
	}
	
	/*@RequestMapping(value = {"/ajax/userdata.json"}, method = RequestMethod.GET)
	@Secured("ROLE_ADMIN")
	@ResponseBody
	public List<User> usersData(Model model) throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting users data for table on index page");
		List<User> users = userService.getAllUsers();
		return users;
	}*/
	
	
	@RequestMapping(value = "/ajax/userdata.json")
	@Secured("ROLE_ADMIN")
	public @ResponseBody DatatablesResponse<User> data(@DatatablesParams DatatablesCriterias criterias){
		List<User> users = userService.getAllUsers();
		Long size= new Long(users.size());
	    DataSet<User> dataset = new DataSet<User>(users,size,size);
	    return DatatablesResponse.build(dataset, criterias);
	}
	
	/*@RequestMapping(value = "/persons")
	public @ResponseBody DatatablesResponse findAll(@DatatablesParams DatatablesCriterias criterias) {
	   DataSet dataSet = personService.findPersonsWithDatatablesCriterias(criterias);
	   return DatatablesResponse.build(dataSet, criterias);
	}*/
	
	
	/*@RequestMapping(value = {"/ajax/userdata.json"}, method = RequestMethod.GET)
	@Secured("ROLE_ADMIN")
	@ResponseBody
	public DataTablesResponse<Object> usersDatatables(Model model) throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting users data for table on index page");
		List<User> users = userService.getAllUsers();
		return users;
	}*/
	
	@RequestMapping(value = {"/create"}, method = RequestMethod.GET)
	@Secured("ROLE_ADMIN")
	public String viewCreatePage(Model model) throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting create user page");
		User user = new User();
		model.addAttribute("user", user);
        return "/users/form";
	}
		
	@RequestMapping(value = {"/create"}, method = RequestMethod.POST)
	@Secured("ROLE_ADMIN")
	public String saveCreatePage(final ModelMap model, @Valid final User user, final BindingResult bindingResult ) throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting add user method");
		if(bindingResult.hasErrors()){
			return "/users/form";
		}
		user.setName(user.getLname()+" "+user.getFname()+" "+user.getMname());
		user.setSigninDate(new Date());
		userService.addUser(user);
		model.clear();
        return "redirect:/users/update/"+user.getId();
	}
	
	@RequestMapping(value = {"/update/{id}"}, method = RequestMethod.GET)
	@Secured("ROLE_ADMIN")
	public String viewUpdatePage(ModelMap model, @PathVariable(value = "id") int id) throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting update user page");
		User user = userService.getUserById(id);
		model.addAttribute("user", user);
        return "/users/form";
	}
	
	@RequestMapping(value = {"/update/{id}"}, method = RequestMethod.POST)
	@Secured("ROLE_ADMIN")
	public String saveUpdatePage(ModelMap model, @PathVariable(value = "id") int id) throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting save update user method");
		User user = new User();
		model.addAttribute("user", user);
		model.clear();
        return "/users/form";
	}
	
	@RequestMapping(value = {"/delete/{id}"}, method = RequestMethod.GET)
	@Secured("ROLE_ADMIN")
	public String deleteUser(final ModelMap model, @PathVariable(value = "id") int id) throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting delete user");
		userService.deleteUser(id);
		model.clear();
		return "redirect:/users/index.html";
	}
}
