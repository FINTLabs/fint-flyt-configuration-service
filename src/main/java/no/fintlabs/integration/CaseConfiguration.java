package no.fintlabs.integration;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CaseConfiguration {
    private CaseCreationStrategy caseCreationStrategy;
    private List<Field> fields = new ArrayList<>();
}