package no.fintlabs.validation;

import lombok.Builder;
import lombok.Data;
import no.fintlabs.model.integration.Integration;
import no.fintlabs.model.metadata.InstanceElementMetadata;
import no.fintlabs.model.metadata.IntegrationMetadata;

import javax.validation.Payload;
import java.util.Map;

@Data
@Builder
public class ConfigurationValidationContext implements Payload {
    private final Integration integration;
    private final IntegrationMetadata metadata;
    private final Map<String, InstanceElementMetadata.Type> metadataInstanceFieldTypePerKey;
}