package no.novari.flyt.configuration.validation;

import jakarta.validation.Payload;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;
import no.novari.flyt.configuration.model.integration.Integration;
import no.novari.flyt.configuration.model.metadata.InstanceValueMetadata;
import no.novari.flyt.configuration.model.metadata.IntegrationMetadata;

import java.util.Map;

@Getter
@EqualsAndHashCode
@Jacksonized
@Builder
public class ConfigurationValidationContext implements Payload {
    private final Integration integration;
    private final IntegrationMetadata metadata;
    private final Map<String, InstanceValueMetadata.Type> instanceValueTypePerKey;
}
