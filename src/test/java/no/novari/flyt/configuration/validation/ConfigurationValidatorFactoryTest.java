package no.novari.flyt.configuration.validation;

import no.novari.flyt.configuration.kafka.InstanceMetadataRequestProducerService;
import no.novari.flyt.configuration.kafka.IntegrationRequestProducerService;
import no.novari.flyt.configuration.kafka.MetadataRequestProducerService;
import no.novari.flyt.configuration.model.integration.Integration;
import no.novari.flyt.configuration.model.metadata.InstanceMetadataContent;
import no.novari.flyt.configuration.model.metadata.IntegrationMetadata;
import org.hibernate.validator.HibernateValidatorContext;
import org.hibernate.validator.HibernateValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

class ConfigurationValidatorFactoryTest {

    @Mock
    private ValidatorFactory validatorFactory;
    @Mock
    private IntegrationRequestProducerService integrationRequestProducerService;
    @Mock
    private MetadataRequestProducerService metadataRequestProducerService;
    @Mock
    private InstanceMetadataRequestProducerService instanceMetadataRequestProducerService;

    private ConfigurationValidatorFactory configurationValidatorFactory;

    @BeforeEach
    void setUp() {
        integrationRequestProducerService = mock(IntegrationRequestProducerService.class, RETURNS_DEEP_STUBS);
        metadataRequestProducerService = mock(MetadataRequestProducerService.class, RETURNS_DEEP_STUBS);
        instanceMetadataRequestProducerService = mock(InstanceMetadataRequestProducerService.class, RETURNS_DEEP_STUBS);
        validatorFactory = mock(ValidatorFactory.class, RETURNS_DEEP_STUBS);
        configurationValidatorFactory = new ConfigurationValidatorFactory(validatorFactory, integrationRequestProducerService, metadataRequestProducerService, instanceMetadataRequestProducerService);

        HibernateValidatorFactory hibernateValidatorFactory = mock(HibernateValidatorFactory.class);
        HibernateValidatorContext hibernateValidatorContext = mock(HibernateValidatorContext.class);

        when(validatorFactory.unwrap(HibernateValidatorFactory.class)).thenReturn(hibernateValidatorFactory);
        when(hibernateValidatorFactory.usingContext()).thenReturn(hibernateValidatorContext);
        when(hibernateValidatorContext.constraintValidatorPayload(any())).thenReturn(hibernateValidatorContext);
        when(hibernateValidatorContext.getValidator()).thenReturn(mock(Validator.class));
    }

    @Test
    void getValidatorTest() {
        Integration mockIntegration = Integration.builder()
                .sourceApplicationId(1L)
                .sourceApplicationIntegrationId("id")
                .destination("destination")
                .state(Integration.State.ACTIVE)
                .activeConfigurationId("configId")
                .build();

        IntegrationMetadata mockMetadata = IntegrationMetadata.builder()
                .sourceApplicationId(1L)
                .sourceApplicationIntegrationId("id")
                .sourceApplicationIntegrationUri("uri")
                .integrationDisplayName("name")
                .version(1L)
                .build();

        InstanceMetadataContent mockInstanceMetadata = InstanceMetadataContent.builder()
                .categories(Collections.emptyList())
                .instanceObjectCollectionMetadata(Collections.emptyList())
                .instanceValueMetadata(Collections.emptyList())
                .build();

        when(integrationRequestProducerService.get(any())).thenReturn(Optional.of(mockIntegration));
        when(metadataRequestProducerService.get(any())).thenReturn(Optional.of(mockMetadata));
        when(instanceMetadataRequestProducerService.get(any())).thenReturn(Optional.of(mockInstanceMetadata));

        Validator result = configurationValidatorFactory.getValidator(1L, 1L);

        assertNotNull(result);
    }

    @Test
    void getValidatorIntegrationNotFoundTest() {
        when(integrationRequestProducerService.get(anyLong())).thenReturn(Optional.empty());

        assertThrows(CouldNotFindIntegrationException.class, () -> configurationValidatorFactory.getValidator(1L, 1L));
    }

    @Test
    void getValidatorMetadataNotFoundTest() {
        Integration mockIntegration = Integration.builder()
                .sourceApplicationId(1L)
                .sourceApplicationIntegrationId("id")
                .destination("destination")
                .state(Integration.State.ACTIVE)
                .activeConfigurationId("configId")
                .build();

        when(integrationRequestProducerService.get(any())).thenReturn(Optional.of(mockIntegration));
        when(metadataRequestProducerService.get(any())).thenReturn(Optional.empty());

        assertThrows(CouldNotFindMetadataException.class, () -> configurationValidatorFactory.getValidator(1L, 1L));
    }

    @Test
    void getValidatorInstanceMetadataNotFoundTest() {
        Integration mockIntegration = Integration.builder()
                .sourceApplicationId(1L)
                .sourceApplicationIntegrationId("id")
                .destination("destination")
                .state(Integration.State.ACTIVE)
                .activeConfigurationId("configId")
                .build();

        IntegrationMetadata mockMetadata = IntegrationMetadata.builder()
                .sourceApplicationId(1L)
                .sourceApplicationIntegrationId("id")
                .sourceApplicationIntegrationUri("uri")
                .integrationDisplayName("name")
                .version(1L)
                .build();

        when(integrationRequestProducerService.get(any())).thenReturn(Optional.of(mockIntegration));
        when(metadataRequestProducerService.get(any())).thenReturn(Optional.of(mockMetadata));
        when(instanceMetadataRequestProducerService.get(any())).thenReturn(Optional.empty());

        assertThrows(CouldNotFindInstanceMetadataException.class, () -> configurationValidatorFactory.getValidator(1L, 1L));
    }

}
