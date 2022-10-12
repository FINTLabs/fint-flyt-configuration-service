package no.fintlabs.integration.model.metadata;

import lombok.Data;

@Data
public class IntegrationMetadata {
    private Long sourceApplicationId;
    private String sourceApplicationIntegrationId;
    private String sourceApplicationIntegrationUri;
    private String integrationDisplayName;
    private Long version;
}
