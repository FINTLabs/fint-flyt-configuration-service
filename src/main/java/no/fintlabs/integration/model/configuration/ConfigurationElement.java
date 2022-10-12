package no.fintlabs.integration.model.configuration;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import no.fintlabs.integration.validation.constraints.UniqueChildrenKeys;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Collection;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@UniqueChildrenKeys
@Entity
public class ConfigurationElement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    @Setter(AccessLevel.NONE)
    private long id;

    @NotBlank
    private String key;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "parent_configuration_element_id")
    private Collection<@Valid ConfigurationElement> elements = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "configuration_element_id")
    private Collection<@Valid FieldConfiguration> fieldConfigurations = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "configuration_element_id")
    private Collection<@Valid CollectionFieldConfiguration> collectionFieldConfigurations = new ArrayList<>();

}
