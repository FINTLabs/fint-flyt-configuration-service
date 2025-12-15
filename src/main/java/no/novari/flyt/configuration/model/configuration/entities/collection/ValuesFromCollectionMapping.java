package no.novari.flyt.configuration.model.configuration.entities.collection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;
import no.novari.flyt.configuration.model.configuration.entities.ValueMapping;

import java.util.Collection;

@Getter
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
