package no.fintlabs.integration.model.metadata;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
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

    public Optional<String> getKey() {
        return Optional.ofNullable(key);
    }

    private Type type;

    public Optional<Type> getType() {
        return Optional.ofNullable(type);
    }

    private String displayName;
    private List<InstanceElementMetadata> children;
}
