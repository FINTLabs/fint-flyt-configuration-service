package no.fintlabs.integration.model;


import lombok.*;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
@Embeddable
public class DocumentConfiguration {

    @OneToMany(mappedBy = "integrationConfiguration", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<DocumentConfigurationField> fields = new LinkedHashSet<>();
}
