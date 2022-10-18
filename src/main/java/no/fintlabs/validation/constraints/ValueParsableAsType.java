package no.fintlabs.validation.constraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Constraint(validatedBy = {
        ValueParsableAsTypeFieldConfigurationValidator.class,
        ValueParsableAsTypeCollectionFieldConfigurationValidator.class,
})
public @interface ValueParsableAsType {

    String FIELD_VALUE_TYPE_REF = "fieldValueType";

    String message() default "contains value that is not parsable as type={" + FIELD_VALUE_TYPE_REF + "}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
