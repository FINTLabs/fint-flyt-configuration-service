package no.fintlabs.integration.validation;

import lombok.Builder;
import lombok.Data;
import no.fintlabs.integration.model.metadata.InstanceElementMetadata;

import javax.validation.Payload;
import java.util.Map;

@Data
@Builder
public class ConfigurationValidationContext implements Payload {
    Map<String, InstanceElementMetadata.Type> metadataInstanceFieldTypePerKey;
}
