package no.fintlabs.model.configuration.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@EqualsAndHashCode
@Builder
@Jacksonized
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ValueMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    @Setter(AccessLevel.NONE)
    private long id;

    public enum Type {
        STRING, URL, BOOLEAN, DYNAMIC_STRING, FILE, VALUE_CONVERTING
    }

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private Type type;

    private String mappingString;

    @Override
    public String toString() {
        return "Sensitive data omitted";
    }

}
