package no.fintlabs.integration.validation.constraints;

import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.hibernate.validator.internal.util.CollectionHelper;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.stream.Collectors;

import static no.fintlabs.integration.validation.constraints.UniqueChildrenKeys.DUPLICATE_CHILDREN_KEYS_REF;

public abstract class UniqueChildrenKeysValidator<T> implements ConstraintValidator<UniqueChildrenKeys, T> {

    @Override
    public boolean isValid(T value, ConstraintValidatorContext constraintValidatorContext) {
        List<String> duplicateKeys = findDuplicateKeys(value);
        if (duplicateKeys.isEmpty()) {
            return true;
        }
        if (constraintValidatorContext instanceof HibernateConstraintValidatorContext) {
            constraintValidatorContext.unwrap(HibernateConstraintValidatorContext.class)
                    .addMessageParameter(
                            DUPLICATE_CHILDREN_KEYS_REF,
                            duplicateKeys
                                    .stream()
                                    .map(key -> "'" + key + "'")
                                    .collect(Collectors.joining(", ", "[", "]"))
                    )
                    .withDynamicPayload(CollectionHelper.toImmutableList(duplicateKeys));
        }
        return false;
    }

    abstract List<String> findDuplicateKeys(T value);

}
