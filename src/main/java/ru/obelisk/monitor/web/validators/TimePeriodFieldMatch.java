package ru.obelisk.monitor.web.validators;

import javax.validation.Constraint;
import javax.validation.Payload;

import java.lang.annotation.Documented;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = TimePeriodFieldMatchValidator.class)
@Target({TYPE, ANNOTATION_TYPE})
@Retention(RUNTIME)
public @interface TimePeriodFieldMatch
{
    String message() default "{field.validation.error}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
   
}