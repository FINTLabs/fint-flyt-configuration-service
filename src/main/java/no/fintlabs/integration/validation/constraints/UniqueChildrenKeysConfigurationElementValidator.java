package no.fintlabs.integration.validation.constraints;

import no.fintlabs.integration.model.ConfigurationElement;
import no.fintlabs.integration.model.FieldCollectionConfiguration;
import no.fintlabs.integration.model.FieldConfiguration;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UniqueChildrenKeysConfigurationElementValidator extends UniqueChildrenKeysValidator<ConfigurationElement> {

    protected List<String> findDuplicateKeys(ConfigurationElement value) {
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
