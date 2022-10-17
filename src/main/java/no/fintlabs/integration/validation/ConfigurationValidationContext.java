package no.fintlabs.integration.validation;

import lombok.Builder;
import lombok.Data;
import no.fintlabs.integration.model.integration.Integration;
import no.fintlabs.integration.model.metadata.InstanceElementMetadata;
import no.fintlabs.integration.model.metadata.IntegrationMetadata;

import javax.validation.Payload;
import java.util.Map;

@Data
@Builder
public class ConfigurationValidationContext implements Payload {
    private final Integration integration;
    private final IntegrationMetadata metadata;
    private final Map<String, InstanceElementMetadata.Type> metadataInstanceFieldTypePerKey;
}
