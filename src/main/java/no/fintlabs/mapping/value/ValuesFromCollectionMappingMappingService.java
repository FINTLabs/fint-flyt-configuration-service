package no.fintlabs.mapping.value;

import no.fintlabs.mapping.InstanceCollectionReferencesMappingService;
import no.fintlabs.model.configuration.dtos.value.ValuesFromCollectionMappingDto;
import no.fintlabs.model.configuration.entities.value.ValuesFromCollectionMapping;
import org.springframework.stereotype.Service;

@Service
public class ValuesFromCollectionMappingMappingService {

    private final InstanceCollectionReferencesMappingService instanceCollectionReferencesMappingService;
    private final ValueMappingMappingService valueMappingMappingService;

    public ValuesFromCollectionMappingMappingService(
            InstanceCollectionReferencesMappingService instanceCollectionReferencesMappingService,
            ValueMappingMappingService valueMappingMappingService
    ) {
        this.valueMappingMappingService = valueMappingMappingService;
        this.instanceCollectionReferencesMappingService = instanceCollectionReferencesMappingService;
    }


    public ValuesFromCollectionMapping toEntity(ValuesFromCollectionMappingDto valuesFromCollectionMappingDto) {
        return ValuesFromCollectionMapping
                .builder()
                .instanceCollectionReferencesOrdered(
                        instanceCollectionReferencesMappingService.toEntity(
                                valuesFromCollectionMappingDto.getInstanceCollectionReferencesOrdered()
                        )
                )
                .valueMapping(
                        valueMappingMappingService.toEntity(
                                valuesFromCollectionMappingDto.getValueMapping()
                        )
                )
                .build();
    }

    public ValuesFromCollectionMappingDto toDto(ValuesFromCollectionMapping valuesFromCollectionMapping) {
        return ValuesFromCollectionMappingDto
                .builder()
                .instanceCollectionReferencesOrdered(
                        instanceCollectionReferencesMappingService.toDto(
                                valuesFromCollectionMapping.getInstanceCollectionReferencesOrdered()
                        )
                )
                .valueMapping(valueMappingMappingService.toDto(valuesFromCollectionMapping.getValueMapping()))
                .build();
    }

}
