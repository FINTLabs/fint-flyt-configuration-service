package no.fintlabs.integration.kafka;

import no.fintlabs.integration.ConfigurationRepository;
import no.fintlabs.kafka.common.topic.TopicCleanupPolicyParameters;
import no.fintlabs.kafka.requestreply.ReplyProducerRecord;
import no.fintlabs.kafka.requestreply.RequestConsumerFactoryService;
import no.fintlabs.kafka.requestreply.topic.RequestTopicNameParameters;
import no.fintlabs.kafka.requestreply.topic.RequestTopicService;
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
            ConfigurationRepository configurationRepository
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
                no.fintlabs.integration.model.configuration.Configuration.class,
                (ConsumerRecord<String, Long> consumerRecord) -> ReplyProducerRecord
                        .<no.fintlabs.integration.model.configuration.Configuration>builder()
                        .value(configurationRepository.findById(consumerRecord.value()).orElse(null))
                        .build(),
                new CommonLoggingErrorHandler()
        ).createContainer(requestTopicNameParameters);
    }

}
