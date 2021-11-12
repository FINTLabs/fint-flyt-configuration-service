package no.fintlabs.integration;

import lombok.Data;

@Data
public class Field {
    private ValueBuildStrategy valueBuildStrategy;
    private String field;
    private ValueBuilder valueBuilder;
}