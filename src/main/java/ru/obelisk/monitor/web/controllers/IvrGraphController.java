package ru.obelisk.monitor.web.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.asteriskjava.manager.AuthenticationFailedException;
import org.asteriskjava.manager.TimeoutException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.security.access.annotation.Secured;

import ru.obelisk.monitor.graph.HandsetInNode;
import ru.obelisk.monitor.graph.Node;

@Controller
@RequestMapping("/ivrs")
public class IvrGraphController {
	
	private static Logger logger = LogManager.getLogger(IvrGraphController.class);
	
	private Map<Integer, Node> schemaMap = new HashMap<Integer, Node>();
	
	@RequestMapping(value = {"/", "/index.html"}, method = RequestMethod.GET)
	@Secured("ROLE_ADMIN")
	public String indexPage(Model model) 
			throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting ivr index page");
		return "ivrs/index";
	}
	
	@RequestMapping(value = {"/infoNode"}, method = RequestMethod.GET)
	@Secured("ROLE_ADMIN")
	public String getNodeInfo(Model model,
					@RequestParam int index,
					@RequestParam String type
			) throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{ 
		logger.info("Requesting ivr node info objects");
		
		Node node = null;
		if(schemaMap.containsKey(index)){
			node = schemaMap.get(index);
		} else {
			node = new HandsetInNode();
			node.setIndex(index);
			node.setType(type);
		}
		model.addAttribute("node", node);
		return "ivrs/nodes/"+node.getType()+"::"+node.getType()+"FormContent";
	}
	
	@RequestMapping(value = {"/addNode"}, method = RequestMethod.POST)
	@Secured("ROLE_ADMIN")
	public @ResponseBody Node getNewNodeInfo(
					@RequestParam int index,
					@RequestParam String type
			) throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting ivr new node info objects");
		HandsetInNode node = new HandsetInNode();
		node.setIndex(index);
		node.setType(type);
		schemaMap.put(index, node);
		return node;
	}
	
	@RequestMapping(value = {"/removeNode"}, method = RequestMethod.POST)
	@Secured("ROLE_ADMIN")
	public @ResponseBody Node removeNode(
					@RequestParam int index,
					@RequestParam String type
			) throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting ivr remove node objects");
		Node node = null;
		if(schemaMap.containsKey(index)){
			node = schemaMap.get(index);
			schemaMap.remove(index);
		}	
		return node;
	}
	
	
}
