package no.fintlabs.model.configuration.entities;

import lombok.*;
import no.fintlabs.model.configuration.entities.collection.ObjectCollectionMapping;
import no.fintlabs.model.configuration.entities.collection.ValueCollectionMapping;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ObjectMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private long id;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(
            joinColumns = @JoinColumn(name = "object_mapping_id"),
            inverseJoinColumns = @JoinColumn(name = "value_mapping_id")
    )
    @MapKeyColumn(name = "key")
    private Map<String, @Valid @NotNull ValueMapping> valueMappingPerKey;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(
            joinColumns = @JoinColumn(name = "object_mapping_id"),
            inverseJoinColumns = @JoinColumn(name = "value_collection_mapping_id")
    )
    @MapKeyColumn(name = "key")
    private Map<String, @Valid @NotNull ValueCollectionMapping> valueCollectionMappingPerKey;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(
            joinColumns = @JoinColumn(name = "parent_id"),
            inverseJoinColumns = @JoinColumn(name = "child_id")
    )
    @MapKeyColumn(name = "key")
    private Map<String, @Valid @NotNull ObjectMapping> objectMappingPerKey;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(
            joinColumns = @JoinColumn(name = "object_mapping_id"),
            inverseJoinColumns = @JoinColumn(name = "object_collection_mapping_id")
    )
    @MapKeyColumn(name = "key")
    private Map<String, @Valid @NotNull ObjectCollectionMapping> objectCollectionMappingPerKey;

}
