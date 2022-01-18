package no.fintlabs.integration.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ResourceReference {
    private final String id;
    private final String displayName;
}