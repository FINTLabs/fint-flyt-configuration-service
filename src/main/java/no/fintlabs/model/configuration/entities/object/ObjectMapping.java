package no.fintlabs.model.configuration.entities.object;

import lombok.*;
import no.fintlabs.model.configuration.entities.value.ValueCollectionMapping;
import no.fintlabs.model.configuration.entities.value.ValueMapping;

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

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinTable(
            joinColumns = @JoinColumn(name = "parent_object_mapping_id"),
            inverseJoinColumns = @JoinColumn(name = "child_value_mapping_id")
    )
    @MapKeyColumn(name = "key")
    private Map<String, @Valid @NotNull ValueMapping> valueMappingPerKey;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinTable(
            joinColumns = @JoinColumn(name = "parent_value_mapping_id"),
            inverseJoinColumns = @JoinColumn(name = "child_value_collection_mapping_id")
    )
    @MapKeyColumn(name = "key")
    private Map<String, @Valid @NotNull ValueCollectionMapping> valueCollectionMappingPerKey;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinTable(
            joinColumns = @JoinColumn(name = "parent_object_mapping_id"),
            inverseJoinColumns = @JoinColumn(name = "child_object_mapping_id")
    )
    @MapKeyColumn(name = "key")
    private Map<String, @Valid @NotNull ObjectMapping> objectMappingPerKey;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinTable(
            joinColumns = @JoinColumn(name = "parent_object_mapping_id"),
            inverseJoinColumns = @JoinColumn(name = "child_object_collection_mapping_id")
    )
    @MapKeyColumn(name = "key")
    private Map<String, @Valid @NotNull ObjectCollectionMapping> objectCollectionMappingPerKey;

}
