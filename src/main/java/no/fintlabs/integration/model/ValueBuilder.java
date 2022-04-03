package no.fintlabs.integration.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class ValueBuilder {
    private String value;

    @ElementCollection
    private Set<Property> properties = new LinkedHashSet<>();
}