package ru.obelisk.monitor.web.validators;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ru.obelisk.monitor.database.models.entity.TimePeriod;
import ru.obelisk.monitor.database.models.entity.enums.CalendarDays;
import ru.obelisk.monitor.database.models.entity.enums.CalendarMonthDays;
import ru.obelisk.monitor.database.models.entity.enums.CalendarMonths;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TimePeriodFieldMatchValidator implements ConstraintValidator<TimePeriodFieldMatch, Object>
{
	private static Logger logger = LogManager.getLogger(TimePeriodFieldMatchValidator.class);
	   
    @Override
    public void initialize(final TimePeriodFieldMatch constraintAnnotation){ }
    
    @Override
    public boolean isValid(final Object value, final ConstraintValidatorContext context)
    {
        try{
        	boolean flag = true;
        	if(value instanceof TimePeriod){
        		flag=((TimePeriod) value).getWeekdayStart()==CalendarDays.NOTSELECT || 
        				(((TimePeriod) value).getWeekdayStart()!=CalendarDays.NOTSELECT &&
        				((TimePeriod) value).getWeekdayStart().getNumber()<=((TimePeriod) value).getWeekdayStop().getNumber());
        		if(false == flag) {
        			context.disableDefaultConstraintViolation();
    				context.buildConstraintViolationWithTemplate("field.validation.error.timeperiod.weekdaystart").
    					addPropertyNode("weekdayStart").addConstraintViolation();
    				context.buildConstraintViolationWithTemplate("field.validation.error.timeperiod.weekdaystop").
    					addPropertyNode("weekdayStop").addConstraintViolation();
    				return flag;
        		}
        		
        		flag=((TimePeriod) value).getMonthStart()==CalendarMonths.NOTSELECT || 
        				(((TimePeriod) value).getMonthStart()!=CalendarMonths.NOTSELECT &&
        				((TimePeriod) value).getMonthStart().getNumber()<=((TimePeriod) value).getMonthStop().getNumber());
        		if(false == flag) {
        			context.disableDefaultConstraintViolation();
    				context.buildConstraintViolationWithTemplate("field.validation.error.timeperiod.monthstart").
    					addPropertyNode("monthStart").addConstraintViolation();
    				context.buildConstraintViolationWithTemplate("field.validation.error.timeperiod.monthstop").
    					addPropertyNode("monthStop").addConstraintViolation();
    				return flag;
        		}
        		
        		flag=((TimePeriod) value).getMonthDayStart()==CalendarMonthDays.NOTSELECT || 
        				(((TimePeriod) value).getMonthDayStart()!=CalendarMonthDays.NOTSELECT &&
        				((TimePeriod) value).getMonthDayStart().getNumber()<=((TimePeriod) value).getMonthDayStop().getNumber());
        		if(false == flag) {
        			context.disableDefaultConstraintViolation();
    				context.buildConstraintViolationWithTemplate("field.validation.error.timeperiod.monthdaystart").
    					addPropertyNode("monthDayStart").addConstraintViolation();
    				context.buildConstraintViolationWithTemplate("field.validation.error.timeperiod.monthdaystop").
    					addPropertyNode("monthDayStop").addConstraintViolation();
    				return flag;
        		}
        		return flag;
        	}
        }
        catch (final Exception ignore){
            logger.warn("Validation error: {}",ignore);
        }
        return true;
    }
}