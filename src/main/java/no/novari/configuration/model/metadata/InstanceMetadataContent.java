package no.novari.configuration.model.metadata;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.util.Collection;

@Getter
@Builder
@Jacksonized
public class InstanceMetadataContent {
    private final Collection<InstanceValueMetadata> instanceValueMetadata;
    private final Collection<InstanceObjectCollectionMetadata> instanceObjectCollectionMetadata;
    private final Collection<InstanceMetadataCategory> categories;
}
