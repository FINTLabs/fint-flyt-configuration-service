package no.fintlabs.integration.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "integration_configuration")
@EntityListeners(AuditingEntityListener.class)
public class IntegrationConfiguration {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    @JsonIgnore
    private Long id;

    @CreatedDate
    private LocalDateTime createdDate;

    private String integrationId;
    private String name;
    private String description;
    private String sourceApplication;
    private String sourceApplicationIntegrationId;
    private String orgId;
    private String destination;
    private int version;
    private boolean isPublished;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "case_configuration_id", referencedColumnName = "id")
    private CaseConfiguration caseConfiguration;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "record_configuration_id", referencedColumnName = "id")
    private RecordConfiguration recordConfiguration;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "document_configuration_id", referencedColumnName = "id")
    private DocumentConfiguration documentConfiguration;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "applicant_configuration_id", referencedColumnName = "id")
    private ApplicantConfiguration applicantConfiguration;

    public boolean isSameAs(String otherId) {
        return integrationId.equals(otherId);
    }
}
