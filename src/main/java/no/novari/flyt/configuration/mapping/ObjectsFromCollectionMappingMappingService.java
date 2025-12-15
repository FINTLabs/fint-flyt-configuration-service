package no.novari.flyt.configuration.mapping;

import no.novari.flyt.configuration.model.configuration.dtos.FromCollectionMappingDto;
import no.novari.flyt.configuration.model.configuration.dtos.ObjectMappingDto;
import no.novari.flyt.configuration.model.configuration.entities.collection.ObjectsFromCollectionMapping;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;


@Service
public class ObjectsFromCollectionMappingMappingService {

    private final InstanceCollectionReferencesMappingService instanceCollectionReferencesMappingService;
    private final ObjectMappingMappingService objectMappingMappingService;

    public ObjectsFromCollectionMappingMappingService(
            InstanceCollectionReferencesMappingService instanceCollectionReferencesMappingService,
            @Lazy ObjectMappingMappingService objectMappingMappingService
    ) {
        this.instanceCollectionReferencesMappingService = instanceCollectionReferencesMappingService;
        this.objectMappingMappingService = objectMappingMappingService;
    }

    public ObjectsFromCollectionMapping toEntity(
            FromCollectionMappingDto<ObjectMappingDto> objectsFromCollectionMappingDto
    ) {
        return ObjectsFromCollectionMapping
                .builder()
                .instanceCollectionReferencesOrdered(
                        instanceCollectionReferencesMappingService.toEntity(
                                objectsFromCollectionMappingDto.getInstanceCollectionReferencesOrdered()
                        )
                )
                .objectMapping(
                        objectMappingMappingService.toEntity(objectsFromCollectionMappingDto.getElementMapping())
                )
                .build();
    }

    public FromCollectionMappingDto<ObjectMappingDto> toDto(ObjectsFromCollectionMapping objectsFromCollectionMapping) {
        return FromCollectionMappingDto
                .<ObjectMappingDto>builder()
                .instanceCollectionReferencesOrdered(
                        instanceCollectionReferencesMappingService.toDto(
                                objectsFromCollectionMapping.getInstanceCollectionReferencesOrdered()
                        )
                )
                .elementMapping(objectMappingMappingService.toDto(objectsFromCollectionMapping.getObjectMapping()))
                .build();
    }

}
