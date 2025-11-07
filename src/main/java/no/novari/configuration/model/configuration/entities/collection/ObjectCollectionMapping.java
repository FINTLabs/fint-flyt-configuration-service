package no.novari.configuration.model.configuration.entities.collection;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;
import no.novari.configuration.model.configuration.entities.ObjectMapping;

import java.util.Collection;

@Getter
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

}
