package no.fintlabs.model.integration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Integration {

    public enum State {
        ACTIVE,
        DEACTIVATED
    }

    private Long sourceApplicationId;
    private String sourceApplicationIntegrationId;
    private String destination;
    private State state;
    private String activeConfigurationId;

}
