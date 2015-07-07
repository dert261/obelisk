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
import ru.obelisk.monitor.database.models.entity.Location;
import ru.obelisk.monitor.database.models.services.DevicePoolService;
import ru.obelisk.monitor.database.models.services.LocationService;
import ru.obelisk.monitor.database.models.services.PbxStationService;
import ru.obelisk.monitor.database.models.views.View;
import ru.obelisk.monitor.web.ui.datatables.DataSet;
import ru.obelisk.monitor.web.ui.datatables.DatatablesCriterias;
import ru.obelisk.monitor.web.ui.datatables.DatatablesResponse;
import ru.obelisk.monitor.web.ui.select2.Select2Result;

@Controller
@RequestMapping("/locations")
public class LocationController {
	
	private static Logger logger = LogManager.getLogger(LocationController.class);
	
	@Autowired
    private LocationService locationService;
	
	@Autowired
    private PbxStationService pbxStationService;
	
	@Autowired
    private DevicePoolService devicePoolService;
	
	@RequestMapping(value = {"/search/locations"}, method = RequestMethod.GET)
	@Secured("ROLE_ADMIN")
	public @ResponseBody List<Select2Result> searchLocation(@RequestParam String searchString) throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting search location with term: {}",searchString);
		return locationService.findLocationByTerm(searchString);
	}
	
	@RequestMapping(value = {"/", "/index.html"}, method = RequestMethod.GET)
	@Secured("ROLE_ADMIN")
	public String indexPage(Model model) throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting Locations page");
		Location location = new Location();
		model.addAttribute("location", location);
		model.addAttribute("locationAll", locationService.getAllLocations());
		return "locations/index";
	}
	
	@JsonView(View.Location.class)
	@RequestMapping(value = {"/ajax/serverside/locationdata.json"}, method = RequestMethod.GET)
	@Secured("ROLE_ADMIN")
	public @ResponseBody DatatablesResponse<Location> locationsDatatables(
			@DatatableCriterias DatatablesCriterias criterias, 
			Model model) 
					throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting locations data for table on index page");
		List<Location> locations = locationService.findLocationWithDatatablesCriterias(criterias);
		Long count = locationService.getTotalCount();
		Long countFiltered = locationService.getFilteredCount(criterias);
		return DatatablesResponse.build(new DataSet<Location>(locations,count,countFiltered), criterias);
	}
	@JsonView(View.Location.class)	
	@RequestMapping(value = {"/ajax/clientside/locationdata.json"}, method = RequestMethod.GET)
	@Secured("ROLE_ADMIN")
	public @ResponseBody DatatablesResponse<Location> locationsDataClientSide(Model model) throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting locations data for table on index page");
		List<Location> locations = locationService.getAllLocations();
		return DatatablesResponse.clientSideBuild(locations);
	}
	
	@RequestMapping(value = {"/create"}, method = RequestMethod.GET)
	@Secured("ROLE_ADMIN")
	public String viewCreateLocationPage(Model model) throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting create location page");
		Location location = new Location();
		model.addAttribute("location", location);
		model.addAttribute("devicePoolAll", devicePoolService.getAllDevicePools());
		model.addAttribute("pbxStationAll", pbxStationService.getAllPbxStations());
		return "locations/create";
	}
		
	@RequestMapping(value = {"/create"}, method = RequestMethod.POST)
	@Secured("ROLE_ADMIN")
	public String saveCreateLocationPage(	final ModelMap model, 
									@Valid @ModelAttribute("location") final Location location, 
									final BindingResult bindingResult ) 
					throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting add location method");
		if(bindingResult.hasErrors()){
			model.addAttribute("devicePoolAll", devicePoolService.getAllDevicePools());
			model.addAttribute("pbxStationAll", pbxStationService.getAllPbxStations());
			return "locations/create";
		}
		locationService.addLocation(location);
		model.clear();
        return "redirect:/locations/index.html";
	}
	
	@RequestMapping(value = {"/update/{id}"}, method = RequestMethod.GET)
	@Secured("ROLE_ADMIN")
	public String viewUpdateLocationPage(	ModelMap model, 
											@PathVariable(value = "id") int id) 
			throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting update location page");
		Location location = locationService.getLocationById(id);
		model.addAttribute("location", location);
		model.addAttribute("devicePoolAll", devicePoolService.getAllDevicePools());
		model.addAttribute("pbxStationAll", pbxStationService.getAllPbxStations());
		return "locations/update";
	}
	
	@RequestMapping(value = {"/update"}, method = RequestMethod.PUT)
	@Secured("ROLE_ADMIN")
	public String saveUpdateLocationPage(final ModelMap model, 
									@Valid @ModelAttribute("location") final Location formLocation, 
									final BindingResult bindingResult,
									SessionStatus status) 
			throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting save update location method");
		if(bindingResult.hasErrors()){
			model.addAttribute("devicePoolAll", devicePoolService.getAllDevicePools());
			model.addAttribute("pbxStationAll", pbxStationService.getAllPbxStations());
			return "locations/update";
		}
		locationService.editLocation(formLocation);
		status.setComplete();
		return "redirect:/locations/index.html";
	}
	
	@RequestMapping(value = {"/delete"}, method = RequestMethod.DELETE)
	@Secured("ROLE_ADMIN")
	public String deleteLocation(int id, SessionStatus status) 
			throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting delete location");
		locationService.deleteLocation(id);
		status.setComplete();
		return "redirect:/locations/index.html";
	}
}
