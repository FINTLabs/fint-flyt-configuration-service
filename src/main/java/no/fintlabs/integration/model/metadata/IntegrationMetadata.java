package no.fintlabs.integration.model.metadata;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IntegrationMetadata {
    private Long sourceApplicationId;
    private String sourceApplicationIntegrationId;
    private String sourceApplicationIntegrationUri;
    private String integrationDisplayName;
    private Long version;
}
