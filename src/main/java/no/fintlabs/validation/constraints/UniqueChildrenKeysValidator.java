package no.fintlabs.validation.constraints;

import no.fintlabs.model.configuration.dtos.ElementMappingDto;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.hibernate.validator.internal.util.CollectionHelper;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static no.fintlabs.validation.constraints.UniqueChildrenKeys.DUPLICATE_CHILDREN_KEYS_REF;

public class UniqueChildrenKeysValidator implements HibernateConstraintValidator<UniqueChildrenKeys, ElementMappingDto> {

    @Override
    public boolean isValid(ElementMappingDto value, HibernateConstraintValidatorContext hibernateConstraintValidatorContext) {
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


    protected List<String> findDuplicateKeys(ElementMappingDto value) {
        Set<String> checkedKeys = new HashSet<>();
        return Stream.of(
                        value.getValueMappingPerKey(),
                        value.getElementMappingPerKey(),
                        value.getElementCollectionMappingPerKey()
                )
                .map(Map::keySet)
                .flatMap(Collection::stream)
                .filter(Objects::nonNull)
                .filter(n -> !checkedKeys.add(n))
                .collect(Collectors.toList());
    }

}
