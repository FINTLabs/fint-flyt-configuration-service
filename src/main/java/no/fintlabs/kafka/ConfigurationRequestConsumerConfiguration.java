package no.fintlabs.kafka;

import no.fintlabs.ConfigurationService;
import no.fintlabs.kafka.common.topic.TopicCleanupPolicyParameters;
import no.fintlabs.kafka.requestreply.ReplyProducerRecord;
import no.fintlabs.kafka.requestreply.RequestConsumerFactoryService;
import no.fintlabs.kafka.requestreply.topic.RequestTopicNameParameters;
import no.fintlabs.kafka.requestreply.topic.RequestTopicService;
import no.fintlabs.model.configuration.dtos.ConfigurationDto;
import no.fintlabs.model.configuration.dtos.ConfigurationElementsDto;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.listener.CommonLoggingErrorHandler;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

@Configuration
public class ConfigurationRequestConsumerConfiguration {

    @Bean
    ConcurrentMessageListenerContainer<String, Long>
    configurationByConfigurationIdRequestConsumer(
            RequestTopicService requestTopicService,
            RequestConsumerFactoryService requestConsumerFactoryService,
            ConfigurationService configurationService
    ) {
        RequestTopicNameParameters requestTopicNameParameters = RequestTopicNameParameters
                .builder()
                .resource("configuration")
                .parameterName("configuration-id")
                .build();
        requestTopicService
                .ensureTopic(requestTopicNameParameters, 0, TopicCleanupPolicyParameters.builder().build());

        return requestConsumerFactoryService.createFactory(
                Long.class,
                ConfigurationDto.class,
                (ConsumerRecord<String, Long> consumerRecord) -> ReplyProducerRecord
                        .<ConfigurationDto>builder()
                        .value(configurationService.findById(consumerRecord.value(), true).orElse(null))
                        .build(),
                new CommonLoggingErrorHandler()
        ).createContainer(requestTopicNameParameters);
    }

    @Bean
    ConcurrentMessageListenerContainer<String, Long>
    configurationElementsByConfigurationIdRequestConsumer(
            RequestTopicService requestTopicService,
            RequestConsumerFactoryService requestConsumerFactoryService,
            ConfigurationService configurationService
    ) {
        RequestTopicNameParameters requestTopicNameParameters = RequestTopicNameParameters
                .builder()
                .resource("configuration-elements")
                .parameterName("configuration-id")
                .build();
        requestTopicService
                .ensureTopic(requestTopicNameParameters, 0, TopicCleanupPolicyParameters.builder().build());

        return requestConsumerFactoryService.createFactory(
                Long.class,
                ConfigurationElementsDto.class,
                (ConsumerRecord<String, Long> consumerRecord) -> ReplyProducerRecord
                        .<ConfigurationElementsDto>builder()
                        .value(
                                configurationService
                                        .findById(consumerRecord.value(), false)
                                        .map(ConfigurationDto::getElements)
                                        .map(ConfigurationElementsDto::new)
                                        .orElse(null))
                        .build(),
                new CommonLoggingErrorHandler()
        ).createContainer(requestTopicNameParameters);
    }

}
