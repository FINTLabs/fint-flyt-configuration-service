package no.fintlabs.kafka;

import no.fintlabs.kafka.consuming.ListenerConfiguration;
import no.fintlabs.kafka.requestreply.RequestProducerRecord;
import no.fintlabs.kafka.requestreply.RequestTemplate;
import no.fintlabs.kafka.requestreply.RequestTemplateFactory;
import no.fintlabs.kafka.requestreply.topic.ReplyTopicService;
import no.fintlabs.kafka.requestreply.topic.configuration.ReplyTopicConfiguration;
import no.fintlabs.kafka.requestreply.topic.name.ReplyTopicNameParameters;
import no.fintlabs.kafka.requestreply.topic.name.RequestTopicNameParameters;
import no.fintlabs.kafka.topic.name.TopicNamePrefixParameters;
import no.fintlabs.model.metadata.IntegrationMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Service
public class MetadataRequestProducerService {

    private final RequestTopicNameParameters requestTopicNameParameters;
    private final RequestTemplate<Long, IntegrationMetadata> requestTemplate;

    private static final Duration RETENTION_TIME = Duration.ofMinutes(5);
    private static final Duration REPLY_TIMEOUT = Duration.ofSeconds(5);

    public MetadataRequestProducerService(
            @Value("${fint.kafka.application-id}") String applicationId,
            RequestTemplateFactory requestTemplateFactory,
            ReplyTopicService replyTopicService
    ) {
        ReplyTopicNameParameters replyTopicNameParameters = ReplyTopicNameParameters.builder()
                .applicationId(applicationId)
                .topicNamePrefixParameters(TopicNamePrefixParameters
                        .builder()
                        .orgIdApplicationDefault()
                        .domainContextApplicationDefault()
                        .build()
                )
                .resourceName("metadata")
                .build();

        replyTopicService.createOrModifyTopic(replyTopicNameParameters, ReplyTopicConfiguration
                .builder()
                .retentionTime(RETENTION_TIME)
                .build()
        );

        this.requestTopicNameParameters = RequestTopicNameParameters
                .builder()
                .topicNamePrefixParameters(TopicNamePrefixParameters
                        .builder()
                        .orgIdApplicationDefault()
                        .domainContextApplicationDefault()
                        .build()
                )
                .resourceName("metadata")
                .parameterName("metadata-id")
                .build();

        this.requestTemplate = requestTemplateFactory.createTemplate(
                replyTopicNameParameters,
                Long.class,
                IntegrationMetadata.class,
                REPLY_TIMEOUT,
                ListenerConfiguration
                        .stepBuilder()
                        .groupIdApplicationDefault()
                        .maxPollRecordsKafkaDefault()
                        .maxPollIntervalKafkaDefault()
                        .continueFromPreviousOffsetOnAssignment()
                        .build()
        );
    }

    public Optional<IntegrationMetadata> get(Long metadataId) {
        return Optional.ofNullable(
                requestTemplate.requestAndReceive(
                        RequestProducerRecord.<Long>builder()
                                .topicNameParameters(requestTopicNameParameters)
                                .value(metadataId)
                                .build()
                ).value()
        );
    }

}
