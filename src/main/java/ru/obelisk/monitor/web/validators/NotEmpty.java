package ru.obelisk.monitor.web.validators;

import javax.validation.Constraint;
import javax.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NotEmptyConstraintValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface NotEmpty {
	String message() default "field.validation.error.notempty";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
