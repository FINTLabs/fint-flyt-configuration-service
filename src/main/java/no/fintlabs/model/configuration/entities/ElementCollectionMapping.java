package no.fintlabs.model.configuration.entities;

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
public class ElementCollectionMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private long id;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinTable(
            joinColumns = @JoinColumn(name = "parent_element_collection_mapping_id"),
            inverseJoinColumns = @JoinColumn(name = "child_element_mapping_id")
    )
    private Collection<@Valid @NotNull ElementMapping> elementMappings;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinTable(
            joinColumns = @JoinColumn(name = "parent_element_collection_mapping_id"),
            inverseJoinColumns = @JoinColumn(name = "child_elements_from_collection_mapping_id")
    )
    private Collection<@Valid @NotNull ElementsFromCollectionMapping> elementsFromCollectionMappings;

}
