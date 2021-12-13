package no.fintlabs.integration.consumers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.Link;
import no.fint.model.resource.arkiv.noark.AdministrativEnhetResource;
import no.fintlabs.integration.cache.FintCacheManager;
import no.fintlabs.kafka.TopicNameService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Component
public class AdministrativEnhetResourceEntityConsumer extends EntityConsumer<AdministrativEnhetResource> {

    protected AdministrativEnhetResourceEntityConsumer(ObjectMapper objectMapper, FintCacheManager fintCacheManager) {
        super(objectMapper, fintCacheManager);
    }

    @Override
    protected String getResourceReference() {
        return "arkiv.noark.administrativenhet";
    }

    @Override
    protected Class<AdministrativEnhetResource> getResourceClass() {
        return AdministrativEnhetResource.class;
    }

    @Override
    protected List<String> getKeys(AdministrativEnhetResource resource) {
        String systemId = resource.getSystemId().getIdentifikatorverdi();
        Stream<String> selfLinks = resource.getSelfLinks().stream().map(Link::getHref);
        return Stream.concat(Stream.of(systemId), selfLinks).collect(Collectors.toList());
    }

    @Bean
    String administrativEnhetResourceEntityTopicName(TopicNameService topicNameService) {
        return topicNameService.generateEntityTopicName(this.getResourceReference());
    }

    @KafkaListener(topics = "#{administrativEnhetResourceEntityTopicName}")
    protected void kafkaListener(ConsumerRecord<String, String> consumerRecord) {
        this.processMessage(consumerRecord);
    }

}
