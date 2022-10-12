package no.fintlabs.integration.validation.parsability;

import no.fintlabs.integration.model.CollectionFieldConfiguration;

import java.util.Collection;

public interface CollectionFieldParsabilityValidator {

    default boolean isValid(CollectionFieldConfiguration collectionFieldConfiguration) {
        return collectionFieldConfiguration.getType() != getCollectionFieldType() || isValid(collectionFieldConfiguration.getValues());
    }

    CollectionFieldConfiguration.Type getCollectionFieldType();

    boolean isValid(Collection<String> values);

}
