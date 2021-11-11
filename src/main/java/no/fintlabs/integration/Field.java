package no.fintlabs.integration;

import lombok.Data;

@Data
public class Field {
    private String valueBuildStrategy;
    private String field;
    private ValueBuilder valueBuilder;
}