package no.fintlabs.integration.validation.parsability;

import no.fintlabs.integration.model.FieldCollectionConfiguration;

import java.util.Collection;

public interface FieldCollectionParsabilityValidator {

    default boolean validate(FieldCollectionConfiguration fieldCollectionConfiguration) {
        return fieldCollectionConfiguration.getType() != getFieldCollectionType() || validate(fieldCollectionConfiguration.getValues());
    }

    FieldCollectionConfiguration.Type getFieldCollectionType();

    boolean validate(Collection<String> values);

}
