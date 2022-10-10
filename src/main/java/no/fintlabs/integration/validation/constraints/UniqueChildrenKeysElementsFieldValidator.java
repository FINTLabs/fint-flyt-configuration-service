package no.fintlabs.integration.validation.constraints;

import no.fintlabs.integration.model.ConfigurationElement;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.hibernate.validator.internal.util.CollectionHelper;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.*;
import java.util.stream.Collectors;

import static no.fintlabs.integration.validation.constraints.UniqueChildrenKeys.DUPLICATE_CHILDREN_KEYS_REF;

public class UniqueChildrenKeysElementsFieldValidator implements ConstraintValidator<UniqueChildrenKeys, Collection<ConfigurationElement>> {

    @Override
    public boolean isValid(Collection<ConfigurationElement> value, ConstraintValidatorContext constraintValidatorContext) {
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

    private List<String> findDuplicateKeys(Collection<ConfigurationElement> value) {
        Set<String> checkedKeys = new HashSet<>();
        return value
                .stream()
                .map(ConfigurationElement::getKey)
                .filter(Objects::nonNull)
                .filter(n -> !checkedKeys.add(n))
                .collect(Collectors.toList());
    }

}
