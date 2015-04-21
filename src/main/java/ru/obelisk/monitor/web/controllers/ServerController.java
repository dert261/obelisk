package ru.obelisk.monitor.web.controllers;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.asteriskjava.manager.AuthenticationFailedException;
import org.asteriskjava.manager.TimeoutException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.security.access.annotation.Secured;

//import ru.obelisk.message.data.ChannelsListImpl;
import ru.obelisk.message.data.HostInfo;
import ru.obelisk.message.data.HostInfoListImpl;
//import ru.obelisk.message.data.PeersListImpl;

@Controller
@RequestMapping("/server")
public class ServerController {
	//@Autowired private ChannelsListImpl channels;
	//@Autowired private PeersListImpl peers;
	@Autowired private HostInfoListImpl hostInfo;
	
	private static Logger logger = LogManager.getLogger(ServerController.class);
	
	@RequestMapping(value = {"", "/", "/all"}, method = RequestMethod.GET)
	@Secured("ROLE_ADMIN")
	public String serversPage(Model model) throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting servers page");
		return "servers";
	}
	
	@RequestMapping(value = {"/{name}"}, method = RequestMethod.GET)
	@Secured("ROLE_ADMIN")
	public String serverInfoPage(@PathVariable String name, Model model) throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting server page for {}", name);
		HostInfo host = hostInfo.getHostInfoByServername(name);
		model.addAttribute("host", host);
		model.addAttribute("hostname", host.getServerName());
		return "serverinfo";
	}
	
	@RequestMapping(value = {"/servers.json"}, method = RequestMethod.GET)
	@Secured("ROLE_ADMIN")
	public @ResponseBody List<HostInfo> serversData(HttpServletResponse res) throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception {
		return hostInfo.getHostInfoList();
	}
}
