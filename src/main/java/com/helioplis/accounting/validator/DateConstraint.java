package com.helioplis.accounting.validator;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Documented
@Constraint(validatedBy = DateConstraint.DateValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface DateConstraint {

    String message() default "Invalid date format, format is yyyy-MM-dd HH:mm:ss";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    public class DateValidator implements ConstraintValidator<DateConstraint, String > {
        private static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            if (value == null) {
                return true;
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
            try
            {
                LocalDateTime d = LocalDateTime.parse(value, formatter);
                return true;
            }
            catch (DateTimeParseException e)
            {
                return false;
            }
        }

        @Override
        public void initialize(DateConstraint constraintAnnotation) {
            ConstraintValidator.super.initialize(constraintAnnotation);
        }
    }

}