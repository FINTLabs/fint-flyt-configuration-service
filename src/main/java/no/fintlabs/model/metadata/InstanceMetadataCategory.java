package no.fintlabs.model.metadata;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
public class InstanceMetadataCategory {
    private final InstanceMetadataContent content;
}
