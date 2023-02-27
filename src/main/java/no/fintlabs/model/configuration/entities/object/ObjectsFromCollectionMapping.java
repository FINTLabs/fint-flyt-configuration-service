package no.fintlabs.model.configuration.entities.object;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import no.fintlabs.model.configuration.entities.InstanceCollectionReference;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ObjectsFromCollectionMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    @Setter(AccessLevel.NONE)
    private long id;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn
    @NotEmpty
    private Collection<@Valid @NotNull InstanceCollectionReference> instanceCollectionReferencesOrdered;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @Valid
    @NotNull
    private ObjectMapping objectMapping;

}
