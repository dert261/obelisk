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
import ru.obelisk.monitor.database.models.entity.PbxStation;
import ru.obelisk.monitor.database.models.services.PbxStationService;
import ru.obelisk.monitor.database.models.views.View;
import ru.obelisk.monitor.web.ui.datatables.DataSet;
import ru.obelisk.monitor.web.ui.datatables.DatatablesCriterias;
import ru.obelisk.monitor.web.ui.datatables.DatatablesResponse;
import ru.obelisk.monitor.web.ui.select2.Select2Result;

@Controller
@RequestMapping("/stations/pbxstations")
public class PbxStationController {
	
	private static Logger logger = LogManager.getLogger(PbxStationController.class);
	
	@Autowired
    private PbxStationService pbxStationService;
	
	
	@RequestMapping(value = {"/search/pbxstations"}, method = RequestMethod.GET)
	@Secured("ROLE_ADMIN")
	public @ResponseBody List<Select2Result> searchPbxStation(@RequestParam String searchString) throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting search pbxStation with term: {}",searchString);
		return pbxStationService.findPbxStationByTerm(searchString);
	}
	
	@JsonView(View.PbxStation.class)		
	@RequestMapping(value = {"/", "/index.html"}, method = RequestMethod.GET)
	@Secured("ROLE_ADMIN")
	public String indexPage(Model model) throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting PbxStations page");
		PbxStation pbxStation = new PbxStation();
		model.addAttribute("pbxStation", pbxStation);
		model.addAttribute("pbxStationAll", pbxStationService.getAllPbxStations());
		return "stations/pbxstations/index";
	}
	
	@JsonView(View.PbxStation.class)	
	@RequestMapping(value = {"/ajax/serverside/pbxstationdata.json"}, method = RequestMethod.GET)
	@Secured("ROLE_ADMIN")
	public @ResponseBody DatatablesResponse<PbxStation> pbxStationsDatatables(
			@DatatableCriterias DatatablesCriterias criterias, 
			Model model) 
					throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting pbx stations data for table on index page");
			
		List<PbxStation> pbxStations = pbxStationService.findPbxStationWithDatatablesCriterias(criterias);
		Long count = pbxStationService.getTotalCount();
		Long countFiltered = pbxStationService.getFilteredCount(criterias);
	    return DatatablesResponse.build(new DataSet<PbxStation>(pbxStations,count,countFiltered), criterias);
	}
	
	@JsonView(View.PbxStation.class)	
	@RequestMapping(value = {"/ajax/clientside/pbxstationdata.json"}, method = RequestMethod.GET)
	@Secured("ROLE_ADMIN")
	public @ResponseBody DatatablesResponse<PbxStation> pbxStationsDataClientSide(Model model) throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting pbx stations data for table on index page");
		List<PbxStation> pbxStations = pbxStationService.getAllPbxStations();
		return DatatablesResponse.clientSideBuild(pbxStations);
	}
	
	@JsonView(View.PbxStation.class)	
	@RequestMapping(value = {"/create"}, method = RequestMethod.GET)
	@Secured("ROLE_ADMIN")
	public String viewCreatePbxStationPage(Model model) throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting create pbx station page");
		PbxStation pbxStation = new PbxStation();
		model.addAttribute("pbxStation", pbxStation);
        return "stations/pbxstations/create";
	}
	
	@JsonView(View.PbxStation.class)	
	@RequestMapping(value = {"/create"}, method = RequestMethod.POST)
	@Secured("ROLE_ADMIN")
	public String saveCreatePbxStationPage(	final ModelMap model, 
									@Valid @ModelAttribute("pbxStation") final PbxStation pbxStation, 
									final BindingResult bindingResult ) 
					throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting add pbx station method");
		if(bindingResult.hasErrors()){
			return "stations/pbxstations/create";
		}
		pbxStationService.addPbxStation(pbxStation);
		model.clear();
        return "redirect:/stations/pbxstations/index.html";
	}
	
	@JsonView(View.PbxStation.class)
	@RequestMapping(value = {"/update/{id}"}, method = RequestMethod.GET)
	@Secured("ROLE_ADMIN")
	public String viewUpdatePbxStationPage(	ModelMap model, 
											@PathVariable(value = "id") int id) 
			throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting update pbx station page");
		PbxStation pbxStation = pbxStationService.getPbxStationById(id);
		model.addAttribute("pbxStation", pbxStation);
		return "stations/pbxstations/update";
	}
	
	@JsonView(View.PbxStation.class)	
	@RequestMapping(value = {"/update"}, method = RequestMethod.PUT)
	@Secured("ROLE_ADMIN")
	public String saveUpdatePbxStationPage(final ModelMap model, 
									@Valid @ModelAttribute("pbxStation") final PbxStation formPbxStation, 
									final BindingResult bindingResult,
									SessionStatus status) 
			throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting save update pbx station method");
		if(bindingResult.hasErrors()){
			return "stations/pbxstations/update";
		}
		pbxStationService.editPbxStation(formPbxStation);
		status.setComplete();
		return "redirect:/stations/pbxstations/index.html";
	}
	
	@JsonView(View.PbxStation.class)	
	@RequestMapping(value = {"/delete"}, method = RequestMethod.DELETE)
	@Secured("ROLE_ADMIN")
	public String deletePbxStation(int id, SessionStatus status) 
			throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting delete pbx station");
		pbxStationService.deletePbxStation(id);
		status.setComplete();
		return "redirect:/stations/pbxstations/index.html";
	}
	
	@JsonView(View.PbxStation.class)	
	@RequestMapping(value = {"/updateconfig"}, method = RequestMethod.POST)
	@Secured("ROLE_ADMIN")
	public String updateConfigPbxStation(int id, SessionStatus status) 
			throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting update config pbx station");
		pbxStationService.updateConfig(id);
		status.setComplete();
		return "redirect:/stations/pbxstations/index.html";
	}
	
	@JsonView(View.PbxStation.class)	
	@RequestMapping(value = {"/reinitbase"}, method = RequestMethod.POST)
	@Secured("ROLE_ADMIN")
	public String reinitBasePbxStation(int id, SessionStatus status) 
			throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting reinit db for pbx station");
		pbxStationService.reinitBase(id);
		status.setComplete();
		return "redirect:/stations/pbxstations/index.html";
	}
}
