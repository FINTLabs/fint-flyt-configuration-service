package no.fintlabs.integration.validation.parsability;

import no.fintlabs.integration.model.FieldCollectionConfiguration;

import java.util.Collection;

public interface FieldCollectionParsabilityValidator {

    default boolean isValid(FieldCollectionConfiguration fieldCollectionConfiguration) {
        return fieldCollectionConfiguration.getType() != getFieldCollectionType() || isValid(fieldCollectionConfiguration.getValues());
    }

    FieldCollectionConfiguration.Type getFieldCollectionType();

    boolean isValid(Collection<String> values);

}
