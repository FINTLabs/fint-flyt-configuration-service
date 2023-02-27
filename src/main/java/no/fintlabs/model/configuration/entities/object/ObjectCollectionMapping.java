package no.fintlabs.model.configuration.entities.object;

import lombok.*;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ObjectCollectionMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private long id;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinTable(
            joinColumns = @JoinColumn(name = "parent_object_collection_mapping_id"),
            inverseJoinColumns = @JoinColumn(name = "child_object_mapping_id")
    )
    private Collection<@Valid @NotNull ObjectMapping> objectMappings;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinTable(
            joinColumns = @JoinColumn(name = "parent_object_collection_mapping_id"),
            inverseJoinColumns = @JoinColumn(name = "child_objects_from_collection_mapping_id")
    )
    private Collection<@Valid @NotNull ObjectsFromCollectionMapping> objectsFromCollectionMappings;

}
