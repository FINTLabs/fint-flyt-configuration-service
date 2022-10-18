package no.fintlabs.integration.validation.constraints;

import no.fintlabs.integration.model.configuration.dtos.CollectionFieldConfigurationDto;
import no.fintlabs.integration.model.configuration.dtos.ConfigurationElementDto;
import no.fintlabs.integration.model.configuration.dtos.FieldConfigurationDto;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UniqueChildrenKeysConfigurationElementValidator extends UniqueChildrenKeysValidator<ConfigurationElementDto> {

    protected List<String> findDuplicateKeys(ConfigurationElementDto value) {
        Set<String> checkedKeys = new HashSet<>();
        return Stream.of(
                        value.getFieldConfigurations().stream().map(FieldConfigurationDto::getKey),
                        value.getCollectionFieldConfigurations().stream().map(CollectionFieldConfigurationDto::getKey),
                        value.getElements().stream().map(ConfigurationElementDto::getKey)
                ).flatMap(Function.identity())
                .filter(Objects::nonNull)
                .filter(n -> !checkedKeys.add(n))
                .collect(Collectors.toList());
    }

}
