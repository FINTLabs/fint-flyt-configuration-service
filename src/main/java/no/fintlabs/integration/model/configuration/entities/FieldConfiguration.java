package no.fintlabs.integration.model.configuration.entities;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class FieldConfiguration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private long id;

    public enum Type {
        STRING, URL, BOOLEAN, DYNAMIC_STRING
    }

    private String key;

    @Enumerated(value = EnumType.STRING)
    private Type type;

    private String value;

}
