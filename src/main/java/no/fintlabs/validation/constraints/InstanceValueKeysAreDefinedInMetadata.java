package no.fintlabs.validation.constraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Constraint(validatedBy = InstanceValueKeysAreDefinedInMetadataValidator.class)
public @interface InstanceValueKeysAreDefinedInMetadata {

    String KEYS_MISSING_IN_METADATA = "keysMissingInMetadata";

    String message() default "contains references to instance values that are not defined in the metadata: {" + KEYS_MISSING_IN_METADATA + "}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
