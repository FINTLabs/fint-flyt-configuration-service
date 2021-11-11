package no.fintlabs.integration;

import lombok.Data;

import java.util.List;

@Data
public class DocumentConfiguration {
    private List<Field> fields;
}