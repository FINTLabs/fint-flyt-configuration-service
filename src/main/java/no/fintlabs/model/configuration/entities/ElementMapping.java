package no.fintlabs.model.configuration.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ElementMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private long id;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(
            joinColumns = @JoinColumn(name = "parent_element_mapping_id"),
            inverseJoinColumns = @JoinColumn(name = "child_value_mapping_id")
    )
    @MapKeyColumn(name = "key")
    private Map<String, @Valid @NotNull ValueMapping> valueMappingPerKey = new HashMap<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(
            joinColumns = @JoinColumn(name = "parent_element_mapping_id"),
            inverseJoinColumns = @JoinColumn(name = "child_element_mapping_id")
    )
    @MapKeyColumn(name = "key")
    private Map<String, @Valid @NotNull ElementMapping> elementMappingPerKey = new HashMap<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(
            joinColumns = @JoinColumn(name = "parent_element_mapping_id"),
            inverseJoinColumns = @JoinColumn(name = "child_elementCollection_mapping_id")
    )
    @MapKeyColumn(name = "key")
    private Map<String, @Valid @NotNull ElementCollectionMapping> elementCollectionMappingPerKey = new HashMap<>();

}
