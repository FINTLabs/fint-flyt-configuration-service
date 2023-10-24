package no.fintlabs.model.configuration.entities.collection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.extern.jackson.Jacksonized;
import no.fintlabs.model.configuration.entities.ValueMapping;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@Getter
@EqualsAndHashCode
@Jacksonized
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ValuesFromCollectionMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    @Setter(AccessLevel.NONE)
    private long id;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(
            name = "values_from_collection_mapping_references_ordered",
            joinColumns = @JoinColumn(name = "values_from_collection_mapping_id"),
            inverseJoinColumns = @JoinColumn(name = "instance_collection_reference_id")
    )
    @NotEmpty
    private Collection<@Valid @NotNull InstanceCollectionReference> instanceCollectionReferencesOrdered;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @Valid
    @NotNull
    private ValueMapping valueMapping;

}
