package no.fintlabs.mapping;


import no.fintlabs.model.configuration.dtos.ObjectMappingDto;
import no.fintlabs.model.configuration.entities.ObjectMapping;
import org.springframework.stereotype.Service;

@Service
public class ObjectMappingMappingService implements ElementMappingMappingService<ObjectMapping, ObjectMappingDto> {

    private final PerKeyMappingService perKeyMappingService;
    private final ValueMappingMappingService valueMappingMappingService;
    private final ValueCollectionMappingMappingService valueCollectionMappingMappingService;
    private final ObjectCollectionMappingMappingService objectCollectionMappingMappingService;

    public ObjectMappingMappingService(
            PerKeyMappingService perKeyMappingService,
            ValueMappingMappingService valueMappingMappingService,
            ValueCollectionMappingMappingService valueCollectionMappingMappingService,
            ObjectCollectionMappingMappingService objectCollectionMappingMappingService
    ) {
        this.perKeyMappingService = perKeyMappingService;
        this.valueMappingMappingService = valueMappingMappingService;
        this.valueCollectionMappingMappingService = valueCollectionMappingMappingService;
        this.objectCollectionMappingMappingService = objectCollectionMappingMappingService;
    }

    public ObjectMapping toEntity(ObjectMappingDto objectMappingDto) {
        return ObjectMapping
                .builder()
                .valueMappingPerKey(
                        perKeyMappingService.mapPerKey(
                                objectMappingDto.getValueMappingPerKey(),
                                valueMappingMappingService::toEntity
                        )
                )
                .valueCollectionMappingPerKey(
                        perKeyMappingService.mapPerKey(
                                objectMappingDto.getValueCollectionMappingPerKey(),
                                valueCollectionMappingMappingService::toEntity
                        )
                )
                .objectMappingPerKey(
                        perKeyMappingService.mapPerKey(
                                objectMappingDto.getObjectMappingPerKey(),
                                this::toEntity
                        )
                )
                .objectCollectionMappingPerKey(
                        perKeyMappingService.mapPerKey(
                                objectMappingDto.getObjectCollectionMappingPerKey(),
                                objectCollectionMappingMappingService::toEntity
                        )
                )
                .build();
    }

    public ObjectMappingDto toDto(ObjectMapping objectMapping) {
        return ObjectMappingDto
                .builder()
                .valueMappingPerKey(
                        perKeyMappingService.mapPerKey(
                                objectMapping.getValueMappingPerKey(),
                                valueMappingMappingService::toDto
                        )
                )
                .valueCollectionMappingPerKey(
                        perKeyMappingService.mapPerKey(
                                objectMapping.getValueCollectionMappingPerKey(),
                                valueCollectionMappingMappingService::toDto
                        )
                )
                .objectMappingPerKey(
                        perKeyMappingService.mapPerKey(
                                objectMapping.getObjectMappingPerKey(),
                                this::toDto
                        )
                )
                .objectCollectionMappingPerKey(
                        perKeyMappingService.mapPerKey(
                                objectMapping.getObjectCollectionMappingPerKey(),
                                objectCollectionMappingMappingService::toDto
                        )
                )
                .build();
    }

}
