package no.fintlabs.integration.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Property {

    private ValueSource source;
    private String key;
    @Column(name = "property_order")
    private int order;
}