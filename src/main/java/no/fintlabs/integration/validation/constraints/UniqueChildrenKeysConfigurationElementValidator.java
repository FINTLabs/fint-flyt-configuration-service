package no.fintlabs.integration.validation.constraints;

import no.fintlabs.integration.model.ConfigurationElement;
import no.fintlabs.integration.model.FieldCollectionConfiguration;
import no.fintlabs.integration.model.FieldConfiguration;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.hibernate.validator.internal.util.CollectionHelper;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static no.fintlabs.integration.validation.constraints.UniqueChildrenKeys.DUPLICATE_CHILDREN_KEYS_REF;

public class UniqueChildrenKeysConfigurationElementValidator implements ConstraintValidator<UniqueChildrenKeys, ConfigurationElement> {

    @Override
    public boolean isValid(ConfigurationElement value, ConstraintValidatorContext constraintValidatorContext) {
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

    private List<String> findDuplicateKeys(ConfigurationElement value) {
        Set<String> checkedKeys = new HashSet<>();
        return Stream.of(
                        value.getFieldConfigurations().stream().map(FieldConfiguration::getKey),
                        value.getFieldCollectionConfigurations().stream().map(FieldCollectionConfiguration::getKey),
                        value.getElements().stream().map(ConfigurationElement::getKey)
                ).flatMap(Function.identity())
                .filter(Objects::nonNull)
                .filter(n -> !checkedKeys.add(n))
                .collect(Collectors.toList());
    }

}
