package no.fintlabs.integration.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embedded;
import javax.persistence.MappedSuperclass;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public abstract class Field {
    protected ValueBuildStrategy valueBuildStrategy;
    protected String field;

    @Embedded
    protected ValueBuilder valueBuilder;
}