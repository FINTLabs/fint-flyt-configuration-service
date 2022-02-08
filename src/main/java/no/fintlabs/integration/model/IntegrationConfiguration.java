package no.fintlabs.integration.model;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document
public class IntegrationConfiguration {
    @Id
    private String documentId;

    @CreatedDate
    private LocalDateTime documentCreatedDate;

    private String id;
    private String name;
    private String description;
    private String sourceApplication;
    private String sourceApplicationIntegrationID;
    private int version;

    private RecordConfiguration recordConfiguration;
    private DocumentConfiguration documentConfiguration;
    private CaseConfiguration caseConfiguration;
    private ApplicantConfiguration applicantConfiguration;

    public boolean isSameAs(String otherId) {
        return id.equals(otherId);
    }
}
