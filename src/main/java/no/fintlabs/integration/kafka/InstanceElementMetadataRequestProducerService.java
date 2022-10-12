package no.fintlabs.integration.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import no.fintlabs.integration.model.metadata.InstanceElementMetadata;
import no.fintlabs.kafka.common.topic.TopicCleanupPolicyParameters;
import no.fintlabs.kafka.requestreply.RequestProducer;
import no.fintlabs.kafka.requestreply.RequestProducerFactory;
import no.fintlabs.kafka.requestreply.RequestProducerRecord;
import no.fintlabs.kafka.requestreply.topic.ReplyTopicNameParameters;
import no.fintlabs.kafka.requestreply.topic.ReplyTopicService;
import no.fintlabs.kafka.requestreply.topic.RequestTopicNameParameters;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class InstanceElementMetadataRequestProducerService {

    @Data
    @AllArgsConstructor
    private static class InstanceElementMetadataWrapper {
        private Collection<InstanceElementMetadata> instanceElementMetadata;
    }

    private final RequestTopicNameParameters requestTopicNameParameters;
    private final RequestProducer<Long, InstanceElementMetadataWrapper> requestProducer;

    public InstanceElementMetadataRequestProducerService(
            @Value("${fint.kafka.application-id}") String applicationId,
            RequestProducerFactory requestProducerFactory,
            ReplyTopicService replyTopicService
    ) {
        ReplyTopicNameParameters replyTopicNameParameters = ReplyTopicNameParameters.builder()
                .applicationId(applicationId)
                .resource("instance-element-metadata")
                .build();

        replyTopicService.ensureTopic(replyTopicNameParameters, 0, TopicCleanupPolicyParameters.builder().build());

        this.requestTopicNameParameters = RequestTopicNameParameters.builder()
                .resource("instance-element-metadata")
                .parameterName("metadata-id")
                .build();

        this.requestProducer = requestProducerFactory.createProducer(
                replyTopicNameParameters,
                Long.class,
                InstanceElementMetadataWrapper.class
        );
    }

    public Optional<Collection<InstanceElementMetadata>> get(Long metadataId) {
        return requestProducer.requestAndReceive(
                        RequestProducerRecord.<Long>builder()
                                .topicNameParameters(requestTopicNameParameters)
                                .value(metadataId)
                                .build()
                ).map(ConsumerRecord::value)
                .map(InstanceElementMetadataWrapper::getInstanceElementMetadata);
    }

}
