package no.fintlabs.mapping;

import no.fintlabs.model.configuration.dtos.CollectionMappingDto;
import no.fintlabs.model.configuration.entities.collection.CollectionMapping;
import no.fintlabs.model.configuration.entities.collection.FromCollectionMapping;
import org.springframework.stereotype.Service;

import java.util.function.Function;
import java.util.function.Supplier;

@Service
public class CollectionMappingMappingService {

    private final FromCollectionMappingMappingService fromCollectionMappingMappingService;

    protected CollectionMappingMappingService(FromCollectionMappingMappingService fromCollectionMappingMappingService) {
        this.fromCollectionMappingMappingService = fromCollectionMappingMappingService;
    }

    public <E_M, E_CM extends CollectionMapping<E_M, E_FCM>, E_FCM extends FromCollectionMapping<E_M>, DTO_M>
    E_CM toEntity(
            Function<DTO_M, E_M> elementMappingToEntityFunction,
            Supplier<E_CM> collectionMappingConstructor,
            Supplier<E_FCM> fromCollectionMappingConstructor,
            CollectionMappingDto<DTO_M> collectionMappingDto
    ) {
        E_CM newInstance = collectionMappingConstructor.get();
        newInstance.setElementMappings(
                collectionMappingDto
                        .getElementMappings()
                        .stream()
                        .map(elementMappingToEntityFunction)
                        .toList()
        );
        newInstance.setFromCollectionMappings(
                collectionMappingDto
                        .getFromCollectionMappings()
                        .stream()
                        .map(fromCollectionMappingDto -> fromCollectionMappingMappingService.toEntity(
                                elementMappingToEntityFunction,
                                fromCollectionMappingConstructor,
                                fromCollectionMappingDto
                        ))
                        .toList()
        );
        return newInstance;
    }

    public <E_M, E_CM extends CollectionMapping<E_M, E_FCM>, E_FCM extends FromCollectionMapping<E_M>, DTO_M>
    CollectionMappingDto<DTO_M> toDto(
            Function<E_M, DTO_M> elementMappingToDtoMappingFunction,
            E_CM entity
    ) {
        return CollectionMappingDto
                .<DTO_M>builder()
                .elementMappings(
                        entity
                                .getElementMappings()
                                .stream()
                                .map(elementMappingToDtoMappingFunction)
                                .toList()
                )
                .fromCollectionMappings(
                        entity
                                .getFromCollectionMappings()
                                .stream()
                                .map(fromCollectionMapping -> fromCollectionMappingMappingService.toDto(
                                        elementMappingToDtoMappingFunction,
                                        fromCollectionMapping
                                ))
                                .toList()
                )
                .build();
    }

}
