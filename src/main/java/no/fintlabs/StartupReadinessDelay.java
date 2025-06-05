package no.fintlabs;

import org.springframework.boot.availability.AvailabilityChangeEvent;
import org.springframework.boot.availability.ReadinessState;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class StartupReadinessDelay implements ApplicationListener<ApplicationReadyEvent> {

    private final ConfigurableApplicationContext context;

    public StartupReadinessDelay(ConfigurableApplicationContext context) {
        this.context = context;
    }

    @Override
    public void onApplicationEvent(@NonNull ApplicationReadyEvent event) {
        try {
            Thread.sleep(90_000);
        } catch (InterruptedException ignored) {
        }
        AvailabilityChangeEvent.publish(context, ReadinessState.ACCEPTING_TRAFFIC);
    }
}
