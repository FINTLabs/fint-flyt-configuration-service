package no.fintlabs.integration.validation.constraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Constraint(validatedBy = InstanceFieldReferenceKeysExistInMetadataValidator.class)
public @interface InstanceFieldReferenceKeysExistInMetadata {

    String KEYS_MISSING_IN_METADATA = "keysMissingInMetadata";

    String message() default "contains instance field references that are not defined in the metadata: {" + KEYS_MISSING_IN_METADATA + "}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
