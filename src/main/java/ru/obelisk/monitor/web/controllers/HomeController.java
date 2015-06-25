package ru.obelisk.monitor.web.controllers;

import java.io.IOException;
import java.security.Principal;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.asteriskjava.manager.AuthenticationFailedException;
import org.asteriskjava.manager.TimeoutException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.security.access.annotation.Secured;

import ru.obelisk.message.data.HostInfoListImpl;
import ru.obelisk.monitor.database.models.services.UserService;


@Controller
public class HomeController {
	@Autowired private HostInfoListImpl hostInfo;
	
	@Autowired private UserService userService;
	
	private static Logger logger = LogManager.getLogger(HomeController.class);
		
	@RequestMapping(value = {"/", "/home.html", "/index.html"}, method = RequestMethod.GET)
	@Secured("ROLE_ADMIN")
	public String homePage(Model model, Principal principal, HttpSession session) 
			throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting channels page");
		/*User user = userService.getUserByUsername(principal.getName()); 
		session.setAttribute("principal_name", user.getFname()+" "+user.getLname());
		logger.info("Set session principal_name attribute in: {}",user);*/
		return "home";
	}
	
	@RequestMapping(value = {"/channels.html"}, method = RequestMethod.GET)
	@Secured("ROLE_ADMIN")
	public String channelsPage(Model model) throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting channels page");
		return "channels";
	}
	
	@RequestMapping(value = {"/peers.html"}, method = RequestMethod.GET)
	@Secured("ROLE_ADMIN")
	public String peersPage(Model model) throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting peers page");
		return "peers";
	}
	
	/*@RequestMapping(value = {"/servers.html"}, method = RequestMethod.GET)
	@Secured("ROLE_ADMIN")
	public String serversPage(Model model) throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting servers page");
		return "servers";
	}
	
	@RequestMapping(value = {"/serverinfo.html?name={name}"}, method = RequestMethod.GET)
	@Secured("ROLE_ADMIN")
	public String serverInfoPage(@PathVariable String name, Model model) throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting server page for {}", name);
		HostInfo host = hostInfo.getHostInfoByServername(name);
		model.addAttribute("host", host);
		return "serverinfo";
	}*/
	
	
	@ExceptionHandler(Exception.class)
	public ModelAndView handleException(HttpServletRequest req, Exception e) {
		logger.warn("In handleException");
		ModelAndView mav = new ModelAndView();
		mav.addObject("exception", e);
		mav.addObject("timestamp", new Date());
		mav.addObject("url", req.getRequestURL());
		mav.setViewName("exception");
		return mav;
	}
}
