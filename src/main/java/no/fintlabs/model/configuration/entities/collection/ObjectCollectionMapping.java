package no.fintlabs.model.configuration.entities.collection;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import no.fintlabs.model.configuration.entities.ObjectMapping;

import javax.persistence.Entity;

@SuperBuilder
@NoArgsConstructor
@Entity
public class ObjectCollectionMapping extends CollectionMapping<ObjectMapping, ObjectsFromCollectionMapping> {
}
