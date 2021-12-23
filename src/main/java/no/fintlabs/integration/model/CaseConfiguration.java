package no.fintlabs.integration.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CaseConfiguration {
    private CaseCreationStrategy caseCreationStrategy;
    private String caseNumber;
    private List<Field> fields = new ArrayList<>();
}