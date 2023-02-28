package no.fintlabs.mapping;

import no.fintlabs.model.configuration.dtos.FromCollectionMappingDto;
import no.fintlabs.model.configuration.entities.collection.FromCollectionMapping;
import org.springframework.stereotype.Service;

import java.util.function.Function;
import java.util.function.Supplier;


@Service
public class FromCollectionMappingMappingService {

    private final InstanceCollectionReferencesMappingService instanceCollectionReferencesMappingService;

    public FromCollectionMappingMappingService(
            InstanceCollectionReferencesMappingService instanceCollectionReferencesMappingService
    ) {
        this.instanceCollectionReferencesMappingService = instanceCollectionReferencesMappingService;
    }

    public <E_FCM extends FromCollectionMapping<E_M>, E_M, DTO_M> E_FCM toEntity(
            Function<DTO_M, E_M> elementMappingToEntityMappingFunction,
            Supplier<E_FCM> fromCollectionMappingConstructor,
            FromCollectionMappingDto<DTO_M> fromCollectionMappingDto
    ) {
        E_FCM newInstance = fromCollectionMappingConstructor.get();
        newInstance.setInstanceCollectionReferencesOrdered(
                instanceCollectionReferencesMappingService.toEntity(
                        fromCollectionMappingDto.getInstanceCollectionReferencesOrdered()
                )
        );
        newInstance.setElementMapping(elementMappingToEntityMappingFunction.apply(fromCollectionMappingDto.getElementMapping()));
        return newInstance;
    }

    public <E_M, DTO_M> FromCollectionMappingDto<DTO_M> toDto(
            Function<E_M, DTO_M> elementMappingToDtoMappingFunction,
            FromCollectionMapping<E_M> fromCollectionMapping
    ) {
        return FromCollectionMappingDto
                .<DTO_M>builder()
                .instanceCollectionReferencesOrdered(
                        instanceCollectionReferencesMappingService.toDto(
                                fromCollectionMapping.getInstanceCollectionReferencesOrdered()
                        )
                )
                .elementMapping(elementMappingToDtoMappingFunction.apply(fromCollectionMapping.getElementMapping()))
                .build();
    }

}
