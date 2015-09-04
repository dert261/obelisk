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
import ru.obelisk.monitor.database.models.entity.Timezone;
import ru.obelisk.monitor.database.models.services.TimezoneService;
import ru.obelisk.monitor.database.models.views.View;
import ru.obelisk.monitor.web.ui.datatables.DataSet;
import ru.obelisk.monitor.web.ui.datatables.DatatablesCriterias;
import ru.obelisk.monitor.web.ui.datatables.DatatablesResponse;
import ru.obelisk.monitor.web.ui.select2.Select2Result;

@Controller
@RequestMapping("/calendar/timezones")
public class TimezoneController {
	
	private static Logger logger = LogManager.getLogger(TimezoneController.class);
	
	@Autowired
    private TimezoneService timezoneService;
	
	@RequestMapping(value = {"/search/timezones"}, method = RequestMethod.GET)
	@Secured("ROLE_ADMIN")
	public @ResponseBody List<Select2Result> searchTimezone(@RequestParam String searchString) 
			throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting search timezone with term: {}",searchString);
		return timezoneService.findTimezoneByTerm(searchString);
	}
	
	@RequestMapping(value = {"/", "/index.html"}, method = RequestMethod.GET)
	@Secured("ROLE_ADMIN")
	public String indexPage(Model model) 
			throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting timezone page");
		Timezone timezone = new Timezone();
		model.addAttribute("timezone", timezone);
		model.addAttribute("timezoneAll", timezoneService.getAllTimezones());
		return "calendar/timezones/index";
	}
	
	@JsonView(View.Timezone.class)
	@RequestMapping(value = {"/ajax/serverside/timezonedata.json"}, method = RequestMethod.GET)
	@Secured("ROLE_ADMIN")
	public @ResponseBody DatatablesResponse<Timezone> timezonesDatatables(
			@DatatableCriterias DatatablesCriterias criterias, 
			Model model) 
					throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting timezones data for table on index page");
		List<Timezone> timezones = timezoneService.findTimezoneWithDatatablesCriterias(criterias);
		Long count = timezoneService.getTotalCount();
		Long countFiltered = timezoneService.getFilteredCount(criterias);
		return DatatablesResponse.build(new DataSet<Timezone>(timezones,count,countFiltered), criterias);
	}
	
	@JsonView(View.Timezone.class)	
	@RequestMapping(value = {"/ajax/clientside/timezonedata.json"}, method = RequestMethod.GET)
	@Secured("ROLE_ADMIN")
	public @ResponseBody DatatablesResponse<Timezone> timezonesDataClientSide(Model model) 
			throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting timezones data for table on index page");
		List<Timezone> timezone = timezoneService.getAllTimezones();
		return DatatablesResponse.clientSideBuild(timezone);
	}
	
	@RequestMapping(value = {"/create"}, method = RequestMethod.GET)
	@Secured("ROLE_ADMIN")
	public String viewCreateTimezonePage(Model model) 
			throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting create timezone page");
		Timezone timezone = new Timezone();
		model.addAttribute("timezone", timezone);
		return "calendar/timezones/create";
	}
		
	@RequestMapping(value = {"/create"}, method = RequestMethod.POST)
	@Secured("ROLE_ADMIN")
	public String saveCreateTimezonePage(	final ModelMap model, 
									@Valid @ModelAttribute("timezone") final Timezone timezone, 
									final BindingResult bindingResult) 
					throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting add timezone method");
		if(bindingResult.hasErrors()){
			return "calendar/timezones/create";
		}
		timezoneService.addTimezone(timezone);
		model.clear();
        return "redirect:/calendar/timezones/index.html";
	}
	
	@RequestMapping(value = {"/update/{id}"}, method = RequestMethod.GET)
	@Secured("ROLE_ADMIN")
	public String viewUpdateTimezonePage(	ModelMap model, 
											@PathVariable(value = "id") int id) 
			throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting update timezones page");
		Timezone timezone = timezoneService.getTimezoneById(id);
		model.addAttribute("timezone", timezone);
		return "calendar/timezones/update";
	}
	
	@RequestMapping(value = {"/update"}, method = RequestMethod.PUT)
	@Secured("ROLE_ADMIN")
	public String saveUpdateTimezonePage(final ModelMap model, 
									@Valid @ModelAttribute("timezone") final Timezone formTimezone, 
									final BindingResult bindingResult,
									SessionStatus status) 
			throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting save update timezones method");
		if(bindingResult.hasErrors()){
			return "calendar/timezones/update";
		}
		timezoneService.editTimezone(formTimezone);
		status.setComplete();
		return "redirect:/calendar/timezones/index.html";
	}
	
	@RequestMapping(value = {"/delete"}, method = RequestMethod.DELETE)
	@Secured("ROLE_ADMIN")
	public String deleteTimezone(int id, SessionStatus status) 
			throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting delete timezone");
		timezoneService.deleteTimezone(id);
		status.setComplete();
		return "redirect:/calendar/timezones/index.html";
	}
}
