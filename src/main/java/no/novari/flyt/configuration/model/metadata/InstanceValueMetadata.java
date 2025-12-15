package no.novari.flyt.configuration.model.metadata;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
public class InstanceValueMetadata {

    public enum Type {
        STRING, FILE
    }

    private final String key;
    private final Type type;

}
