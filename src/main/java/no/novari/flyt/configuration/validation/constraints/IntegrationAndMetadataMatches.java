package no.novari.flyt.configuration.validation.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Constraint(validatedBy = IntegrationAndMetadataMatchesValidator.class)
public @interface IntegrationAndMetadataMatches {

    String message() default "Integration and metadata do not have matching source application ids and/or source application integration ids";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
