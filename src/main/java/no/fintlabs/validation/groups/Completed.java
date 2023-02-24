package no.fintlabs.validation.groups;

import javax.validation.GroupSequence;

@GroupSequence({ValueParsability.class, InstanceValueKeys.class, InstanceValueTypes.class})
public interface Completed {
}
