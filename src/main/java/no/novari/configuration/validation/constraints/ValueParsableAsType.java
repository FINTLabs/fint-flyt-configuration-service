package no.novari.configuration.validation.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Constraint(validatedBy = ValueParsableAsTypeValidator.class)
public @interface ValueParsableAsType {

    String VALUE_TYPE = "valueType";

    String message() default "contains value that is not parsable as type={" + VALUE_TYPE + "}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
