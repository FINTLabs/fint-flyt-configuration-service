package no.novari.flyt.configuration.validation.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Constraint(validatedBy = AtLeastOneChildValidator.class)
public @interface AtLeastOneChild {

    String message() default "has no children";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
