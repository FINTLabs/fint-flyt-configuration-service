package no.novari.flyt.configuration.validation.constraints;

import no.novari.flyt.configuration.model.configuration.dtos.ObjectMappingDto;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.hibernate.validator.internal.util.CollectionHelper;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static no.novari.flyt.configuration.validation.constraints.UniqueChildrenKeys.DUPLICATE_CHILDREN_KEYS_REF;

public class UniqueChildrenKeysValidator implements HibernateConstraintValidator<UniqueChildrenKeys, ObjectMappingDto> {

    @Override
    public boolean isValid(ObjectMappingDto value, HibernateConstraintValidatorContext hibernateConstraintValidatorContext) {
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


    protected List<String> findDuplicateKeys(ObjectMappingDto value) {
        Set<String> checkedKeys = new HashSet<>();
        return Stream.of(
                        value.getValueMappingPerKey(),
                        value.getValueCollectionMappingPerKey(),
                        value.getObjectMappingPerKey(),
                        value.getObjectCollectionMappingPerKey()
                )
                .map(Map::keySet)
                .flatMap(Collection::stream)
                .filter(Objects::nonNull)
                .filter(n -> !checkedKeys.add(n))
                .collect(Collectors.toList());
    }

}
