package no.fintlabs.integration.model;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.Collection;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Audited(withModifiedFlag = true)
@Entity
public class FieldCollectionConfiguration {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Setter(AccessLevel.NONE)
    private UUID id;

    public enum Type {
        STRING, URL, BOOLEAN
    }

    private String key;

    @Enumerated(value = EnumType.STRING)
    private Type type;

    @ElementCollection
    @JoinColumn(name = "field_collection_configuration_id")
    Collection<String> values;

}
