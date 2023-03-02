package no.fintlabs.validation.constraints;

import javax.validation.Constraint;
import javax.validation.Payload;
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
