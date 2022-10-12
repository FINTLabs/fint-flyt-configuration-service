package no.fintlabs.integration.model.metadata;

import lombok.Data;

import java.util.List;

@Data
public class InstanceElementMetadata {

    public enum Type {
        STRING,
        DATE,
        DATETIME,
        URL,
        EMAIL,
        PHONE,
        BOOLEAN,
        INTEGER,
        DOUBLE
    }

    private String key;
    private Type type;
    private String displayName;
    private List<InstanceElementMetadata> children;
}
