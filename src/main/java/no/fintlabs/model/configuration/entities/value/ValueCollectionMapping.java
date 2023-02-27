package no.fintlabs.model.configuration.entities.value;

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
public class ValueCollectionMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private long id;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinTable(
            joinColumns = @JoinColumn(name = "parent_value_collection_mapping_id"),
            inverseJoinColumns = @JoinColumn(name = "child_value_mapping_id")
    )
    private Collection<@Valid @NotNull ValueMapping> valueMappings;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinTable(
            joinColumns = @JoinColumn(name = "parent_value_collection_mapping_id"),
            inverseJoinColumns = @JoinColumn(name = "child_values_from_collection_mapping_id")
    )
    private Collection<@Valid @NotNull ValuesFromCollectionMapping> valuesFromCollectionMappings;

}
