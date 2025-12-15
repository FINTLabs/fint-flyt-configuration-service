package no.novari.flyt.configuration.model.metadata;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
