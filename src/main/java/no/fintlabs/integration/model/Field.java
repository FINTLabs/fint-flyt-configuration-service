package no.fintlabs.integration.model;

import lombok.Data;

@Data
public class Field {
    private ValueBuildStrategy valueBuildStrategy;
    private String field;
    private ValueBuilder valueBuilder;
}