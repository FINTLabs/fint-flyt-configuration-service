package no.fintlabs.integration;

import no.fintlabs.integration.model.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/integration/configuration")
public class ConfigurationController {

    @GetMapping
    public ResponseEntity<List<IntegrationConfiguration>> getIntegrationConfigurations() {
        IntegrationConfiguration integrationConfiguration = new IntegrationConfiguration();
        Metadata metadata = new Metadata();
        metadata.setName("Test integration");
        metadata.setDescription("Test integration description");
        metadata.setVersion(1);

        integrationConfiguration.setMetadata(metadata);
        CaseConfiguration caseConfiguration = new CaseConfiguration();
        caseConfiguration.setCaseCreationStrategy(CaseCreationStrategy.NEW);
        Field field1 = new Field();
        field1.setField("title");
        field1.setValueBuildStrategy(ValueBuildStrategy.COMBINE_STRING_VALUE);

        Property property1 = new Property();
        property1.setKey("fornavn");
        property1.setOrder(0);
        property1.setSource(ValueSource.FORM);
        Property property2 = new Property();
        property2.setKey("etternavn");
        property2.setOrder(1);
        property2.setSource(ValueSource.FORM);



        field1.setValueBuilder(       ValueBuilder.builder()
                .value("Søknad for %s %s")
                .properties(Arrays.asList(
                        property1,
                        property2
                )).build());

        Field field2 = new Field();
        field2.setValueBuildStrategy(ValueBuildStrategy.FIXED_ARCHIVE_CODE_VALUE);
        field2.setField("caseUnit");
        field2.setValueBuilder(ValueBuilder.builder().value("123").build());


        caseConfiguration.setFields(Arrays.asList(field1, field2));

        integrationConfiguration.setCaseConfiguration(caseConfiguration);
        RecordConfiguration recordConfiguration = new RecordConfiguration();

        Field recordField1 = new Field();
        recordField1.setField("administrativeUnit");
        recordField1.setValueBuildStrategy(ValueBuildStrategy.FIXED_ARCHIVE_CODE_VALUE);
        recordField1.setValueBuilder(ValueBuilder.builder().value("897").build());
        recordConfiguration.setFields(List.of(recordField1));
        integrationConfiguration.setRecordConfiguration(recordConfiguration);

        DocumentConfiguration documentConfiguration = new DocumentConfiguration();
        Field documentField = new Field();
        documentField.setField("accessCode");
        documentField.setValueBuildStrategy(ValueBuildStrategy.FIXED_ARCHIVE_CODE_VALUE);
        documentField.setValueBuilder(ValueBuilder.builder().value("43").build());
        documentConfiguration.setFields(List.of(documentField));

        integrationConfiguration.setDocumentConfiguration(documentConfiguration);

        ApplicantConfiguration applicantConfiguration = new ApplicantConfiguration();
        Field applicantField = new Field();
        applicantField.setField("name");
        applicantField.setValueBuildStrategy(ValueBuildStrategy.COMBINE_STRING_VALUE);
        applicantField.setValueBuilder(ValueBuilder.builder()
                        .value("%s %s")
                        .properties(Arrays.asList(
                                Property.builder()
                                        .key("fornavn")
                                        .order(0)
                                        .source(ValueSource.FORM)
                                        .build(),
                                Property.builder()
                                        .key("etternavn")
                                        .order(1)
                                        .source(ValueSource.FORM)
                                        .build()
                        ))
                .build());
        applicantConfiguration.setFields(List.of(applicantField));
        integrationConfiguration.setApplicantConfiguration(applicantConfiguration);

        return ResponseEntity.ok(List.of(integrationConfiguration));
    }
}
