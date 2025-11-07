package no.fintlabs.validation;

import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import no.fintlabs.kafka.InstanceMetadataRequestProducerService;
import no.fintlabs.kafka.IntegrationRequestProducerService;
import no.fintlabs.kafka.MetadataRequestProducerService;
import no.fintlabs.model.integration.Integration;
import no.fintlabs.model.metadata.InstanceMetadataCategory;
import no.fintlabs.model.metadata.InstanceMetadataContent;
import no.fintlabs.model.metadata.InstanceValueMetadata;
import no.fintlabs.model.metadata.IntegrationMetadata;
import org.hibernate.validator.HibernateValidatorFactory;
import org.springframework.stereotype.Service;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ConfigurationValidatorFactory {

    private final ValidatorFactory validatorFactory;

    private final IntegrationRequestProducerService integrationRequestProducerService;
    private final MetadataRequestProducerService metadataRequestProducerService;
    private final InstanceMetadataRequestProducerService instanceMetadataRequestProducerService;

    public ConfigurationValidatorFactory(
            ValidatorFactory validatorFactory,
            IntegrationRequestProducerService integrationRequestProducerService,
            MetadataRequestProducerService metadataRequestProducerService,
            InstanceMetadataRequestProducerService instanceMetadataRequestProducerService
    ) {
        this.validatorFactory = validatorFactory;
        this.integrationRequestProducerService = integrationRequestProducerService;
        this.metadataRequestProducerService = metadataRequestProducerService;
        this.instanceMetadataRequestProducerService = instanceMetadataRequestProducerService;
    }

    public Validator getValidator(Long integrationId, Long metadataId) {
        return validatorFactory
                .unwrap(HibernateValidatorFactory.class)
                .usingContext()
                .constraintValidatorPayload(getConfigurationValidationContext(integrationId, metadataId))
                .getValidator();
    }

    private ConfigurationValidationContext getConfigurationValidationContext(Long integrationId, Long metadataId) {

        Integration integration = integrationRequestProducerService
                .get(integrationId)
                .orElseThrow(() -> new CouldNotFindIntegrationException(integrationId));

        IntegrationMetadata metadata = metadataRequestProducerService
                .get(metadataId)
                .orElseThrow(() -> new CouldNotFindMetadataException(metadataId));

        InstanceMetadataContent instanceMetadata = instanceMetadataRequestProducerService
                .get(metadataId)
                .orElseThrow(() -> new CouldNotFindInstanceMetadataException(metadataId));

        return ConfigurationValidationContext
                .builder()
                .integration(integration)
                .metadata(metadata)
                .instanceValueTypePerKey(getInstanceValueTypePerKey(instanceMetadata))
                .build();

    }

    private Map<String, InstanceValueMetadata.Type> getInstanceValueTypePerKey(
            InstanceMetadataContent instanceMetadata
    ) {
        return getInstanceValueTypePerKeyEntries(instanceMetadata)
                .stream()
                .collect(Collectors.toMap(
                        AbstractMap.SimpleEntry::getKey,
                        AbstractMap.SimpleEntry::getValue
                ));
    }

    private List<AbstractMap.SimpleEntry<String, InstanceValueMetadata.Type>> getInstanceValueTypePerKeyEntries(
            InstanceMetadataContent instanceMetadataContent
    ) {
        return Stream.concat(
                        instanceMetadataContent
                                .getCategories()
                                .stream()
                                .map(InstanceMetadataCategory::getContent)
                                .map(this::getInstanceValueTypePerKeyEntries)
                                .flatMap(Collection::stream),
                        instanceMetadataContent
                                .getInstanceValueMetadata()
                                .stream()
                                .map(instanceValueMetadata -> new AbstractMap.SimpleEntry<>(
                                        instanceValueMetadata.getKey(),
                                        instanceValueMetadata.getType()
                                ))
                )
                .toList();
    }

}
