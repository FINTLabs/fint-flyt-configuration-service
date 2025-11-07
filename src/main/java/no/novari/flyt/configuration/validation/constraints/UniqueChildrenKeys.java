package no.novari.flyt.configuration.validation.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Constraint(validatedBy = UniqueChildrenKeysValidator.class
)
public @interface UniqueChildrenKeys {

    String DUPLICATE_CHILDREN_KEYS_REF = "duplicateChildrenKeys";

    String message() default "contains duplicate children keys: {" + DUPLICATE_CHILDREN_KEYS_REF + "}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
