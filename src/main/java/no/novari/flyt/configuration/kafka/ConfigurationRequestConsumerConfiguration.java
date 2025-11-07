package no.novari.flyt.configuration.kafka;

import no.novari.flyt.configuration.ConfigurationService;
import no.fintlabs.kafka.consuming.ErrorHandlerConfiguration;
import no.fintlabs.kafka.consuming.ErrorHandlerFactory;
import no.fintlabs.kafka.requestreply.ReplyProducerRecord;
import no.fintlabs.kafka.requestreply.RequestListenerConfiguration;
import no.fintlabs.kafka.requestreply.RequestListenerContainerFactory;
import no.fintlabs.kafka.requestreply.topic.RequestTopicService;
import no.fintlabs.kafka.requestreply.topic.configuration.RequestTopicConfiguration;
import no.fintlabs.kafka.requestreply.topic.name.RequestTopicNameParameters;
import no.fintlabs.kafka.topic.name.TopicNamePrefixParameters;
import no.novari.flyt.configuration.model.configuration.dtos.ConfigurationDto;
import no.novari.flyt.configuration.model.configuration.dtos.ObjectMappingDto;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

import java.time.Duration;

@Configuration
public class ConfigurationRequestConsumerConfiguration {

    private static final Duration RETENTION_TIME = Duration.ofMinutes(5);

    @Bean
    ConcurrentMessageListenerContainer<String, Long>
    configurationByConfigurationIdRequestConsumer(
            RequestTopicService requestTopicService,
            RequestListenerContainerFactory requestListenerContainerFactory,
            ConfigurationService configurationService,
            ErrorHandlerFactory errorHandlerFactory) {
        RequestTopicNameParameters requestTopicNameParameters = RequestTopicNameParameters
                .builder()
                .topicNamePrefixParameters(TopicNamePrefixParameters
                        .builder()
                        .orgIdApplicationDefault()
                        .domainContextApplicationDefault()
                        .build()
                )
                .resourceName("configuration")
                .parameterName("configuration-id")
                .build();
        requestTopicService.createOrModifyTopic(requestTopicNameParameters, RequestTopicConfiguration
                .builder()
                .retentionTime(RETENTION_TIME)
                .build()
        );

        return requestListenerContainerFactory.createRecordConsumerFactory(
                Long.class,
                ConfigurationDto.class,
                (ConsumerRecord<String, Long> consumerRecord) -> ReplyProducerRecord
                        .<ConfigurationDto>builder()
                        .value(configurationService.findById(consumerRecord.value(), true).orElse(null))
                        .build(),
                RequestListenerConfiguration
                        .stepBuilder(Long.class)
                        .maxPollRecordsKafkaDefault()
                        .maxPollIntervalKafkaDefault()
                        .build(),
                errorHandlerFactory.createErrorHandler(
                        ErrorHandlerConfiguration
                                .stepBuilder()
                                .noRetries()
                                .skipFailedRecords()
                                .build()
                )
        ).createContainer(requestTopicNameParameters);
    }

    @Bean
    ConcurrentMessageListenerContainer<String, Long>
    mappingByConfigurationIdRequestConsumer(
            RequestTopicService requestTopicService,
            RequestListenerContainerFactory requestListenerContainerFactory,
            ConfigurationService configurationService,
            ErrorHandlerFactory errorHandlerFactory
    ) {
        RequestTopicNameParameters requestTopicNameParameters = RequestTopicNameParameters
                .builder()
                .topicNamePrefixParameters(TopicNamePrefixParameters
                        .builder()
                        .orgIdApplicationDefault()
                        .domainContextApplicationDefault()
                        .build()
                )
                .resourceName("mapping")
                .parameterName("configuration-id")
                .build();
        requestTopicService.createOrModifyTopic(requestTopicNameParameters, RequestTopicConfiguration
                .builder()
                .retentionTime(RETENTION_TIME)
                .build()
        );

        return requestListenerContainerFactory.createRecordConsumerFactory(
                Long.class,
                ObjectMappingDto.class,
                (ConsumerRecord<String, Long> consumerRecord) -> ReplyProducerRecord
                        .<ObjectMappingDto>builder()
                        .value(
                                configurationService
                                        .findById(consumerRecord.value(), false)
                                        .map(ConfigurationDto::getMapping)
                                        .orElse(null))
                        .build(),
                RequestListenerConfiguration
                        .stepBuilder(Long.class)
                        .maxPollRecordsKafkaDefault()
                        .maxPollIntervalKafkaDefault()
                        .build(),
                errorHandlerFactory.createErrorHandler(
                        ErrorHandlerConfiguration
                                .stepBuilder()
                                .noRetries()
                                .skipFailedRecords().build()
                )
        ).createContainer(requestTopicNameParameters);
    }

}
