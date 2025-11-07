package no.fintlabs.validation.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Constraint(validatedBy = InstanceValueTypesAreCompatibleValidator.class)
public @interface InstanceValueTypesAreCompatible {


    String CONFIGURATION_VALUE_TYPE = "configurationValueType";
    String INCOMPATIBLE_INSTANCE_VALUES_KEY_AND_TYPE = "incompatibleInstanceValuesKeyAndType";

    String message() default "contains references to instance values that are incompatible with " +
            "configuration value type={" + CONFIGURATION_VALUE_TYPE + "}" +
            " : {" + INCOMPATIBLE_INSTANCE_VALUES_KEY_AND_TYPE + "}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
