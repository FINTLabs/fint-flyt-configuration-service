package no.fintlabs.integration.cache;

import no.fintlabs.integration.cache.ehcache.FintEhCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FintCacheConfiguration {

    @Bean
    public FintCacheManager fintCacheManager() {
        return new FintEhCacheManager();
    }
}
