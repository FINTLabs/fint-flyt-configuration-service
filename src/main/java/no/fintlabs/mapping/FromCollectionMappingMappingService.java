package no.fintlabs.mapping;

import no.fintlabs.model.configuration.dtos.FromCollectionMappingDto;
import no.fintlabs.model.configuration.entities.collection.FromCollectionMapping;
import org.springframework.stereotype.Service;

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
            ElementMappingMappingService<E_M, DTO_M> elementMappingMappingService,
            FromCollectionMappingDto<DTO_M> fromCollectionMappingDto,
            Supplier<E_FCM> fromCollectionMappingConstructor
    ) {
        E_FCM newInstance = fromCollectionMappingConstructor.get();
        newInstance.setInstanceCollectionReferencesOrdered(
                instanceCollectionReferencesMappingService.toEntity(
                        fromCollectionMappingDto.getInstanceCollectionReferencesOrdered()
                )
        );
        newInstance.setElementMapping(elementMappingMappingService.toEntity(fromCollectionMappingDto.getElementMapping()));
        return newInstance;
    }

    public <E_M, DTO_M> FromCollectionMappingDto<DTO_M> toDto(
            ElementMappingMappingService<E_M, DTO_M> elementMappingMappingService,
            FromCollectionMapping<E_M> fromCollectionMapping
    ) {
        return FromCollectionMappingDto
                .<DTO_M>builder()
                .instanceCollectionReferencesOrdered(
                        instanceCollectionReferencesMappingService.toDto(
                                fromCollectionMapping.getInstanceCollectionReferencesOrdered()
                        )
                )
                .elementMapping(elementMappingMappingService.toDto(fromCollectionMapping.getElementMapping()))
                .build();
    }

}
