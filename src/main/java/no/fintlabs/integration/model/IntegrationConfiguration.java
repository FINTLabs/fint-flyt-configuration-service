package no.fintlabs.integration.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "integration_configuration")
public class IntegrationConfiguration {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    @JsonIgnore
    private Long id;

    @CreatedDate
    private LocalDateTime documentCreatedDate;

    private String integrationId;
    private String name;
    private String description;
    private String sourceApplication;
    private String sourceApplicationIntegrationId;
    private String orgId;
    private String destination;
    private int version;
    private boolean isPublished;

    @OneToMany(mappedBy = "integrationConfiguration", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RecordConfigurationField> recordConfiguration = new LinkedHashSet<>();

    @OneToMany(mappedBy = "integrationConfiguration", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<DocumentConfigurationField> documentConfiguration = new LinkedHashSet<>();

    @Embedded
    private CaseConfiguration caseConfiguration;
    //private ApplicantConfiguration applicantConfiguration;

    public boolean isSameAs(String otherId) {
        return integrationId.equals(otherId);
    }
}
