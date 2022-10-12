package no.fintlabs.integration.model.configuration;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import no.fintlabs.integration.validation.constraints.ValueParsableAsType;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ValueParsableAsType
@Entity
public class FieldConfiguration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    @Setter(AccessLevel.NONE)
    private long id;

    public enum Type {
        STRING, URL, BOOLEAN, DYNAMIC_STRING
    }

    @NotBlank
    private String key;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private Type type;

    private String value;

}
