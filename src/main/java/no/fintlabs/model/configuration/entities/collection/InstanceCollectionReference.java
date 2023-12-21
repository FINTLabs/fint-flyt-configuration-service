package no.fintlabs.model.configuration.entities.collection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

@Getter
@Builder
@Jacksonized
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class InstanceCollectionReference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    @Setter(AccessLevel.NONE)
    private long id;

    private int index;

    @NotBlank
    private String reference;

}
