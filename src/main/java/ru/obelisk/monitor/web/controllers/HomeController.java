package ru.obelisk.monitor.web.controllers;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.asteriskjava.manager.AuthenticationFailedException;
import org.asteriskjava.manager.TimeoutException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.security.access.annotation.Secured;

//import ru.obelisk.message.data.ChannelsListImpl;
import ru.obelisk.message.data.HostInfoListImpl;
//import ru.obelisk.message.data.HostInfo;
//import ru.obelisk.message.data.HostInfoListImpl;
//import ru.obelisk.message.data.PeersListImpl;

@Controller
public class HomeController {
	//@Autowired private ChannelsListImpl channels;
	//@Autowired private PeersListImpl peers;
	@Autowired private HostInfoListImpl hostInfo;
	
	private static Logger logger = LogManager.getLogger(HomeController.class);
	
	@RequestMapping(value = {"/", "/home.html", "/index.html"}, method = RequestMethod.GET)
	@Secured("ROLE_ADMIN")
	public String homePage(Model model) throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting channels page");
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
}
