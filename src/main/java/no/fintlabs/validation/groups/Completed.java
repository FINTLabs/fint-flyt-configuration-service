package no.fintlabs.validation.groups;

import javax.validation.GroupSequence;

@GroupSequence({ValueParsability.class, IntegrationMetadataCorrelation.class, MetadataKeys.class, MetadataType.class})
public interface Completed {
}
