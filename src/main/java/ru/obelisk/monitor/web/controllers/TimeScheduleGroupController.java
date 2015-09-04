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
import ru.obelisk.monitor.database.models.entity.TimeScheduleGroup;
import ru.obelisk.monitor.database.models.services.TimeScheduleGroupService;
import ru.obelisk.monitor.database.models.views.View;
import ru.obelisk.monitor.web.ui.datatables.DataSet;
import ru.obelisk.monitor.web.ui.datatables.DatatablesCriterias;
import ru.obelisk.monitor.web.ui.datatables.DatatablesResponse;
import ru.obelisk.monitor.web.ui.select2.Select2Result;

@Controller
@RequestMapping("/calendar/timeschedgroups")
public class TimeScheduleGroupController {
	
	private static Logger logger = LogManager.getLogger(TimeScheduleGroupController.class);
	
	@Autowired
    private TimeScheduleGroupService timeschedgroupService;
	
	@RequestMapping(value = {"/search/timeschedgroups"}, method = RequestMethod.GET)
	@Secured("ROLE_ADMIN")
	public @ResponseBody List<Select2Result> searchTimeScheduleGroup(@RequestParam String searchString) 
			throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting search timeschedgroup with term: {}",searchString);
		return timeschedgroupService.findTimeScheduleGroupByTerm(searchString);
	}
	
	@RequestMapping(value = {"/", "/index.html"}, method = RequestMethod.GET)
	@Secured("ROLE_ADMIN")
	public String indexPage(Model model) 
			throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting timeschedgroup page");
		TimeScheduleGroup timeschedgroup = new TimeScheduleGroup();
		model.addAttribute("timeschedgroup", timeschedgroup);
		model.addAttribute("timeschedgroupAll", timeschedgroupService.getAllTimeScheduleGroups());
		return "calendar/timeschedgroups/index";
	}
	
	@JsonView(View.TimeScheduleGroup.class)
	@RequestMapping(value = {"/ajax/serverside/timeschedgroupdata.json"}, method = RequestMethod.GET)
	@Secured("ROLE_ADMIN")
	public @ResponseBody DatatablesResponse<TimeScheduleGroup> timeschedgroupsDatatables(
			@DatatableCriterias DatatablesCriterias criterias, 
			Model model) 
					throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting timeschedgroups data for table on index page");
		List<TimeScheduleGroup> timeschedgroups = timeschedgroupService.findTimeScheduleGroupWithDatatablesCriterias(criterias);
		Long count = timeschedgroupService.getTotalCount();
		Long countFiltered = timeschedgroupService.getFilteredCount(criterias);
		return DatatablesResponse.build(new DataSet<TimeScheduleGroup>(timeschedgroups,count,countFiltered), criterias);
	}
	
	@JsonView(View.TimeScheduleGroup.class)	
	@RequestMapping(value = {"/ajax/clientside/timeschedgroupdata.json"}, method = RequestMethod.GET)
	@Secured("ROLE_ADMIN")
	public @ResponseBody DatatablesResponse<TimeScheduleGroup> timeschedgroupsDataClientSide(Model model) 
			throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting timeschedgroups data for table on index page");
		List<TimeScheduleGroup> timeschedgroup = timeschedgroupService.getAllTimeScheduleGroups();
		return DatatablesResponse.clientSideBuild(timeschedgroup);
	}
	
	@RequestMapping(value = {"/create"}, method = RequestMethod.GET)
	@Secured("ROLE_ADMIN")
	public String viewCreateTimeScheduleGroupPage(Model model) 
			throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting create timeschedgroup page");
		TimeScheduleGroup timeschedgroup = new TimeScheduleGroup();
		model.addAttribute("timeschedgroup", timeschedgroup);
		return "calendar/timeschedgroups/create";
	}
		
	@RequestMapping(value = {"/create"}, method = RequestMethod.POST)
	@Secured("ROLE_ADMIN")
	public String saveCreateTimeScheduleGroupPage(	final ModelMap model, 
									@Valid @ModelAttribute("timeschedgroup") final TimeScheduleGroup timeschedgroup, 
									final BindingResult bindingResult) 
					throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting add timeschedgroup method");
		if(bindingResult.hasErrors()){
			return "calendar/timeschedgroups/create";
		}
		timeschedgroupService.addTimeScheduleGroup(timeschedgroup);
		model.clear();
        return "redirect:/calendar/timeschedgroups/index.html";
	}
	
	@RequestMapping(value = {"/update/{id}"}, method = RequestMethod.GET)
	@Secured("ROLE_ADMIN")
	public String viewUpdateTimeScheduleGroupPage(	ModelMap model, 
											@PathVariable(value = "id") int id) 
			throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting update timeschedgroups page");
		TimeScheduleGroup timeschedgroup = timeschedgroupService.getTimeScheduleGroupById(id);
		model.addAttribute("timeschedgroup", timeschedgroup);
		return "calendar/timeschedgroups/update";
	}
	
	@RequestMapping(value = {"/update"}, method = RequestMethod.PUT)
	@Secured("ROLE_ADMIN")
	public String saveUpdateTimeScheduleGroupPage(final ModelMap model, 
									@Valid @ModelAttribute("timeschedgroup") final TimeScheduleGroup formTimeScheduleGroup, 
									final BindingResult bindingResult,
									SessionStatus status) 
			throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting save update timeschedgroups method");
		if(bindingResult.hasErrors()){
			return "calendar/timeschedgroups/update";
		}
		timeschedgroupService.editTimeScheduleGroup(formTimeScheduleGroup);
		status.setComplete();
		return "redirect:/calendar/timeschedgroups/index.html";
	}
	
	@RequestMapping(value = {"/delete"}, method = RequestMethod.DELETE)
	@Secured("ROLE_ADMIN")
	public String deleteTimeScheduleGroup(int id, SessionStatus status) 
			throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting delete timeschedgroup");
		timeschedgroupService.deleteTimeScheduleGroup(id);
		status.setComplete();
		return "redirect:/calendar/timeschedgroups/index.html";
	}
}
