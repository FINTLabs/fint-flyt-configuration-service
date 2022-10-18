package no.fintlabs.integration.validation;

import no.fintlabs.integration.kafka.InstanceElementMetadataRequestProducerService;
import no.fintlabs.integration.kafka.IntegrationRequestProducerService;
import no.fintlabs.integration.kafka.MetadataRequestProducerService;
import no.fintlabs.integration.model.integration.Integration;
import no.fintlabs.integration.model.metadata.InstanceElementMetadata;
import no.fintlabs.integration.model.metadata.IntegrationMetadata;
import org.hibernate.validator.HibernateValidatorFactory;
import org.springframework.stereotype.Service;

import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ConfigurationValidatorFacory {

    private final ValidatorFactory validatorFactory;

    private final IntegrationRequestProducerService integrationRequestProducerService;
    private final MetadataRequestProducerService metadataRequestProducerService;
    private final InstanceElementMetadataRequestProducerService instanceElementMetadataRequestProducerService;

    public ConfigurationValidatorFacory(
            ValidatorFactory validatorFactory,
            IntegrationRequestProducerService integrationRequestProducerService,
            MetadataRequestProducerService metadataRequestProducerService,
            InstanceElementMetadataRequestProducerService instanceElementMetadataRequestProducerService
    ) {
        this.validatorFactory = validatorFactory;
        this.integrationRequestProducerService = integrationRequestProducerService;
        this.metadataRequestProducerService = metadataRequestProducerService;
        this.instanceElementMetadataRequestProducerService = instanceElementMetadataRequestProducerService;
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

        Collection<InstanceElementMetadata> instanceElementMetadata = instanceElementMetadataRequestProducerService
                .get(metadataId)
                .orElseThrow(() -> new CouldNotFindInstanceElementMetadataException(metadataId));

        return ConfigurationValidationContext
                .builder()
                .integration(integration)
                .metadata(metadata)
                .metadataInstanceFieldTypePerKey(getInstanceFieldTypePerKey(instanceElementMetadata))
                .build();

    }

    private Map<String, InstanceElementMetadata.Type> getInstanceFieldTypePerKey(
            Collection<InstanceElementMetadata> instanceElementMetadata
    ) {
        return getInstanceFieldTypePerKeyEntries(instanceElementMetadata)
                .stream()
                .collect(Collectors.toMap(
                        AbstractMap.SimpleEntry::getKey,
                        AbstractMap.SimpleEntry::getValue
                ));
    }

    private List<AbstractMap.SimpleEntry<String, InstanceElementMetadata.Type>> getInstanceFieldTypePerKeyEntries(
            Collection<InstanceElementMetadata> instanceElementMetadata
    ) {
        return instanceElementMetadata.stream()
                .map(this::getInstanceFieldTypePerKeyEntries)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private List<AbstractMap.SimpleEntry<String, InstanceElementMetadata.Type>> getInstanceFieldTypePerKeyEntries(
            InstanceElementMetadata instanceElementMetadata
    ) {
        List<AbstractMap.SimpleEntry<String, InstanceElementMetadata.Type>> typesPerKey =
                getInstanceFieldTypePerKeyEntries(instanceElementMetadata.getChildren());
        if (instanceElementMetadata.getKey().isPresent() && instanceElementMetadata.getType().isPresent()) {
            typesPerKey.add(new AbstractMap.SimpleEntry<>(
                    instanceElementMetadata.getKey().get(),
                    instanceElementMetadata.getType().get()
            ));
        }
        return typesPerKey;
    }
}
