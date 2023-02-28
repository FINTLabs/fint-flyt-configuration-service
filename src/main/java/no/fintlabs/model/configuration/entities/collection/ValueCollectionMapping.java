package no.fintlabs.model.configuration.entities.collection;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import no.fintlabs.model.configuration.entities.ValueMapping;

import javax.persistence.Entity;

@SuperBuilder
@NoArgsConstructor
@Entity
public class ValueCollectionMapping extends CollectionMapping<ValueMapping, ValuesFromCollectionMapping> {

}
