package no.fintlabs.integration.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import no.fintlabs.integration.validation.constraints.ValueParsableAsType;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ValueParsableAsType
@Entity
public class FieldCollectionConfiguration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    @Setter(AccessLevel.NONE)
    private long id;

    public enum Type {
        STRING, URL
    }

    @NotBlank
    private String key;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private Type type;

    @ElementCollection
    @JoinColumn(name = "field_collection_configuration_id")
    Collection<String> values;

}
