package no.fintlabs.mapping;

import no.fintlabs.model.configuration.dtos.CollectionMappingDto;
import no.fintlabs.model.configuration.entities.collection.CollectionMapping;
import no.fintlabs.model.configuration.entities.collection.FromCollectionMapping;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;

@Service
public class CollectionMappingMappingService {

    private final FromCollectionMappingMappingService fromCollectionMappingMappingService;

    protected CollectionMappingMappingService(FromCollectionMappingMappingService fromCollectionMappingMappingService) {
        this.fromCollectionMappingMappingService = fromCollectionMappingMappingService;
    }

    public <E_M, E_CM extends CollectionMapping<E_M, E_FCM>, E_FCM extends FromCollectionMapping<E_M>, DTO_M>
    E_CM toEntity(
            ElementMappingMappingService<E_M, DTO_M> elementMappingMappingService,
            CollectionMappingDto<DTO_M> collectionMappingDto,
            Supplier<E_CM> collectionMappingConstructor,
            Supplier<E_FCM> fromCollectionMappingConstructor
    ) {
        E_CM newInstance = collectionMappingConstructor.get();
        newInstance.setElementMappings(
                collectionMappingDto
                        .getElementMappings()
                        .stream()
                        .map(elementMappingMappingService::toEntity)
                        .toList()
        );
        newInstance.setFromCollectionMappings(
                collectionMappingDto
                        .getFromCollectionMappings()
                        .stream()
                        .map(fromCollectionMappingDto -> fromCollectionMappingMappingService.toEntity(
                                elementMappingMappingService,
                                fromCollectionMappingDto,
                                fromCollectionMappingConstructor
                        ))
                        .toList()
        );
        return newInstance;
    }

    public <E_M, E_CM extends CollectionMapping<E_M, E_FCM>, E_FCM extends FromCollectionMapping<E_M>, DTO_M>
    CollectionMappingDto<DTO_M> toDto(
            ElementMappingMappingService<E_M, DTO_M> elementMappingMappingService,
            E_CM entity
    ) {
        return CollectionMappingDto
                .<DTO_M>builder()
                .elementMappings(
                        entity
                                .getElementMappings()
                                .stream()
                                .map(elementMappingMappingService::toDto)
                                .toList()
                )
                .fromCollectionMappings(
                        entity
                                .getFromCollectionMappings()
                                .stream()
                                .map(fromCollectionMapping -> fromCollectionMappingMappingService.toDto(
                                        elementMappingMappingService,
                                        fromCollectionMapping
                                ))
                                .toList()
                )
                .build();
    }

}
