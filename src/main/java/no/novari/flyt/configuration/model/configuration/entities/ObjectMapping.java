package no.novari.flyt.configuration.model.configuration.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.MapKeyColumn;
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
import no.novari.flyt.configuration.model.configuration.entities.collection.ObjectCollectionMapping;
import no.novari.flyt.configuration.model.configuration.entities.collection.ValueCollectionMapping;

import java.util.Map;

@Getter
@Jacksonized
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
