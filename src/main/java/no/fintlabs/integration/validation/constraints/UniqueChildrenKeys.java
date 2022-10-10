package no.fintlabs.integration.validation.constraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Constraint(validatedBy = {
        UniqueChildrenKeysElementsFieldValidator.class,
        UniqueChildrenKeysConfigurationElementValidator.class}
)
public @interface UniqueChildrenKeys {

    String DUPLICATE_CHILDREN_KEYS_REF = "duplicateChildrenKeys";

    String message() default "contains duplicate children keys: {" + DUPLICATE_CHILDREN_KEYS_REF + "}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
