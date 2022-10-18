package no.fintlabs.validation.constraints;

import no.fintlabs.model.configuration.dtos.ConfigurationElementDto;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UniqueChildrenKeysElementsFieldValidator extends UniqueChildrenKeysValidator<Collection<ConfigurationElementDto>> {

    protected List<String> findDuplicateKeys(Collection<ConfigurationElementDto> value) {
        Set<String> checkedKeys = new HashSet<>();
        return value == null
                ? Collections.emptyList()
                : value
                .stream()
                .map(ConfigurationElementDto::getKey)
                .filter(Objects::nonNull)
                .filter(n -> !checkedKeys.add(n))
                .collect(Collectors.toList());
    }

}
