package ru.obelisk.monitor.web.controllers;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.validation.ConstraintViolation;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import javax.validation.metadata.ConstraintDescriptor;
import javax.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.asteriskjava.manager.AuthenticationFailedException;
import org.asteriskjava.manager.TimeoutException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.access.annotation.Secured;
import ru.obelisk.monitor.database.models.entity.LdapAuthenticationParameters;
import ru.obelisk.monitor.database.models.entity.LdapAuthenticationServers;
import ru.obelisk.monitor.database.models.services.LdapAuthenticationParametersService;


@Controller
@RequestMapping("/ldap")
public class LdapController {
	
	@Autowired
    private LdapAuthenticationParametersService ldapAuthParamsService;
	
	@Autowired(required = true)
	private javax.validation.Validator validator;
	
	private static Logger logger = LogManager.getLogger(LdapController.class);
	
	@RequestMapping(value = {"/authentication/index.html","/authentication/"}, method = RequestMethod.GET)
	@Secured("ROLE_ADMIN")
	public String viewUpdateAuthenticationParametersPage(ModelMap model) throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting update ldap authentication page");
		LdapAuthenticationParameters ldapAuthParams =ldapAuthParamsService.getParameters();
		model.addAttribute("ldapAuthParams", ldapAuthParams);
		return "ldap/authentication/index";
	}
	
	@RequestMapping(value = {"/authentication/ajax/addserver"}, method = RequestMethod.GET)
	@Secured("ROLE_ADMIN")
	public String addAuthServerAjax(ModelMap model,
									@ModelAttribute("ldapAuthParams") LdapAuthenticationParameters formParameters,
									RedirectAttributes redirectAttributes
				) throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting to add redundant ldap server (ajax)");
		formParameters.getLdapServers().add(new LdapAuthenticationServers());
		return "ldap/authentication/form::ldapAuthServersContent";
	}
	
	@RequestMapping(value = {"/authentication/ajax/delserver"}, method = RequestMethod.GET)
	@Secured("ROLE_ADMIN")
	public String deleteAuthServerAjax(	final ModelMap model,
										@RequestParam("index") int index,
										@ModelAttribute("ldapAuthParams") final LdapAuthenticationParameters formParameters
				) throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting to delete redundant ldap server (ajax)");
		formParameters.getLdapServers().remove(index);
		return "ldap/authentication/form::ldapAuthServersContent";
	}
	
	
	@RequestMapping(value = {"/authentication/index.html"}, method = RequestMethod.PUT)
	@Secured("ROLE_ADMIN")
	public String saveUpdateAuthenticationParametersPage(final ModelMap model, 
									@Valid @ModelAttribute("ldapAuthParams") final LdapAuthenticationParameters formParameters, 
									final BindingResult bindingResult,
									SessionStatus status,
									final RedirectAttributes redirectAttributes) 
			throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception	{
		logger.info("Requesting save update ldap authentication method");
		
		if (isNotValid(formParameters, bindingResult, 
					LdapAuthenticationParameters.LdapAuthParamsStepOne.class, 
					LdapAuthenticationParameters.LdapAuthParamsStepTwo.class)) {
			return "ldap/authentication/index";
		}
						
		ldapAuthParamsService.editParameters(formParameters);
		redirectAttributes.addFlashAttribute("operationResult", "Successfully saved");
		status.setComplete();
		return "redirect:/ldap/authentication/index.html";
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
