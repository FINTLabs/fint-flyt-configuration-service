package no.fintlabs.integration.model;

import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
@Embeddable
public class ApplicantConfiguration {
    private String organisationNumber;
    private String nationalIdentityNumber;

    @OneToMany(mappedBy = "integrationConfiguration", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ApplicantConfigurationField> fields = new LinkedHashSet<>();
}