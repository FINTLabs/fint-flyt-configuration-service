package no.fintlabs.integration;

import lombok.Data;

@Data
public class Property {
    private ValueSource source;
    private String key;
    private int order;
}