package no.novari.flyt.configuration.validation.groups;

import jakarta.validation.GroupSequence;

@GroupSequence({ValueParsability.class, InstanceValueKeys.class, InstanceValueTypes.class})
public interface Completed {
}
