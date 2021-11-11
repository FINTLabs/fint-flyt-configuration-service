package no.fintlabs.integration;

import lombok.Data;

@Data
public class IntegrationConfiguration {
    private Metadata metadata;
    private RecordConfiguration recordConfiguration;
    private DocumentConfiguration documentConfiguration;
    private CaseConfiguration caseConfiguration;
    private ApplicantConfiguration applicantConfiguration;
}