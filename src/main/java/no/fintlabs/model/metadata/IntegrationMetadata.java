package no.fintlabs.model.metadata;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

@Getter
@EqualsAndHashCode
@Jacksonized
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
