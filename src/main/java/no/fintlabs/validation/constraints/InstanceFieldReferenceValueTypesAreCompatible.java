package no.fintlabs.validation.constraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Constraint(validatedBy = InstanceFieldReferenceValueTypesAreCompatibleValidator.class)
public @interface InstanceFieldReferenceValueTypesAreCompatible {


    String CONFIGURATION_FIELD_TYPE = "configurationFieldType";
    String INCOMPATIBLE_INSTANCE_FIELDS_KEY_AND_TYPE = "incompatibleInstanceFieldsKeyAndType";

    String message() default "contains instance field references to fields that are incompatible with " +
            "configuration field type={" + CONFIGURATION_FIELD_TYPE + "}" +
            " : {" + INCOMPATIBLE_INSTANCE_FIELDS_KEY_AND_TYPE + "}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
