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
import ru.obelisk.monitor.database.models.entity.Partition;
import ru.obelisk.monitor.database.models.services.PartitionService;
import ru.obelisk.monitor.database.models.services.TimeScheduleGroupService;
import ru.obelisk.monitor.database.models.services.TimezoneService;
import ru.obelisk.monitor.database.models.views.View;
import ru.obelisk.monitor.web.ui.datatables.DataSet;
import ru.obelisk.monitor.web.ui.datatables.DatatablesCriterias;
import ru.obelisk.monitor.web.ui.datatables.DatatablesResponse;
import ru.obelisk.monitor.web.ui.select2.Select2Result;

@Controller
@RequestMapping("/accessclass/partitions")
public class PartitionController {
	
	private static Logger logger = LogManager.getLogger(PartitionController.class);
	
	@Autowired
    private PartitionService partitionService;
	
	@Autowired
    private TimezoneService timezoneService;
	
	@Autowired
    private TimeScheduleGroupService timeScheduleGroupService;
		
	@RequestMapping(value = {"/search/partitions"}, method = RequestMethod.GET)
	@Secured("ROLE_ADMIN")
	public @ResponseBody List<Select2Result> searchPartition(@RequestParam String searchString) throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting search partition with term: {}",searchString);
		return partitionService.findPartitionByTerm(searchString); 
	}
	
	@JsonView(View.Partition.class)		
	@RequestMapping(value = {"/", "/index.html"}, method = RequestMethod.GET)
	@Secured("ROLE_ADMIN")
	public String indexPage(Model model) throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting Partitions page");
		Partition partition = new Partition();
		model.addAttribute("partition", partition);
		model.addAttribute("partitionAll", partitionService.getAllPartitions());
		return "accessclass/partitions/index";
	}
	
	@JsonView(View.Partition.class)	
	@RequestMapping(value = {"/ajax/serverside/partitiondata.json"}, method = RequestMethod.GET)
	@Secured("ROLE_ADMIN")
	public @ResponseBody DatatablesResponse<Partition> partitionsDatatables(
			@DatatableCriterias DatatablesCriterias criterias, 
			Model model) 
					throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting partitions data for table on index page");
			
		List<Partition> partitions = partitionService.findPartitionWithDatatablesCriterias(criterias);
		Long count = partitionService.getTotalCount();
		Long countFiltered = partitionService.getFilteredCount(criterias);
	    return DatatablesResponse.build(new DataSet<Partition>(partitions,count,countFiltered), criterias);
	}
	
	@JsonView(View.Partition.class)	
	@RequestMapping(value = {"/ajax/clientside/partitiondata.json"}, method = RequestMethod.GET)
	@Secured("ROLE_ADMIN")
	public @ResponseBody DatatablesResponse<Partition> partitionsDataClientSide(Model model) throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting partitions data for table on index page");
		List<Partition> partitions = partitionService.getAllPartitions();
		return DatatablesResponse.clientSideBuild(partitions);
	}
	
	@JsonView(View.Partition.class)	
	@RequestMapping(value = {"/create"}, method = RequestMethod.GET)
	@Secured("ROLE_ADMIN")
	public String viewCreatePartitionPage(Model model) throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting create pbx station page");
		Partition partition = new Partition();
		model.addAttribute("partition", partition);
		model.addAttribute("timezones", timezoneService.getAllTimezones());
		model.addAttribute("allTimeScheduleGroups", timeScheduleGroupService.getAllTimeScheduleGroups());
        return "accessclass/partitions/create";
	}
	
	@JsonView(View.Partition.class)	
	@RequestMapping(value = {"/create"}, method = RequestMethod.POST)
	@Secured("ROLE_ADMIN")
	public String saveCreatePartitionPage(	final ModelMap model, 
									@Valid @ModelAttribute("partition") final Partition partition, 
									final BindingResult bindingResult ) 
					throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting add pbx station method");
		if(bindingResult.hasErrors()){
			model.addAttribute("timezones", timezoneService.getAllTimezones());
			model.addAttribute("allTimeScheduleGroups", timeScheduleGroupService.getAllTimeScheduleGroups());
			return "accessclass/partitions/create";
		}
		partitionService.addPartition(partition);
		model.clear();
        return "redirect:/accessclass/partitions/update/"+partition.getId();
	}
	
	@JsonView(View.Partition.class)
	@RequestMapping(value = {"/update/{id}"}, method = RequestMethod.GET)
	@Secured("ROLE_ADMIN")
	public String viewUpdatePartitionPage(	ModelMap model, 
											@PathVariable(value = "id") int id) 
			throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting update pbx station page");
		Partition partition = partitionService.getPartitionById(id);
		model.addAttribute("timezones", timezoneService.getAllTimezones());
		model.addAttribute("allTimeScheduleGroups", timeScheduleGroupService.getAllTimeScheduleGroups());
		model.addAttribute("partition", partition);
		return "accessclass/partitions/update";
	}
	
	@JsonView(View.Partition.class)	
	@RequestMapping(value = {"/update"}, method = RequestMethod.PUT)
	@Secured("ROLE_ADMIN")
	public String saveUpdatePartitionPage(final ModelMap model, 
									@Valid @ModelAttribute("partition") final Partition formPartition, 
									final BindingResult bindingResult,
									SessionStatus status) 
			throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting save update pbx station method");
		if(bindingResult.hasErrors()){
			model.addAttribute("timezones", timezoneService.getAllTimezones());
			model.addAttribute("allTimeScheduleGroups", timeScheduleGroupService.getAllTimeScheduleGroups());
			return "accessclass/partitions/update";
		}
		partitionService.editPartition(formPartition);
		status.setComplete();
		return "redirect:/accessclass/partitions/index.html";
	}
	
	@JsonView(View.Partition.class)	
	@RequestMapping(value = {"/delete"}, method = RequestMethod.DELETE)
	@Secured("ROLE_ADMIN")
	public String deletePartition(int id, SessionStatus status) 
			throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting delete pbx station");
		partitionService.deletePartition(id);
		status.setComplete();
		return "redirect:/accessclass/partitions/index.html";
	}
}
