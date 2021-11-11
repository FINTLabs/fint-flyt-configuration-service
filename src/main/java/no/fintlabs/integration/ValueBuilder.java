package no.fintlabs.integration;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ValueBuilder {
    private String value;
    private List<Property> properties = new ArrayList<>();
}