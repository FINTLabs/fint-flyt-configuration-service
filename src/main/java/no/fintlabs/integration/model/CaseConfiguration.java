package no.fintlabs.integration.model;

import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Data
@Embeddable
public class CaseConfiguration {
    private CaseCreationStrategy caseCreationStrategy;
    private String caseNumber;

    @OneToMany(mappedBy = "integrationConfiguration", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CaseConfigurationField> fields = new LinkedHashSet<>();
}