package no.fintlabs.model.configuration.entities.collection;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@Getter
@Setter
@EqualsAndHashCode
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public abstract class CollectionMapping<M, FCM extends FromCollectionMapping<M>> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private long id;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(
            joinColumns = @JoinColumn(name = "collection_mapping_id"),
            inverseJoinColumns = @JoinColumn(name = "element_mapping_id")
    )
    private Collection<@Valid @NotNull M> elementMappings;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(
            joinColumns = @JoinColumn(name = "collection_mapping_id"),
            inverseJoinColumns = @JoinColumn(name = "from_collection_mapping_id")
    )
    private Collection<@Valid @NotNull FCM> fromCollectionMappings;

}
