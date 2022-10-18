package no.fintlabs.model.configuration.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ConfigurationElement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private long id;

    @NotBlank
    private String key;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "parent_configuration_element_id")
    private Collection<@Valid @NotNull ConfigurationElement> elements = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "configuration_element_id")
    private Collection<@Valid @NotNull FieldConfiguration> fieldConfigurations = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "configuration_element_id")
    private Collection<@Valid @NotNull CollectionFieldConfiguration> collectionFieldConfigurations = new ArrayList<>();

}
