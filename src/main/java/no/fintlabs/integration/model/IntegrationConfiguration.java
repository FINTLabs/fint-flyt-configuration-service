package no.fintlabs.integration.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
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

    @Embedded
    private CaseConfiguration caseConfiguration;

    @Embedded
    private RecordConfiguration recordConfiguration;

    @Embedded
    private DocumentConfiguration documentConfiguration;


    @Embedded
    private ApplicantConfiguration applicantConfiguration;

    public boolean isSameAs(String otherId) {
        return integrationId.equals(otherId);
    }
}
