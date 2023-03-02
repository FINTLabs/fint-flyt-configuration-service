package no.fintlabs.mapping;

import no.fintlabs.model.configuration.dtos.FromCollectionMappingDto;
import no.fintlabs.model.configuration.dtos.ValueMappingDto;
import no.fintlabs.model.configuration.entities.collection.ValuesFromCollectionMapping;
import org.springframework.stereotype.Service;


@Service
public class ValuesFromCollectionMappingMappingService {

    private final InstanceCollectionReferencesMappingService instanceCollectionReferencesMappingService;
    private final ValueMappingMappingService valueMappingMappingService;

    public ValuesFromCollectionMappingMappingService(
            InstanceCollectionReferencesMappingService instanceCollectionReferencesMappingService,
            ValueMappingMappingService valueMappingMappingService
    ) {
        this.instanceCollectionReferencesMappingService = instanceCollectionReferencesMappingService;
        this.valueMappingMappingService = valueMappingMappingService;
    }

    public ValuesFromCollectionMapping toEntity(
            FromCollectionMappingDto<ValueMappingDto> valuesFromCollectionMappingDto
    ) {
        return ValuesFromCollectionMapping
                .builder()
                .instanceCollectionReferencesOrdered(
                        instanceCollectionReferencesMappingService.toEntity(
                                valuesFromCollectionMappingDto.getInstanceCollectionReferencesOrdered()
                        )
                )
                .valueMapping(
                        valueMappingMappingService.toEntity(valuesFromCollectionMappingDto.getElementMapping())
                )
                .build();
    }

    public FromCollectionMappingDto<ValueMappingDto> toDto(ValuesFromCollectionMapping valuesFromCollectionMapping) {
        return FromCollectionMappingDto
                .<ValueMappingDto>builder()
                .instanceCollectionReferencesOrdered(
                        instanceCollectionReferencesMappingService.toDto(
                                valuesFromCollectionMapping.getInstanceCollectionReferencesOrdered()
                        )
                )
                .elementMapping(valueMappingMappingService.toDto(valuesFromCollectionMapping.getValueMapping()))
                .build();
    }

}
