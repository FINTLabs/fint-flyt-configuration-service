package no.fintlabs.integration.validation.constraints;

import no.fintlabs.integration.model.configuration.ConfigurationElement;

import java.util.*;
import java.util.stream.Collectors;

public class UniqueChildrenKeysElementsFieldValidator extends UniqueChildrenKeysValidator<Collection<ConfigurationElement>> {

    protected List<String> findDuplicateKeys(Collection<ConfigurationElement> value) {
        Set<String> checkedKeys = new HashSet<>();
        return value
                .stream()
                .map(ConfigurationElement::getKey)
                .filter(Objects::nonNull)
                .filter(n -> !checkedKeys.add(n))
                .collect(Collectors.toList());
    }

}
