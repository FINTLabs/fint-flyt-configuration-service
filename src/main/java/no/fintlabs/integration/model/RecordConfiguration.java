package no.fintlabs.integration.model;

import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
@Embeddable
public class RecordConfiguration {

    @OneToMany(mappedBy = "integrationConfiguration", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RecordConfigurationField> fields = new LinkedHashSet<>();
}
