package no.fintlabs.model.configuration.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ElementsFromCollectionMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    @Setter(AccessLevel.NONE)
    private long id;

    @ElementCollection
    @CollectionTable(name = "instanceCollectionReferencesOrdered")
    @OrderColumn(name = "index")
    @JoinColumn
    @NotEmpty
    private List<@NotBlank String> instanceCollectionReferencesOrdered;

    @OneToOne
    @Valid
    @NotNull
    private ElementMapping elementMapping;

}
