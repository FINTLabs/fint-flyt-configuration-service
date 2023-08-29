package no.fintlabs.model.configuration.entities.collection;

import lombok.*;
import lombok.extern.jackson.Jacksonized;
import no.fintlabs.model.configuration.entities.ObjectMapping;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@Getter
@EqualsAndHashCode
@Builder
@Jacksonized
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ObjectCollectionMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private long id;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(
            joinColumns = @JoinColumn(name = "collection_mapping_id"),
            inverseJoinColumns = @JoinColumn(name = "element_mapping_id")
    )
    private Collection<@Valid @NotNull ObjectMapping> objectMappings;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(
            joinColumns = @JoinColumn(name = "collection_mapping_id"),
            inverseJoinColumns = @JoinColumn(name = "from_collection_mapping_id")
    )
    private Collection<@Valid @NotNull ObjectsFromCollectionMapping> objectsFromCollectionMappings;

    @Override
    public String toString() {
        return "Sensitive data omitted";
    }

}
