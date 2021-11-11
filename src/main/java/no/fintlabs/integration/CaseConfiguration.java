package no.fintlabs.integration;

import lombok.Data;

import java.util.List;

@Data
public class CaseConfiguration {
    private String creationStrategy;
    private List<Field> fields;
}