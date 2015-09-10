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
import ru.obelisk.monitor.database.models.entity.PbxStationGroup;
import ru.obelisk.monitor.database.models.services.PbxStationGroupService;
import ru.obelisk.monitor.database.models.services.PbxStationService;
import ru.obelisk.monitor.database.models.views.View;
import ru.obelisk.monitor.web.ui.datatables.DataSet;
import ru.obelisk.monitor.web.ui.datatables.DatatablesCriterias;
import ru.obelisk.monitor.web.ui.datatables.DatatablesResponse;
import ru.obelisk.monitor.web.ui.select2.Select2Result;

@Controller
@RequestMapping("/stations/pbxstationgroups")
public class PbxStationGroupController {
	
	private static Logger logger = LogManager.getLogger(PbxStationGroupController.class);
	
	@Autowired
    private PbxStationGroupService pbxstationgroupService;
	@Autowired
    private PbxStationService pbxstationService;
		
	@RequestMapping(value = {"/search/pbxstationgroups"}, method = RequestMethod.GET)
	@Secured("ROLE_ADMIN")
	public @ResponseBody List<Select2Result> searchPbxStationGroup(@RequestParam String searchString) 
			throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting search pbxstationgroup with term: {}",searchString);
		return pbxstationgroupService.findPbxStationGroupByTerm(searchString);
	}
	
	@RequestMapping(value = {"/", "/index.html"}, method = RequestMethod.GET)
	@Secured("ROLE_ADMIN")
	public String indexPage(Model model) 
			throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting pbxstationgroup page");
		PbxStationGroup pbxstationgroup = new PbxStationGroup();
		model.addAttribute("pbxstationgroup", pbxstationgroup);
		model.addAttribute("pbxstationgroupAll", pbxstationgroupService.getAllPbxStationGroups());
		return "stations/pbxstationgroups/index";
	}
	
	@JsonView(View.PbxStationGroup.class)
	@RequestMapping(value = {"/ajax/serverside/pbxstationgroupdata.json"}, method = RequestMethod.GET)
	@Secured("ROLE_ADMIN")
	public @ResponseBody DatatablesResponse<PbxStationGroup> pbxstationgroupsDatatables(
			@DatatableCriterias DatatablesCriterias criterias, 
			Model model) 
					throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting pbxstationgroups data for table on index page");
		List<PbxStationGroup> pbxstationgroups = pbxstationgroupService.findPbxStationGroupWithDatatablesCriterias(criterias);
		Long count = pbxstationgroupService.getTotalCount();
		Long countFiltered = pbxstationgroupService.getFilteredCount(criterias);
		return DatatablesResponse.build(new DataSet<PbxStationGroup>(pbxstationgroups,count,countFiltered), criterias);
	}
	
	@JsonView(View.PbxStationGroup.class)	
	@RequestMapping(value = {"/ajax/clientside/pbxstationgroupdata.json"}, method = RequestMethod.GET)
	@Secured("ROLE_ADMIN")
	public @ResponseBody DatatablesResponse<PbxStationGroup> pbxstationgroupsDataClientSide(Model model) 
			throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting pbxstationgroups data for table on index page");
		List<PbxStationGroup> pbxstationgroup = pbxstationgroupService.getAllPbxStationGroups();
		return DatatablesResponse.clientSideBuild(pbxstationgroup);
	}
	
	@RequestMapping(value = {"/create"}, method = RequestMethod.GET)
	@Secured("ROLE_ADMIN")
	public String viewCreatePbxStationGroupPage(Model model) 
			throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting create pbxstationgroup page");
		PbxStationGroup pbxstationgroup = new PbxStationGroup();
		model.addAttribute("pbxstationgroup", pbxstationgroup);
		return "stations/pbxstationgroups/create";
	}
		
	@RequestMapping(value = {"/create"}, method = RequestMethod.POST)
	@Secured("ROLE_ADMIN")
	public String saveCreatePbxStationGroupPage(	final ModelMap model, 
									@Valid @ModelAttribute("pbxstationgroup") final PbxStationGroup pbxstationgroup, 
									final BindingResult bindingResult) 
					throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting add pbxstationgroup method");
		if(bindingResult.hasErrors()){
			return "stations/pbxstationgroups/create";
		}
		pbxstationgroupService.addPbxStationGroup(pbxstationgroup);
		model.clear();
        return "redirect:/stations/pbxstationgroups/update/"+pbxstationgroup.getId();
	}
	@JsonView(View.PbxStationGroup.class)	
	@RequestMapping(value = {"/update/{id}"}, method = RequestMethod.GET)
	@Secured("ROLE_ADMIN")
	public String viewUpdatePbxStationGroupPage(	ModelMap model, 
											@PathVariable(value = "id") int id) 
			throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting update pbxstationgroups page");
		
		PbxStationGroup pbxstationgroup = pbxstationgroupService.getPbxStationGroupById(id);
		model.addAttribute("allPbxStations", pbxstationService.getAllPbxStations());
		model.addAttribute("pbxstationgroup", pbxstationgroup);
		return "stations/pbxstationgroups/update";
	}
	@JsonView(View.PbxStationGroup.class)	
	@RequestMapping(value = {"/update"}, method = RequestMethod.PUT)
	@Secured("ROLE_ADMIN")
	public String saveUpdatePbxStationGroupPage(final ModelMap model, 
									@Valid @ModelAttribute("pbxstationgroup") final PbxStationGroup formPbxStationGroup, 
									final BindingResult bindingResult,
									SessionStatus status) 
			throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting save update pbxstationgroups method");
		if(bindingResult.hasErrors()){
			model.addAttribute("allPbxStations", pbxstationService.getAllPbxStations());
			return "stations/pbxstationgroups/update";
		}
		pbxstationgroupService.editPbxStationGroup(formPbxStationGroup);
		status.setComplete();
		return "redirect:/stations/pbxstationgroups/index.html";
	}
	
	@RequestMapping(value = {"/delete"}, method = RequestMethod.DELETE)
	@Secured("ROLE_ADMIN")
	public String deletePbxStationGroup(int id, SessionStatus status) 
			throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting delete pbxstationgroup");
		pbxstationgroupService.deletePbxStationGroup(id);
		status.setComplete();
		return "redirect:/stations/pbxstationgroups/index.html";
	}
/*	@JsonView(View.MMTimePeriods.class)
	@RequestMapping(value = {"/ajax/serverside/addperiod"}, method = RequestMethod.GET)
	@Secured("ROLE_ADMIN")
	public @ResponseBody MMTimePeriods addTimePeriod(
					@RequestParam(value="ids") int [] ids
			) throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting add mm time period");
		List<TimePeriod> periods = timeperiodService.getTimePeriodByIds(ids);
		mmTimePeriods.setAvailableTimePeriods(filterTimePeriod(mmTimePeriods.getAvailableTimePeriods(), periods));
		mmTimePeriods.getSelectedTimePeriods().addAll(periods);
		return mmTimePeriods;
	}
		
	@JsonView(View.MMTimePeriods.class)	
	@RequestMapping(value = {"/ajax/serverside/delperiod"}, method = RequestMethod.GET)
	@Secured("ROLE_ADMIN")
	public @ResponseBody MMTimePeriods removeTimePeriod(
					@RequestParam(value="ids") int [] ids
			) throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting delete mm time period");
		List<TimePeriod> periods = timeperiodService.getTimePeriodByIds(ids);
		mmTimePeriods.setSelectedTimePeriods(filterTimePeriod(mmTimePeriods.getSelectedTimePeriods(), periods));
		mmTimePeriods.getAvailableTimePeriods().addAll(periods);
		return mmTimePeriods;
	}
	
	private List<TimePeriod> filterTimePeriod(List<TimePeriod> src, List<TimePeriod> dst){
		boolean found;
		List<TimePeriod> result = new ArrayList<TimePeriod>();
		for(int i = 0; i<src.size(); i++){
			found=false;
			for(int j = 0; j<dst.size(); j++){
				if(src.get(i).getId()==dst.get(j).getId()){
					found=true;
				}
			}
			if(!found) result.add(src.get(i));
		}
		return result;
	}*/
}
