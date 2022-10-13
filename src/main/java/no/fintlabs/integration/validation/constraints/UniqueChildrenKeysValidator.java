package no.fintlabs.integration.validation.constraints;

import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.hibernate.validator.internal.util.CollectionHelper;

import java.util.List;
import java.util.stream.Collectors;

import static no.fintlabs.integration.validation.constraints.UniqueChildrenKeys.DUPLICATE_CHILDREN_KEYS_REF;

public abstract class UniqueChildrenKeysValidator<T> implements HibernateConstraintValidator<UniqueChildrenKeys, T> {

    @Override
    public boolean isValid(T value, HibernateConstraintValidatorContext hibernateConstraintValidatorContext) {
        List<String> duplicateKeys = findDuplicateKeys(value);
        if (duplicateKeys.isEmpty()) {
            return true;
        }
        hibernateConstraintValidatorContext.addMessageParameter(
                        DUPLICATE_CHILDREN_KEYS_REF,
                        duplicateKeys
                                .stream()
                                .map(key -> "'" + key + "'")
                                .collect(Collectors.joining(", ", "[", "]"))
                )
                .withDynamicPayload(CollectionHelper.toImmutableList(duplicateKeys));
        return false;
    }


    protected abstract List<String> findDuplicateKeys(T value);

}
