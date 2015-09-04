package ru.obelisk.monitor.web.controllers;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.metadata.ConstraintDescriptor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.asteriskjava.manager.AuthenticationFailedException;
import org.asteriskjava.manager.TimeoutException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
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
import ru.obelisk.monitor.database.models.entity.TimePeriod;
import ru.obelisk.monitor.database.models.entity.enums.CalendarDays;
import ru.obelisk.monitor.database.models.entity.enums.CalendarMonthDays;
import ru.obelisk.monitor.database.models.entity.enums.CalendarMonths;
import ru.obelisk.monitor.database.models.services.TimePeriodService;
import ru.obelisk.monitor.database.models.views.View;
import ru.obelisk.monitor.web.ui.datatables.DataSet;
import ru.obelisk.monitor.web.ui.datatables.DatatablesCriterias;
import ru.obelisk.monitor.web.ui.datatables.DatatablesResponse;
import ru.obelisk.monitor.web.ui.select2.Select2Result;

@Controller
@RequestMapping("/calendar/timeperiods")
public class TimePeriodController {
	
	private static Logger logger = LogManager.getLogger(TimePeriodController.class);
	
	@Autowired
    private TimePeriodService timeperiodService;
	
	@Autowired(required = true)
	private javax.validation.Validator validator;
	
	@RequestMapping(value = {"/search/timeperiods"}, method = RequestMethod.GET)
	@Secured("ROLE_ADMIN")
	public @ResponseBody List<Select2Result> searchTimePeriod(@RequestParam String searchString) 
			throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting search timeperiod with term: {}",searchString);
		return timeperiodService.findTimePeriodByTerm(searchString);
	}
	
	@RequestMapping(value = {"/", "/index.html"}, method = RequestMethod.GET)
	@Secured("ROLE_ADMIN")
	public String indexPage(Model model) 
			throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting timeperiod page");
		TimePeriod timeperiod = new TimePeriod();
		model.addAttribute("timeperiod", timeperiod);
		model.addAttribute("timeperiodAll", timeperiodService.getAllTimePeriods());
		return "calendar/timeperiods/index";
	}
	
	@JsonView(View.TimePeriod.class)
	@RequestMapping(value = {"/ajax/serverside/timeperioddata.json"}, method = RequestMethod.GET)
	@Secured("ROLE_ADMIN")
	public @ResponseBody DatatablesResponse<TimePeriod> timeperiodsDatatables(
			@DatatableCriterias DatatablesCriterias criterias, 
			Model model) 
					throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting timeperiods data for table on index page");
		List<TimePeriod> timeperiods = timeperiodService.findTimePeriodWithDatatablesCriterias(criterias);
		Long count = timeperiodService.getTotalCount();
		Long countFiltered = timeperiodService.getFilteredCount(criterias);
		return DatatablesResponse.build(new DataSet<TimePeriod>(timeperiods,count,countFiltered), criterias);
	}
	
	@JsonView(View.TimePeriod.class)	
	@RequestMapping(value = {"/ajax/clientside/timeperioddata.json"}, method = RequestMethod.GET)
	@Secured("ROLE_ADMIN")
	public @ResponseBody DatatablesResponse<TimePeriod> timeperiodsDataClientSide(Model model) 
			throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting timeperiods data for table on index page");
		List<TimePeriod> timeperiod = timeperiodService.getAllTimePeriods();
		return DatatablesResponse.clientSideBuild(timeperiod);
	}
	
	@RequestMapping(value = {"/create"}, method = RequestMethod.GET)
	@Secured("ROLE_ADMIN")
	public String viewCreateTimePeriodPage(Model model) 
			throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting create timeperiod page");
		TimePeriod timeperiod = new TimePeriod();
		model.addAttribute("calendarDays", Arrays.asList(CalendarDays.values()));
		model.addAttribute("calendarMonths", Arrays.asList(CalendarMonths.values()));
		model.addAttribute("calendarMonthDays", Arrays.asList(CalendarMonthDays.values()));
		model.addAttribute("timeperiod", timeperiod);
		
		return "calendar/timeperiods/create";
	}
		
	@RequestMapping(value = {"/create"}, method = RequestMethod.POST)
	@Secured("ROLE_ADMIN")
	public String saveCreateTimePeriodPage(	final ModelMap model, 
									@Valid @ModelAttribute("timeperiod") final TimePeriod timeperiod, 
									final BindingResult bindingResult) 
					throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting add timeperiod method");
		model.addAttribute("calendarDays", Arrays.asList(CalendarDays.values()));
		model.addAttribute("calendarMonths", Arrays.asList(CalendarMonths.values()));
		model.addAttribute("calendarMonthDays", Arrays.asList(CalendarMonthDays.values()));
		
		if(isNotValid(timeperiod, bindingResult, 
				TimePeriod.TimePeriodValidationStepOne.class, 
				TimePeriod.TimePeriodValidationStepTwo.class)){
			return "calendar/timeperiods/create";
		}
		
		timeperiodService.addTimePeriod(timeperiod);
		model.clear();
        return "redirect:/calendar/timeperiods/index.html";
	}
	
	@RequestMapping(value = {"/update/{id}"}, method = RequestMethod.GET)
	@Secured("ROLE_ADMIN")
	public String viewUpdateTimePeriodPage(	ModelMap model, 
											@PathVariable(value = "id") int id) 
			throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting update timeperiods page");
		TimePeriod timeperiod = timeperiodService.getTimePeriodById(id);
		model.addAttribute("calendarDays", Arrays.asList(CalendarDays.values()));
		model.addAttribute("calendarMonths", Arrays.asList(CalendarMonths.values()));
		model.addAttribute("calendarMonthDays", Arrays.asList(CalendarMonthDays.values()));
		model.addAttribute("timeperiod", timeperiod);
		return "calendar/timeperiods/update";
	}
	
	@RequestMapping(value = {"/update"}, method = RequestMethod.PUT)
	@Secured("ROLE_ADMIN")
	public String saveUpdateTimePeriodPage(final ModelMap model, 
									@Valid @ModelAttribute("timeperiod") final TimePeriod formTimePeriod, 
									final BindingResult bindingResult,
									SessionStatus status) 
			throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting save update timeperiods method");
		model.addAttribute("calendarDays", Arrays.asList(CalendarDays.values()));
		model.addAttribute("calendarMonths", Arrays.asList(CalendarMonths.values()));
		model.addAttribute("calendarMonthDays", Arrays.asList(CalendarMonthDays.values()));
		
		if(isNotValid(formTimePeriod, bindingResult, 
				TimePeriod.TimePeriodValidationStepOne.class, 
				TimePeriod.TimePeriodValidationStepTwo.class)){
			return "calendar/timeperiods/update";
		}
		timeperiodService.editTimePeriod(formTimePeriod);
		status.setComplete();
		return "redirect:/calendar/timeperiods/index.html";
	}
	
	@RequestMapping(value = {"/delete"}, method = RequestMethod.DELETE)
	@Secured("ROLE_ADMIN")
	public String deleteTimePeriod(int id, SessionStatus status) 
			throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting delete timeperiod");
		timeperiodService.deleteTimePeriod(id);
		status.setComplete();
		return "redirect:/calendar/timeperiods/index.html";
	}
	
	
	private boolean isNotValid(Object target, Errors errors, Class<?>... groups) {
		Set<ConstraintViolation<Object>> result = validator.validate(target, groups);
		for (ConstraintViolation<Object> violation : result) {
			String field = violation.getPropertyPath().toString();
			FieldError fieldError = errors.getFieldError(field);
			if (fieldError == null || !fieldError.isBindingFailure()) {
				ConstraintDescriptor<?> constraintDescriptor = violation.getConstraintDescriptor();
				errors.rejectValue(field, constraintDescriptor.getAnnotation().annotationType().getSimpleName(), getArgumentsForConstraint(errors.getObjectName(), field, constraintDescriptor), violation.getMessage());
			}
		}
		return errors.hasErrors();
	}

	private Object[] getArgumentsForConstraint(String objectName, String field, ConstraintDescriptor<?> descriptor) {
		List<Object> arguments = new LinkedList<Object>();
		String[] codes = new String[] { objectName + Errors.NESTED_PATH_SEPARATOR + field, field };
		arguments.add(new DefaultMessageSourceResolvable(codes, field));
		Map<String, Object> attributesToExpose = new TreeMap<String, Object>();
		for (Map.Entry<String, Object> entry : descriptor.getAttributes().entrySet()) {
			String attributeName = entry.getKey();
			Object attributeValue = entry.getValue();
			if ("message".equals(attributeName) || "groups".equals(attributeName) || "payload".equals(attributeName)) {
				attributesToExpose.put(attributeName, attributeValue);
			}
		}
		arguments.addAll(attributesToExpose.values());
		return arguments.toArray(new Object[arguments.size()]);
	}
}
