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
import ru.obelisk.monitor.database.models.entity.DevicePool;
import ru.obelisk.monitor.database.models.services.DevicePoolService;
import ru.obelisk.monitor.database.models.services.LocationService;
import ru.obelisk.monitor.database.models.services.TimezoneService;
import ru.obelisk.monitor.database.models.views.View;
import ru.obelisk.monitor.web.ui.datatables.DataSet;
import ru.obelisk.monitor.web.ui.datatables.DatatablesCriterias;
import ru.obelisk.monitor.web.ui.datatables.DatatablesResponse;
import ru.obelisk.monitor.web.ui.select2.Select2Result;

@Controller
@RequestMapping("/devicepools")
public class DevicePoolController {
	
	private static Logger logger = LogManager.getLogger(DevicePoolController.class);
	
	@Autowired
    private DevicePoolService devicePoolService;
	
	@Autowired
    private TimezoneService timezoneService;
	
	@Autowired
    private LocationService locationService;
	
	@RequestMapping(value = {"/search/devicepools"}, method = RequestMethod.GET)
	@Secured("ROLE_ADMIN")
	public @ResponseBody List<Select2Result> searchDevicePool(@RequestParam String searchString) 
			throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting search device pool with term: {}",searchString);
		return devicePoolService.findDevicePoolByTerm(searchString);
	}
	
	@RequestMapping(value = {"/", "/index.html"}, method = RequestMethod.GET)
	@Secured("ROLE_ADMIN")
	public String indexPage(Model model) 
			throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting Device pool page");
		DevicePool devicePool = new DevicePool();
		model.addAttribute("devicePool", devicePool);
		model.addAttribute("devicePoolAll", devicePoolService.getAllDevicePools());
		return "devicepools/index";
	}
	
	@JsonView(View.DevicePool.class)
	@RequestMapping(value = {"/ajax/serverside/devicepooldata.json"}, method = RequestMethod.GET)
	@Secured("ROLE_ADMIN")
	public @ResponseBody DatatablesResponse<DevicePool> devicePoolsDatatables(
			@DatatableCriterias DatatablesCriterias criterias, 
			Model model) 
					throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting device pools data for table on index page");
		List<DevicePool> devicePools = devicePoolService.findDevicePoolWithDatatablesCriterias(criterias);
		Long count = devicePoolService.getTotalCount();
		Long countFiltered = devicePoolService.getFilteredCount(criterias);
		return DatatablesResponse.build(new DataSet<DevicePool>(devicePools,count,countFiltered), criterias);
	}
	@JsonView(View.DevicePool.class)	
	@RequestMapping(value = {"/ajax/clientside/devicepooldata.json"}, method = RequestMethod.GET)
	@Secured("ROLE_ADMIN")
	public @ResponseBody DatatablesResponse<DevicePool> devicePoolsDataClientSide(Model model) 
			throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting device pools data for table on index page");
		List<DevicePool> devicePools = devicePoolService.getAllDevicePools();
		return DatatablesResponse.clientSideBuild(devicePools);
	}
	
	@RequestMapping(value = {"/create"}, method = RequestMethod.GET)
	@Secured("ROLE_ADMIN")
	public String viewCreateDevicePoolPage(Model model) 
			throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting create device pool page");
		DevicePool devicePool = new DevicePool();
		model.addAttribute("devicePool", devicePool);
		model.addAttribute("timezoneAll", timezoneService.getAllTimezones());
		return "devicepools/create";
	}
		
	@RequestMapping(value = {"/create"}, method = RequestMethod.POST)
	@Secured("ROLE_ADMIN")
	public String saveCreateDevicePoolPage(	final ModelMap model, 
									@Valid @ModelAttribute("devicePool") final DevicePool devicePool, 
									final BindingResult bindingResult) 
					throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting add device pool method");
		if(bindingResult.hasErrors()){
			model.addAttribute("timezoneAll", timezoneService.getAllTimezones());
			return "devicepools/create";
		}
		devicePoolService.addDevicePool(devicePool);
		model.clear();
        return "redirect:/devicepools/index.html";
	}
	
	@RequestMapping(value = {"/update/{id}"}, method = RequestMethod.GET)
	@Secured("ROLE_ADMIN")
	public String viewUpdateDevicePoolPage(	ModelMap model, 
											@PathVariable(value = "id") int id) 
			throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting update device pools page");
		DevicePool devicePool = devicePoolService.getDevicePoolById(id);
		model.addAttribute("devicePool", devicePool);
		model.addAttribute("timezoneAll", timezoneService.getAllTimezones());
		return "devicepools/update";
	}
	
	@RequestMapping(value = {"/update"}, method = RequestMethod.PUT)
	@Secured("ROLE_ADMIN")
	public String saveUpdateDevicePoolPage(final ModelMap model, 
									@Valid @ModelAttribute("devicePool") final DevicePool formDevicePool, 
									final BindingResult bindingResult,
									SessionStatus status) 
			throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting save update device pools method");
		if(bindingResult.hasErrors()){
			model.addAttribute("timezoneAll", timezoneService.getAllTimezones());
			
			return "devicepools/update";
		}
		devicePoolService.editDevicePool(formDevicePool);
		status.setComplete();
		return "redirect:/devicepools/index.html";
	}
	
	@RequestMapping(value = {"/delete"}, method = RequestMethod.DELETE)
	@Secured("ROLE_ADMIN")
	public String deleteDevicePool(int id, SessionStatus status) 
			throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting delete device pool");
		devicePoolService.deleteDevicePool(id);
		status.setComplete();
		return "redirect:/devicepools/index.html";
	}
}
