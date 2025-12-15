package no.novari.flyt.configuration.model.integration;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
public class Integration {

    public enum State {
        ACTIVE,
        DEACTIVATED
    }

    private final Long sourceApplicationId;
    private final String sourceApplicationIntegrationId;
    private final String destination;
    private final State state;
    private final String activeConfigurationId;

}
