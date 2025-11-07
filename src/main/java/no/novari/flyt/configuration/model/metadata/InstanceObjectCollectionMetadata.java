package no.novari.flyt.configuration.model.metadata;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
public class InstanceObjectCollectionMetadata {
    private final String key;
    private final InstanceMetadataContent objectMetadata;
}
