package no.fintlabs.integration.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InstanceElementMetadata {

    public enum Type {
        STRING, BOOLEAN, INTEGER, DOUBLE
    }

    private String key;

    private Type type;

    private String displayName;

    private List<InstanceElementMetadata> children;

}
