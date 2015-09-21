package ru.obelisk.monitor.web.controllers;

import java.io.IOException;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.security.access.annotation.Secured;

import com.fasterxml.jackson.annotation.JsonView;

import ru.obelisk.monitor.annotations.DatatableCriterias;
import ru.obelisk.monitor.database.models.entity.CallingSearchSpace;
import ru.obelisk.monitor.database.models.services.CallingSearchSpaceService;
import ru.obelisk.monitor.database.models.services.PartitionService;
import ru.obelisk.monitor.database.models.views.View;
import ru.obelisk.monitor.web.ui.datatables.DataSet;
import ru.obelisk.monitor.web.ui.datatables.DatatablesCriterias;
import ru.obelisk.monitor.web.ui.datatables.DatatablesResponse;
import ru.obelisk.monitor.web.ui.select2.Select2Result;

@Controller
@RequestMapping("/accessclass/css")
public class CallingSearchSpaceController {
	
	private static Logger logger = LogManager.getLogger(CallingSearchSpaceController.class);
	
	@Autowired
    private CallingSearchSpaceService cssService;
	
	@Autowired
    private PartitionService partitionService;
	
	@RequestMapping(value = {"/search/css"}, method = RequestMethod.GET)
	@Secured("ROLE_ADMIN")
	public @ResponseBody List<Select2Result> searchCallingSearchSpace(@RequestParam String searchString) throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting search css with term: {}",searchString);
		return cssService.findCallingSearchSpaceByTerm(searchString); 
	}
	
	@JsonView(View.CallingSearchSpace.class)		
	@RequestMapping(value = {"/", "/index.html"}, method = RequestMethod.GET)
	@Secured("ROLE_ADMIN")
	public String indexPage(Model model) throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting CallingSearchSpaces page");
		CallingSearchSpace css = new CallingSearchSpace();
		model.addAttribute("css", css);
		model.addAttribute("cssAll", cssService.getAllCallingSearchSpaces());
		return "accessclass/css/index";
	}
	
	@JsonView(View.CallingSearchSpace.class)	
	@RequestMapping(value = {"/ajax/serverside/cssdata.json"}, method = RequestMethod.GET)
	@Secured("ROLE_ADMIN")
	public @ResponseBody DatatablesResponse<CallingSearchSpace> cssDatatables(
			@DatatableCriterias DatatablesCriterias criterias, 
			Model model) 
					throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting css data for table on index page");
			
		List<CallingSearchSpace> css = cssService.findCallingSearchSpaceWithDatatablesCriterias(criterias);
		Long count = cssService.getTotalCount();
		Long countFiltered = cssService.getFilteredCount(criterias);
	    return DatatablesResponse.build(new DataSet<CallingSearchSpace>(css,count,countFiltered), criterias);
	}
	
	@JsonView(View.CallingSearchSpace.class)	
	@RequestMapping(value = {"/ajax/clientside/cssdata.json"}, method = RequestMethod.GET)
	@Secured("ROLE_ADMIN")
	public @ResponseBody DatatablesResponse<CallingSearchSpace> cssDataClientSide(Model model) throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting css data for table on index page");
		List<CallingSearchSpace> css = cssService.getAllCallingSearchSpaces();
		return DatatablesResponse.clientSideBuild(css);
	}
	
	@JsonView(View.CallingSearchSpace.class)	
	@RequestMapping(value = {"/create"}, method = RequestMethod.GET)
	@Secured("ROLE_ADMIN")
	public String viewCreateCallingSearchSpacePage(Model model) throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting create css page");
		CallingSearchSpace css = new CallingSearchSpace();
		model.addAttribute("css", css);
		model.addAttribute("allPartitions", partitionService.getAllPartitions());
        return "accessclass/css/create";
	}
	
	@JsonView(View.CallingSearchSpace.class)	
	@RequestMapping(value = {"/create"}, method = RequestMethod.POST)
	@Secured("ROLE_ADMIN")
	public String saveCreateCallingSearchSpacePage(	final ModelMap model, 
									@Valid @ModelAttribute("css") final CallingSearchSpace css, 
									final BindingResult bindingResult ) 
					throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting add css method");
		if(bindingResult.hasErrors()){
			model.addAttribute("allPartitions", partitionService.getAllPartitions());
			return "accessclass/css/create";
		}
		cssService.addCallingSearchSpace(css);
		model.clear();
        return "redirect:/accessclass/css/update/"+css.getId();
	}
	
	@JsonView(View.CallingSearchSpace.class)
	@RequestMapping(value = {"/update/{id}"}, method = RequestMethod.GET)
	@Secured("ROLE_ADMIN")
	public String viewUpdateCallingSearchSpacePage(	ModelMap model, 
											@PathVariable(value = "id") int id) 
			throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting update css page");
		CallingSearchSpace css = cssService.getCallingSearchSpaceById(id);
		model.addAttribute("allPartitions", partitionService.getAllPartitions());
		model.addAttribute("css", css);
		return "accessclass/css/update";
	}
	
	@JsonView(View.CallingSearchSpace.class)	
	@RequestMapping(value = {"/update"}, method = RequestMethod.PUT)
	@Secured("ROLE_ADMIN")
	public String saveUpdateCallingSearchSpacePage(final ModelMap model, 
									@Valid @ModelAttribute("css") final CallingSearchSpace formCallingSearchSpace, 
									final BindingResult bindingResult,
									SessionStatus status) 
			throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting save update css method");
		if(bindingResult.hasErrors()){
			model.addAttribute("allPartitions", partitionService.getAllPartitions());
			return "accessclass/css/update";
		}
		cssService.editCallingSearchSpace(formCallingSearchSpace);
		status.setComplete();
		return "redirect:/accessclass/css/index.html";
	}
	
	@JsonView(View.CallingSearchSpace.class)	
	@RequestMapping(value = {"/delete"}, method = RequestMethod.DELETE)
	@Secured("ROLE_ADMIN")
	public String deleteCallingSearchSpace(int id, SessionStatus status) 
			throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting delete css");
		cssService.deleteCallingSearchSpace(id);
		status.setComplete();
		return "redirect:/accessclass/css/index.html";
	}
}
