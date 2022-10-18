package no.fintlabs.validation.parsability;

import no.fintlabs.model.configuration.dtos.CollectionFieldConfigurationDto;
import no.fintlabs.model.configuration.entities.CollectionFieldConfiguration;

import java.util.Collection;

public interface CollectionFieldParsabilityValidator {

    default boolean isValid(CollectionFieldConfigurationDto collectionFieldConfigurationDto) {
        return collectionFieldConfigurationDto.getType() != getCollectionFieldType() || isValid(collectionFieldConfigurationDto.getValues());
    }

    CollectionFieldConfiguration.Type getCollectionFieldType();

    boolean isValid(Collection<String> values);

}
