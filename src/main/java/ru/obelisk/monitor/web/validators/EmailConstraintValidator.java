package ru.obelisk.monitor.web.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
 
public class EmailConstraintValidator implements ConstraintValidator<Email, String> {
 
    @Override
    public void initialize(Email email) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
 
    @Override
    public boolean isValid(String emailField, ConstraintValidatorContext cxt) {
        if(emailField == null) {
            return false;
        }else if(emailField.isEmpty()) {
            return true;
        }
        return emailField.matches("^[-a-z0-9!#$%&'*+/=?^_`{|}~]+(\\.[-a-z0-9!#$%&'*+/=?^_`{|}~]+)*@([a-z0-9]([-a-z0-9]{0,61}[a-z0-9])?\\.)*(aero|arpa|asia|biz|cat|com|coop|edu|gov|info|int|jobs|mil|mobi|museum|name|net|org|pro|tel|travel|[a-z][a-z])$");
    }
}