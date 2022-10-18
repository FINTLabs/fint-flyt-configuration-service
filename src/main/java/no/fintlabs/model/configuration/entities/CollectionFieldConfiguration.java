package no.fintlabs.model.configuration.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CollectionFieldConfiguration {

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
    @JoinColumn
    Collection<String> values;

}
